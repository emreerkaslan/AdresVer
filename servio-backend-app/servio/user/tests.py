from django.test import TestCase
from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status


class CreateUserListCreate(APITestCase):

    def test_create_user(self):
        test_user = {"username":"testusername", "name":"testname", "email":"testemail", "password":"12345678", "bio":"testbio",
                     "competency":"testcompetency", "interest":"testinterest", "geolocation":"testgeolocation"}
        response = self.client.post(reverse("create-user"), test_user)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
