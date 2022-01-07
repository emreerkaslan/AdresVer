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


@api_view(['POST'])
def serviceCreate(request):
    data = request.data
    print(data)
    try:
        title = data.get("title")
        description = data.get("description")
        credits = int(data.get("credits"))
        print(credits)
        giver = data.get("giver")
        #print(giver)
        date = data.get("date")
        print(date)
        geolocation = data.get("geolocation")
        print(geolocation)
        recurring = bool(data.get("recurring"))
        print(recurring)
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
        serializer = ServiceSerializer(services, many=True)
        print(serializer)
        return Response(serializer.data)
        #return JsonResponse(serializer.data, status=200)
    else:
        return JsonResponse(TypeError, status=400)

@api_view(['POST'])
def acceptRequest(request, service, requestmaker):
    try:
        service = Service.objects.get(pk=service)
    except Service.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'POST':
        service.requests.remove(requestmaker)
        service.taker.add(requestmaker)
        serializer = ServiceSerializer(service)
        return Response(serializer.data)
    else:
        serializer = ServiceSerializer(service)
        return JsonResponse(serializer.errors, status=400)


@api_view(['POST'])
def declineRequest(request, service, requestmaker):
    try:
        service = Service.objects.get(pk=service)
    except Service.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'POST':
        service.requests.remove(requestmaker)
        serializer = ServiceSerializer(service)
        return Response(serializer.data)
    else:
        serializer = ServiceSerializer(service)
        return JsonResponse(serializer.errors, status=400)


@api_view(['POST'])
def addRequest(request, service, requestmaker):
    try:
        service = Service.objects.get(pk=service)
    except Service.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'POST':
        service.requests.add(requestmaker)
        serializer = ServiceSerializer(service)
        return Response(serializer.data)
    else:
        serializer = ServiceSerializer(service)
        return JsonResponse(serializer.errors, status=400)

@api_view(['POST'])
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
