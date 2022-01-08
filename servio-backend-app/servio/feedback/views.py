from django.http import JsonResponse
from rest_framework import generics
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from .models import Feedback
from .serializers import FeedbackSerializer


class FeedbackCreate(generics.CreateAPIView):
    # API endpoint that allows creation of a new Feedback
    queryset = Feedback.objects.all(),
    serializer_class = FeedbackSerializer


class FeedbackList(generics.ListAPIView):
    # API endpoint that allows Feedback to be viewed.
    queryset = Feedback.objects.all()
    serializer_class = FeedbackSerializer


class FeedbackDetail(generics.RetrieveAPIView):
    # API endpoint that returns a single Feedback by pk.
    queryset = Feedback.objects.all()
    serializer_class = FeedbackSerializer


class FeedbackUpdate(generics.RetrieveUpdateAPIView):
    # API endpoint that allows a Feedback record to be updated.
    queryset = Feedback.objects.all()
    serializer_class = FeedbackSerializer


class FeedbackDelete(generics.RetrieveDestroyAPIView):
    # API endpoint that allows a Feedback record to be deleted.
    queryset = Feedback.objects.all()
    serializer_class = FeedbackSerializer


@api_view(['POST'])
def getFeedbacks(request):
    data = request.data["nameValuePairs"]['data']['values']
    print(data)
    if request.method == 'POST':
        list = (pk for pk in data)
        print(list)
        feedbacks = Feedback.objects.filter(pk__in=list)
        print(feedbacks)
        serializer = FeedbackSerializer(feedbacks, many=True)
        return Response(serializer.data)
    else:
        serializer = FeedbackSerializer()
        return JsonResponse(serializer.errors, status=400)