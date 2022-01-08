from django.urls import path

from .views import ServiceCreate, ServiceList, ServiceDetail, ServiceUpdate, ServiceDelete, serviceCreate, getService, \
    acceptRequest, declineRequest, addRequest, addFeedback

urlpatterns = [
    path('create/', ServiceCreate.as_view(), name='create-service'),
    path('', ServiceList.as_view()),
    path('<int:pk>/', ServiceDetail.as_view(), name='retrieve-service'),
    path('update/<int:pk>/', ServiceUpdate.as_view(), name='update-service'),
    path('delete/<int:pk>/', ServiceDelete.as_view(), name='delete-service'),
    path('<int:pk>/set/', getService, name='service-servicelist'),
    path('accept/<int:service>/<int:requestmaker>/', acceptRequest, name='service-acceptrequest'),
    path('decline/<int:service>/<int:requestmaker>/', declineRequest, name='service-declinerequest'),
    path('request/<int:service>/<int:requestmaker>/', addRequest, name='service-addrequest'),
    path('feedback/<int:service>/<int:feedback>/', addFeedback, name='service-addfeedbak'),
]
