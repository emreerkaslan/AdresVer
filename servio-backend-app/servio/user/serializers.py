from rest_framework import serializers
from .models import User


class UserSerializer(serializers.ModelSerializer):

    password = serializers.CharField(
        max_length=128,
        min_length=8,
        write_only=True,
        required=False
    )

    read_only = ['isActive']

    class Meta:
        model = User
        fields = ['pk', 'username', 'name', 'password', 'email', 'bio', 'geolocation', 'interest', 'competency',
                  'service', 'following', 'profilePic', 'feedbacks', 'badge', 'is_admin', 'credits',]
        read_only_fields = ('badge', 'credits', 'feedbacks')

        def create(self, validated_data):
            user = User.objects.create(
                username=validated_data.get("username"),
                name=validated_data.get("name"),
                email=validated_data.get("email"),
                bio=validated_data.get("bio"),
                credits=5,
                badge="Newcomer",
                geolocation=validated_data.get("geolocation"),
                interest=validated_data.get("interest"),
                competency=validated_data.get("competency"),
                service=None,
                following=None,
                profilePic='https://www.pngall.com/wp-content/uploads/5/Profile-PNG-Clipart.png',
                feedbacks=None,
                isActive=True,
                **validated_data)
            if validated_data.get("profilePic") is not None:
                user.profilePic = validated_data.get("profilePic")
            user.set_password(validated_data["password"])
            return user

        def __init__(self, *args, **kwargs):
            super().__init__(*args, **kwargs)
            if self.instance:
                self.fields.pop('password')

        def update(self, instance, validated_data):
            if instance.isActive:
                for (key, value) in validated_data.items():
                    setattr(instance, key, value)

                #if validated_data("password") is not None:
                  #  instance.set_password(validated_data("password"))

                instance.save()
            return instance
