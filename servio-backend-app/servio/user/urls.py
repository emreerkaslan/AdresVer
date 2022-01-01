from django.urls import path
from rest_framework.authtoken.views import obtain_auth_token

from .views import UserCreate, UserList, UserDetail, UserUpdate, UserDelete, login, loginCheck

urlpatterns = [
    path('create/', UserCreate.as_view(), name='create-user'),
    path('', UserList.as_view()),
    path('<int:pk>/', UserDetail.as_view(), name='retrieve-user'),
    path('update/<int:pk>/', UserUpdate.as_view(), name='update-user'),
    path('delete/<int:pk>/', UserDelete.as_view(), name='delete-user'),
    path('delete/<int:pk>/', UserDelete.as_view(), name='delete-user'),
    path('login/', login, name='login'),
    path('<str:username>/', loginCheck, name='login-check'),
]
