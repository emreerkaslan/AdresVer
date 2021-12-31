from rest_framework import generics
from rest_framework.permissions import IsAuthenticated

from .models import Event
from .serializers import EventSerializer


class EventCreate(generics.CreateAPIView):
    # API endpoint that allows creation of a new Event
    queryset = Event.objects.all(),
    serializer_class = EventSerializer


class EventList(generics.ListAPIView):
    # API endpoint that allows Event to be viewed.
    queryset = Event.objects.all()
    serializer_class = EventSerializer


class EventDetail(generics.RetrieveAPIView):
    # API endpoint that returns a single Event by pk.
    queryset = Event.objects.all()
    serializer_class = EventSerializer


class EventUpdate(generics.RetrieveUpdateAPIView):
    # API endpoint that allows a Event record to be updated.
    queryset = Event.objects.all()
    serializer_class = EventSerializer


class EventDelete(generics.RetrieveDestroyAPIView):
    # API endpoint that allows a Event record to be deleted.
    queryset = Event.objects.all()
    serializer_class = EventSerializer
