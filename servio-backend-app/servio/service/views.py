from rest_framework import generics
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