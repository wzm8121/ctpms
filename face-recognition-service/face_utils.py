'''
Author: David Ma
FilePath: \campus-trading-platform-management-system\face-recognition-service\face_utils.py
LastEditors: David Ma
Description: 该文件用于人脸检测、特征提取和比对
Date: 2025-03-04 11:01:34
'''
import cv2
import dlib
import numpy as np

# 加载 dlib 预训练模型
predictor = dlib.shape_predictor("face-recognition-service/models/shape_predictor_68_face_landmarks.dat")
face_rec = dlib.face_recognition_model_v1("face-recognition-service/models/dlib_face_recognition_resnet_model_v1.dat")

# 选择人脸检测器（HOG 默认，CNN 可选）
hog_detector = dlib.get_frontal_face_detector()
cnn_detector = dlib.cnn_face_detection_model_v1("face-recognition-service/models/mmod_human_face_detector.dat")


def detect_faces(image, use_cnn=False):
    """
    检测人脸
    :param image: OpenCV 读取的图像
    :param use_cnn: 是否使用 CNN 人脸检测（默认 False）
    :return: 人脸区域列表（dlib.rect）
    """
    # OpenCV 默认是 BGR，dlib 需要 RGB
    rgb_image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    if use_cnn:
        faces = cnn_detector(rgb_image, 1)
        return [f.rect for f in faces] if faces else []
    else:
        faces = hog_detector(rgb_image, 1)
        return faces


def extract_face_features(image, use_cnn=False):
    """
    提取人脸特征
    :param image: OpenCV 读取的图像
    :param use_cnn: 是否使用 CNN 进行人脸检测
    :return: 128 维特征向量
    """
    faces = detect_faces(image, use_cnn)
    if len(faces) == 0:
        return None, "未检测到人脸"

    # 选取最大人脸（适用于多张脸时）
    face_rect = max(faces, key=lambda r: r.width() * r.height())
    
    # 获取关键点
    shape = predictor(image, face_rect)
    
    # 计算 128 维特征
    face_descriptor = face_rec.compute_face_descriptor(image, shape)

    return np.array(face_descriptor, dtype=np.float64), "特征提取成功"


def compare_faces(face_features1, face_features2, threshold=0.6):
    """
    比对两张人脸的相似度
    :param face_features1: 第一张脸的 128 维特征
    :param face_features2: 第二张脸的 128 维特征
    :param threshold: 相似度阈值（推荐 0.6）
    :return: 匹配结果 (matched: 是否匹配, confidence: 置信度)
    """
    if face_features1 is None or face_features2 is None:
        return {'matched': False, 'confidence': 0.0}

    dist = np.linalg.norm(face_features1 - face_features2)
    confidence = max(1 - (dist / threshold), 0)
    
    return {'matched': dist < threshold, 'confidence': round(confidence, 2)}
