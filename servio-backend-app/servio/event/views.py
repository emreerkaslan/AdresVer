from django.http import HttpResponse, JsonResponse
from rest_framework import generics
from rest_framework.decorators import api_view

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


@api_view(['PUT'])
def attend(request, event, attendee):
    try:
        event = Event.objects.get(pk=event)
        from servio.user.models import User
        attendee = User.objects.get(pk=attendee)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'PUT':
        if event.hasQuota and event.quota <= event.attendees.size:
            serializer = EventSerializer(event)
            return JsonResponse(serializer.errors, status=400)
        event.attendees.add(attendee)
        serializer = EventSerializer(event)
        return JsonResponse(serializer.data)
    else:
        serializer = EventSerializer(event)
        return JsonResponse(serializer.errors, status=400)


@api_view(['PUT'])
def cancel(request, event, attendee):
    try:
        event = Event.objects.get(pk=event)
        from servio.user.models import User
        attendee = User.objects.get(pk=attendee)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'PUT':
        if attendee in event.attendees:
            event.attendees.remove(attendee)
            serializer = EventSerializer(event)
            return JsonResponse(serializer.data)
        else:
            serializer = EventSerializer(event)
            return JsonResponse(serializer.errors, status=400)
    else:
        serializer = EventSerializer(event)
        return JsonResponse(serializer.errors, status=400)
