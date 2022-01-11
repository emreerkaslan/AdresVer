from rest_framework.test import APITestCase
from django.urls import reverse
from rest_framework import status


class CreateFeedbackListCreate(APITestCase):

    def test_dont_create_feedback_wo_auth(self):
        test_user = {"nameValuePairs": {"username": "testusername", "name": "testname", "email": "testemail",
                                        "password": "12345678", "bio": "testbio",
                                        "competency": "testcompetency", "interest": "testinterest",
                                        "geolocation": "testgeolocation"}}
        response = self.client.post(reverse("create-user"), test_user, format='json')

        test_user2 = {"nameValuePairs": {"username": "testusername2", "name": "testname", "email": "testemail2",
                                        "password": "12345678", "bio": "testbio",
                                        "competency": "testcompetency", "interest": "testinterest",
                                        "geolocation": "testgeolocation"}}
        response2 = self.client.post(reverse("create-user"), test_user2, format='json')

        test_feedback = {"rating": 5, "comment": "testcomment", "giver": response.get('pk'),
                        "taker": response2.get('pk'),}
        data = self.client.post(reverse("create-event"), test_feedback, format='json')

        self.assertEqual(data.status_code, status.HTTP_401_UNAUTHORIZED)
