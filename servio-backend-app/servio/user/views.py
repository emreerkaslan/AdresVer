from django.http import HttpResponse, JsonResponse
from rest_framework import generics, status
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response

from .models import User
from .serializers import UserSerializer


class UserCreate(generics.CreateAPIView):
    # API endpoint that allows creation of a new user
    # permission_classes = (IsAuthenticated,)
    queryset = User.objects.all(),
    serializer_class = UserSerializer


class UserList(generics.ListAPIView):
    # API endpoint that allows user to be viewed.
    # permission_classes = (IsAuthenticated,)
    queryset = User.objects.all()
    serializer_class = UserSerializer


class UserDetail(generics.RetrieveAPIView):
    # API endpoint that returns a single user by pk.
    #permission_classes = (IsAuthenticated,)
    queryset = User.objects.all()
    serializer_class = UserSerializer


class UserUpdate(generics.RetrieveUpdateAPIView):
    # API endpoint that allows a user record to be updated.
    queryset = User.objects.all()
    serializer_class = UserSerializer


class UserDelete(generics.RetrieveDestroyAPIView):
    # API endpoint that allows a user record to be deleted.
    queryset = User.objects.all()
    serializer_class = UserSerializer


@api_view(['GET'])
@permission_classes([IsAuthenticated])
def loginCheck(request, username):
    try:
        user = User.objects.get(username=username)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'GET':
        serializer = UserSerializer(user)
        return JsonResponse(serializer.data)
    else:
        serializer = UserSerializer(user)
        return JsonResponse(serializer.errors, status=400)


@api_view(['POST'])
def login(request):
    data = request.data
    try:
        username = data['username']
        password = data['password']
    except:
        return Response(status=status.HTTP_400_BAD_REQUEST)

    try:
        user = User.objects.get(username=username, password=password)
    except:
        return Response(status=status.HTTP_401_UNAUTHORIZED)

    try:
        user_token = user.auth_token.key
    except:
        user_token = Token.objects.create(user=user)

    data = {'token': user_token}
    return Response(data=data, status=status.HTTP_200_OK)


@api_view(['GET'])
@permission_classes([IsAuthenticated])
def getFeedback(request, pk):
    try:
        user = User.objects.get(pk=pk)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'GET':
        #feedbacks = user.feedbacks
        from servio.feedback.serializers import FeedbackSerializer
        from servio.feedback.models import Feedback
        feedbacks = Feedback.objects.get(taker=pk)
        serializer = FeedbackSerializer(feedbacks)
        return JsonResponse(serializer.data)
    else:
        serializer = UserSerializer(user)
        return JsonResponse(serializer.errors, status=400)


@api_view(['GET'])
@permission_classes([IsAuthenticated])
def getService(request, pk):
    try:
        user = User.objects.get(pk=pk)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'GET':
        service = user.service
        from servio.service.serializers import ServiceSerializer
        serializer = ServiceSerializer(service)
        return JsonResponse(serializer.data)
    else:
        serializer = UserSerializer(user)
        return JsonResponse(serializer.errors, status=400)


@api_view(['PUT'])
@permission_classes([IsAuthenticated])
def follow(request, follower, followed):
    try:
        follower = User.objects.get(pk=follower)
        followed = User.objects.get(pk=followed)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'PUT':
        following = follower.following
        following.add(followed)
        follower.following = following
        follower.save()
        serializer = UserSerializer(follower)
        return JsonResponse(serializer.data)
    else:
        serializer = UserSerializer(follower)
        return JsonResponse(serializer.errors, status=400)


@api_view(['PUT'])
@permission_classes([IsAuthenticated])
def unfollow(request, follower, followed):
    try:
        follower = User.objects.get(pk=follower)
        followed = User.objects.get(pk=followed)
    except User.DoesNotExist:
        return HttpResponse(status=404)
    if request.method == 'PUT':
        following = follower.following
        following.remove(followed)
        follower.following = following
        follower.save()
        serializer = UserSerializer(follower)
        return JsonResponse(serializer.data)
    else:
        serializer = UserSerializer(follower)
        return JsonResponse(serializer.errors, status=400)

@api_view(['POST'])
def userCreate(request):
    data = request.data["nameValuePairs"]
    print(request.data)
    try:
        username = data.get("username")
        name = data.get("name")
        geolocation = data.get("geolocation")
        interest = data.get("interest")
        competency = data.get("competency")
        email = data.get("email")
        password = data.get("password")
        bio = data.get("bio")
    except:
        return Response(status=status.HTTP_400_BAD_REQUEST)
    user = User(
            username=username,
            email=email,
            name=name,
            bio=bio,
            password=password,
            geolocation=geolocation,
            interest=interest,
            competency=competency,
        )
    if 'picture' in request.data:
        user.picture = data.get('picture')
    user.save()
    serializer = UserSerializer(user)
    return JsonResponse(serializer.data, status=201)
