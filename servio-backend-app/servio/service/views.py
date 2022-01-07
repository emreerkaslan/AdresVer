from django.http import HttpResponse, JsonResponse
from rest_framework import generics, status
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

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

@api_view(['POST'])
def serviceCreate(request):
    data = request.data
    print(data)
    try:
        title = data.get("title")
        description = data.get("description")
        credits = int(data.get("credits"))
        giver = int(data.get("giver"))
        date = data.get("date")
        print(date)
        geolocation = data.get("geolocation")
        print(geolocation)
        recurring = bool(data.get("recurring"))
        print(recurring)
        from servio.user.models import User
        queryset = User.objects
        giver = queryset.get(giver)
        print(giver)
    except:
        return Response(status=status.HTTP_400_BAD_REQUEST)
    service = Service(
            title=title,
            description=description,
            credits=credits,
            giver=giver,
            date=date,
            geolocation=geolocation,
            recurring=recurring
        )
    if 'picture' in request.data:
        service.picture = data.get('picture')
    if 'tags' in request.data:
        service.tags = data.get('tags')
    service.save()
    serializer = ServiceSerializer(service)
    return JsonResponse(serializer.data, status=201)

@api_view(['GET'])
def getService(request, pk):
    print(pk)
    if request.method == 'GET':
        services = Service.objects.filter(giver=pk)
        serializer = ServiceSerializer(services)
        return JsonResponse(serializer.data)
    else:
        return JsonResponse(TypeError, status=400)
