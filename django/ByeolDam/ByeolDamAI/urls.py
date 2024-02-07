from django.urls import path
from . import views

app_name = "ByeolDamAI"
urlpatterns = [
    path("getImage/", views.getImage),
    path("runAI/", views.runAI)
]
