from django.http import HttpResponse, JsonResponse
from rest_framework import generics, filters
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from .models import Event
from .serializers import EventSerializer


class EventCreate(generics.CreateAPIView):
    # API endpoint that allows creation of a new Event
    permission_classes = (IsAuthenticated)
    queryset = Event.objects.all(),
    serializer_class = EventSerializer


class EventList(generics.ListAPIView):
    # API endpoint that allows Event to be viewed.
    queryset = Event.objects.all()
    serializer_class = EventSerializer
    filter_backends = [filters.SearchFilter]
    search_fields = ['title', 'description', 'geolocation']


class EventDetail(generics.RetrieveAPIView):
    # API endpoint that returns a single Event by pk.
    queryset = Event.objects.all()
    serializer_class = EventSerializer


class EventUpdate(generics.RetrieveUpdateAPIView):
    # API endpoint that allows a Event record to be updated.
    permission_classes = (IsAuthenticated)
    queryset = Event.objects.all()
    serializer_class = EventSerializer


class EventDelete(generics.RetrieveDestroyAPIView):
    # API endpoint that allows a Event record to be deleted.
    queryset = Event.objects.all()
    serializer_class = EventSerializer


@api_view(['POST'])
@permission_classes([IsAuthenticated])
def attend(request, event, attendee):
    try:
        event = Event.objects.get(pk=event)
    except Event.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'POST':
        if event.hasQuota and event.quota <= event.attendees.size:
            serializer = EventSerializer(event)
            return JsonResponse(serializer.errors, status=400)
        event.attendees.add(attendee)
        serializer = EventSerializer(event)
        return Response(serializer.data)
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

@api_view(['GET'])
def getEvent(request, pk):
    print(pk)
    if request.method == 'GET':
        events = Event.objects.filter(organizer=pk)
        serializer = EventSerializer(events, many=True)
        print(serializer)
        return Response(serializer.data)
        #return JsonResponse(serializer.data, status=200)
    else:
        return JsonResponse(TypeError, status=400)