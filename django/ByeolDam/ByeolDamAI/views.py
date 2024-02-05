import os
import requests
from django.http import JsonResponse
from rest_framework.response import Response
from django.views.decorators.http import require_POST
from django.views.decorators.csrf import csrf_exempt
from django.shortcuts import render
import detectron2
import torch
from detectron2.utils.logger import setup_logger
import numpy as np
import os, json, cv2, random
from detectron2 import model_zoo
from detectron2.engine import DefaultPredictor
from detectron2.config import get_cfg
from detectron2.utils.visualizer import Visualizer
from detectron2.data import MetadataCatalog, DatasetCatalog
from matplotlib import pyplot as plt
import pandas as pd
from shapely.geometry import Polygon
import time
from rest_framework.decorators import api_view

@require_POST
@csrf_exempt
def getImage(request):
    if request.method == 'POST':
        url = request.POST.get('url').strip('{}')

        # URL에서 이미지 다운로드
        response = requests.get(url)
        if response.status_code == 200:
            # 이미지를 저장할 경로 설정 (예시: media 폴더 내에 저장)
            base_path = 'media/'
            filename = os.path.join(base_path, 'downloaded_image.jpg')

            # 이미지 저장
            f = open(filename, 'wb')
            f.write(response.content)
            return JsonResponse({'image_url': "downloaded_image.jpg"})
        else:
            return JsonResponse({'error': 'Failed to download image'}, status=500)

    return JsonResponse({'error': 'Invalid request method'}, status=400)

"""
request에는 이미지 이름이 들어온다.
runAI함수는 해당 이미지를 read하고 segmentation을 돌려서 윤곽선 데이터를 return한다.
"""
def runAI(request):
    TORCH_VERSION = ".".join(torch.__version__.split(".")[:2])
    CUDA_VERSION = torch.__version__.split("+")[-1]
    print("torch: ", TORCH_VERSION, ", cuda: ", CUDA_VERSION)
    print("detectron2: ", detectron2.__version__)

    setup_logger()

    start = time.time()
    imageFileName = getImage(request)["image_url"]
    image = cv2.imread(imageFileName)

    if image is None:
        return "이미지를 불러오는데 실패하였습니다!"
    
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

    cfg = get_cfg()
    cfg.merge_from_file(model_zoo.get_config_file("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml"))
    cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = 0.5
    predictor = DefaultPredictor(cfg)
    outputs = predictor(image)
    cfg.MODEL.WEIGHTS = model_zoo.get_checkpoint_url("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml")

    boxes = (outputs["instances"].pred_boxes.tensor.cpu().numpy())

    masks = (outputs["instances"].pred_masks.cpu().numpy().astype('uint8'))
    contours = []
    for pred_mask in outputs["instances"].pred_masks:
        mask = pred_mask.cpu().numpy().astype('uint8')
        contours.append(contour[0])
        contour, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_NONE)
    
    dataset = []
    counter_box = 0
    for i, box in enumerate(boxes):
        data = {}
        data["image_width"] = outputs["instances"].image_size[1]
        data["image_height"] = outputs["instances"].image_size[0]
        data["bounding_box"] = box.tolist()
        data["contours"] = contours[i].tolist()
        data["id"] = f"id_{counter_box}"
        counter_box += 1
        dataset.append(data)

    print(f"{time.time()-start: 4f} sec")
    return dataset