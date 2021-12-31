from django.urls import path

from .views import FeedbackCreate, FeedbackList, FeedbackDetail, FeedbackUpdate, FeedbackDelete

urlpatterns = [
    path('create/', FeedbackCreate.as_view(), name='create-feedback'),
    path('', FeedbackList.as_view()),
    path('<int:pk>/', FeedbackDetail.as_view(), name='retrieve-feedback'),
    path('update/<int:pk>/', FeedbackUpdate.as_view(), name='update-feedback'),
    path('delete/<int:pk>/', FeedbackDelete.as_view(), name='delete-feedback'),
]