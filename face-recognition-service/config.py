'''
Author: David Ma
FilePath: \campus-trading-platform-management-system\face-recognition-service\config.py
LastEditors: David Ma
Description: 该文件用于数据库和 Redis 连接管理，以及人脸特征存储与匹配
Date: 2025-03-04 11:02:09
'''
import mysql.connector
import redis
import numpy as np
import logging
from mysql.connector import pooling
from face_utils import compare_faces

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# MySQL 连接池配置
MYSQL_CONFIG = {
    'host': '123.60.139.233',
    'port': 3306,
    'user': 'root',
    'password': '8121536',
    'database': 'ctp_mysql',
    'pool_name': 'face_recognition_pool',
    'pool_size': 10  # 允许最多 5 个并发连接
}
mysql_pool = pooling.MySQLConnectionPool(**MYSQL_CONFIG)

# Redis 连接池
REDIS_CONFIG = {
    'host': '123.60.139.233',
    'port': 6379,
    'password': '8121536',
    'db': 0,
}
redis_pool = redis.ConnectionPool(**REDIS_CONFIG)
r = redis.Redis(connection_pool=redis_pool)


def save_face_features(user_id, features):
    try:
        if features is None or len(features) != 128:
            raise ValueError("无效的人脸特征")

        features_bytes = features.astype(np.float64).tobytes()

        conn = mysql.connector.connect(**MYSQL_CONFIG)
        cursor = conn.cursor()

        # 确保 `user_id` 存在于 `users` 表
        cursor.execute("SELECT COUNT(*) FROM users WHERE user_id = %s", (user_id,))
        if cursor.fetchone()[0] == 0:
            raise ValueError(f"用户 ID {user_id} 不存在，无法存储人脸数据")

        # **使用 ON DUPLICATE KEY UPDATE**
        cursor.execute("""
            INSERT INTO face_features (user_id, feature_vector, created_at, updated_at) 
            VALUES (%s, %s, NOW(), NOW())
            ON DUPLICATE KEY UPDATE feature_vector = VALUES(feature_vector), updated_at = NOW()
        """, (user_id, features_bytes))

        conn.commit()

        # 存入 Redis（覆盖旧数据）
        r = redis.Redis(**REDIS_CONFIG)
        r.set(f"face:{user_id}", features_bytes)

        cursor.close()
        conn.close()

        logger.info(f"✅ 用户 {user_id} 的人脸特征已存入数据库和 Redis")
        return True  # 确保返回 True

    except Exception as e:
        logger.error(f"❌ 存储人脸特征失败: {str(e)}")
        return False  # 失败时返回 False




def find_matching_user(input_features):
    """
    在 Redis 中查找最匹配的用户
    :param input_features: 128 维人脸特征向量
    :return: (匹配的 user_id, {是否匹配, 置信度})
    """
    try:
        if input_features is None:
            logger.error("输入特征为空")
            return None, {'matched': False, 'confidence': 0.0}

        best_match = None
        best_score = -1

        # 使用 Redis SCAN 避免 keys() 带来的性能问题
        for key in r.scan_iter("face:*"):
            stored_features = np.frombuffer(r.get(key), dtype=np.float64)
            result = compare_faces(input_features, stored_features)

            if result['confidence'] > best_score:
                best_score = result['confidence']
                best_match = key.decode().split(":")[1]  # 提取 user_id

        return best_match, {
            'matched': bool(best_score >= 0.5),  # ✅ 修正：转换 `numpy.bool_` → Python `bool`
            'confidence': float(best_score)     # ✅ 修正：转换 `numpy.float_` → Python `float`
        }

    except Exception as e:
        logger.error(f"❌ 人脸匹配失败: {str(e)}")
        return None, {'matched': False, 'confidence': 0.0}

