from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status


class CreateEventListCreate(APITestCase):

    def test_dont_create_event_wo_auth(self):
        test_user = {"nameValuePairs": {"username": "testusername", "name": "testname", "email": "testemail",
                                        "password": "12345678", "bio": "testbio",
                                        "competency": "testcompetency", "interest": "testinterest",
                                        "geolocation": "testgeolocation"}}
        response = self.client.post(reverse("create-user"), test_user, format='json')

        test_event = {"title": "testtitle", "description": "testdescription", "date": "2018-11-20T15:58:44Z",
                        "geolocation": "testgeolocation", "organizer": response.get('pk'),
                        "hasQuota": False}
        data = self.client.post(reverse("create-event"), test_event, format='json')

        self.assertEqual(data.status_code, status.HTTP_401_UNAUTHORIZED)
