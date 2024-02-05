import os
import requests
from django.http import JsonResponse
from rest_framework.response import Response
from django.views.decorators.http import require_POST
from django.views.decorators.csrf import csrf_exempt

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
