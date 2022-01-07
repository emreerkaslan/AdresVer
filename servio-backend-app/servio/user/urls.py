from django.urls import path
from .views import UserList, UserDetail, UserUpdate, UserDelete, login, loginCheck, getFeedback, getService, follow, unfollow, userCreate, getUsers

urlpatterns = [
    path('create/', userCreate, name='create-user'),
    path('', UserList.as_view()),
    path('<int:pk>/', UserDetail.as_view(), name='retrieve-user'),
    path('set/', getUsers, name='retrieve-users'),
    path('update/<int:pk>/', UserUpdate.as_view(), name='update-user'),
    path('delete/<int:pk>/', UserDelete.as_view(), name='delete-user'),
    path('delete/<int:pk>/', UserDelete.as_view(), name='delete-user'),
    path('login/', login, name='login'),
    path('<str:username>/', loginCheck, name='login-check'),
    path('<int:pk>/feedback', getFeedback, name='user-feedbacks'),
    path('<int:pk>/service/', getService, name='user-services'),
    path('follow/<int:follower>/<int:followed>', follow, name='user-follow'),
    path('unfollow/<int:follower>/<int:followed>', unfollow, name='user-unfollow'),
]
