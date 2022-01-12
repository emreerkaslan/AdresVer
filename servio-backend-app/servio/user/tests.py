from django.test import TestCase
from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status

class CreateUserListCreate(APITestCase):

    def test_create_user(self):
        test_user = {"nameValuePairs": {"username": "testusername", "name": "testname", "email": "testemail", "password": "12345678", "bio": "testbio",
                     "competency": "testcompetency", "interest": "testinterest", "geolocation": "testgeolocation"}}
        response = self.client.post(reverse("create-user"), test_user, format='json')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)

    def test_login_user(self):
        test_user = {"nameValuePairs": {"username": "testusername", "name": "testname", "email": "testemail", "password": "12345678", "bio": "testbio",
                     "competency": "testcompetency", "interest": "testinterest", "geolocation": "testgeolocation"}}
        self.client.post(reverse("create-user"), test_user, format='json')
        login_data = {"username": "testusername", "password": "12345678"}
        response = self.client.post(reverse('login'), login_data, format='json')

        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_authenticate(self):
        test_user = {"nameValuePairs": {"username": "testusername", "name": "testname", "email": "testemail", "password": "12345678", "bio": "testbio",
                     "competency": "testcompetency", "interest": "testinterest", "geolocation": "testgeolocation"}}
        self.client.post(reverse("create-user"), test_user, format='json')
        login_data = {"username": "testusername", "password": "12345678"}
        token = self.client.post(reverse('login'), login_data, format='json')
        self.client.credentials(HTTP_AUTHORIZATION=f"Bearer {token.data['token']}")



