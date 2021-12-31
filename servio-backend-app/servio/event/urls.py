from django.urls import path

from .views import EventCreate, EventList, EventDetail, EventUpdate, EventDelete

urlpatterns = [
    path('create/', EventCreate.as_view(), name='create-event'),
    path('', EventList.as_view()),
    path('<int:pk>/', EventDetail.as_view(), name='retrieve-event'),
    path('update/<int:pk>/', EventUpdate.as_view(), name='update-event'),
    path('delete/<int:pk>/', EventDelete.as_view(), name='delete-event'),
]