'''
Author: David Ma
FilePath: \campus-trading-platform-management-system\face-recognition-service\app.py
LastEditors: David Ma
Description: 该文件用于...
Date: 2025-03-08 15:24:42
'''
from flask import Flask, request, jsonify

import face_utils
import config
import cv2
import numpy as np

app = Flask(__name__)

import logging

# 设置日志级别为 DEBUG，确保所有日志都能输出
logging.basicConfig(level=logging.DEBUG)

app.logger.setLevel(logging.DEBUG)

@app.route('/register', methods=['POST'])
def register_face():
    """ 处理用户人脸注册 """
    try:
        if 'file' not in request.files:
            return jsonify({'status': 'error', 'message': '未上传文件'}), 400

        file = request.files['file']
        image = cv2.imdecode(np.frombuffer(file.read(), np.uint8), cv2.IMREAD_COLOR)

        # 提取人脸特征
        face_features, msg = face_utils.extract_face_features(image)
        if face_features is None:
            app.logger.error(f"❌ 人脸注册失败: {msg}")
            return jsonify({'status': 'error', 'message': msg}), 400

        user_id = request.form.get('userId')
        if not user_id:
            return jsonify({'status': 'error', 'message': '缺少 userId'}), 400

        # 存储人脸特征
        success = config.save_face_features(user_id, face_features)
        if success:
            app.logger.info(f"✅ 用户 {user_id} 人脸注册成功")
            return jsonify({'status': 'success', 'message': '🎉 人脸注册成功'})
        else:
            app.logger.error(f"❌ 数据库存储失败（save_face_features 返回 False）")
            return jsonify({'status': 'error', 'message': '数据库存储失败'}), 500

    except Exception as e:
        app.logger.error(f"❌ 注册失败: {str(e)}")
        return jsonify({'status': 'error', 'message': str(e)}), 500



@app.route('/verify', methods=['POST'])
def verify_face():
    """ 处理人脸验证 """
    try:
        if 'file' not in request.files:
            return jsonify({'status': 'error', 'message': '未上传文件'}), 400

        file = request.files['file']
        image = cv2.imdecode(np.frombuffer(file.read(), np.uint8), cv2.IMREAD_COLOR)

        # 提取人脸特征
        input_features, msg = face_utils.extract_face_features(image)
        if input_features is None:
            app.logger.error(f"❌ 人脸验证失败: {msg}")
            return jsonify({'status': 'error', 'message': msg}), 400

        # 进行人脸匹配
        matched_user, match_result = config.find_matching_user(input_features)

        if matched_user:
            app.logger.info(f"✅ 人脸匹配成功: 用户 {matched_user}, 置信度 {match_result['confidence']}")
        else:
            app.logger.warning("⚠️ 未找到匹配用户")

        return jsonify({
            'status': 'success',
            'matched': match_result['matched'],
            'confidence': match_result['confidence'],
            'userId': matched_user
        })

    except Exception as e:
        app.logger.error(f"❌ 验证失败: {str(e)}")
        return jsonify({'status': 'error', 'message': str(e)}), 500


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
