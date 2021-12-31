from rest_framework import generics
from rest_framework.permissions import IsAuthenticated

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
