from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status


class CreateServiceListCreate(APITestCase):

    def test_dont_create_service_wo_auth(self):
        test_user = {"nameValuePairs": {"username": "testusername", "name": "testname", "email": "testemail",
                                        "password": "12345678", "bio": "testbio",
                                        "competency": "testcompetency", "interest": "testinterest",
                                        "geolocation": "testgeolocation"}}
        response = self.client.post(reverse("create-user"), test_user, format='json')

        test_service = {"title": "testtitle", "description": "testdescription", "date": "2018-11-20T15:58:44Z",
                        "geolocation": "testgeolocation", "giver": response.get('pk'),
                        "recurring": False, "credits": 4}
        data = self.client.post(reverse("create-service"), test_service, format='json')

        self.assertEqual(data.status_code, status.HTTP_400_BAD_REQUEST)

    '''def test_create_service(self):
        usertest.test_authenticate()
        test_user = {"nameValuePairs": {"username": "testusername", "name": "testname", "email": "testemail",
                                        "password": "12345678", "bio": "testbio",
                                        "competency": "testcompetency", "interest": "testinterest",
                                        "geolocation": "testgeolocation"}}
        response = self.client.post(reverse("create-user"), test_user, format='json')

        test_service = {"title": "testtitle", "description": "testdescription", "date": "2018-11-20T15:58:44Z", "geolocation": "testgeolocation", "giver": response.get('pk'),
                     "recurring": False, "credits": 4}
        data = self.client.post(reverse("create-service"), test_service, format='json')

        self.assertEqual(data.status_code, status.HTTP_201_CREATED)'''