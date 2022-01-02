from django.http import HttpResponse, JsonResponse
from rest_framework import generics
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated

from .models import Service
from .serializers import ServiceSerializer


class ServiceCreate(generics.CreateAPIView):
    # API endpoint that allows creation of a new Service
    queryset = Service.objects.all(),
    serializer_class = ServiceSerializer


class ServiceList(generics.ListAPIView):
    # API endpoint that allows Service to be viewed.
    queryset = Service.objects.all()
    serializer_class = ServiceSerializer


class ServiceDetail(generics.RetrieveAPIView):
    # API endpoint that returns a single Service by pk.
    queryset = Service.objects.all()
    serializer_class = ServiceSerializer


class ServiceUpdate(generics.RetrieveUpdateAPIView):
    # API endpoint that allows a Service record to be updated.
    queryset = Service.objects.all()
    serializer_class = ServiceSerializer


class ServiceDelete(generics.RetrieveDestroyAPIView):
    # API endpoint that allows a Service record to be deleted.
    queryset = Service.objects.all()
    serializer_class = ServiceSerializer


@api_view(['PUT'])
@permission_classes([IsAuthenticated])
def addRequest(request, service, requestmaker):
    try:
        from servio.user.models import User
        requestmaker = User.objects.get(pk=requestmaker)
        service = Service.objects.get(pk=service)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'PUT':
        if requestmaker not in service.requests and requestmaker.credits >= service.credits:
            service.requests.add(requestmaker)
            requestmaker.credits = requestmaker.credits - service.credits
        serializer = ServiceSerializer(service)
        return JsonResponse(serializer.data)
    else:
        serializer = ServiceSerializer(service)
        return JsonResponse(serializer.errors, status=400)

@api_view(['PUT'])
@permission_classes([IsAuthenticated])
def removeRequest(request, service, requestmaker):
    try:
        from servio.user.models import User
        requestmaker = User.objects.get(pk=requestmaker)
        service = Service.objects.get(pk=service)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'PUT':
        if requestmaker in service.requests:
            service.requests.remove(requestmaker)
            requestmaker.credits = requestmaker.credits + service.credits
        serializer = ServiceSerializer(service)
        return JsonResponse(serializer.data)
    else:
        serializer = ServiceSerializer(service)
        return JsonResponse(serializer.errors, status=400)
