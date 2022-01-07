from django.urls import path

from .views import ServiceCreate, ServiceList, ServiceDetail, ServiceUpdate, ServiceDelete, serviceCreate, getService

urlpatterns = [
    path('create/', serviceCreate, name='create-service'),
    path('', ServiceList.as_view()),
    path('<int:pk>/', ServiceDetail.as_view(), name='retrieve-service'),
    path('update/<int:pk>/', ServiceUpdate.as_view(), name='update-service'),
    path('delete/<int:pk>/', ServiceDelete.as_view(), name='delete-service'),
    path('<int:pk>/set/', getService, name='user-servicelist'),
]
