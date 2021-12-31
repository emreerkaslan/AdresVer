from rest_framework import serializers
from .models import User


class UserSerializer(serializers.ModelSerializer):

    password = serializers.CharField(
        max_length=128,
        min_length=8,
        write_only=True
    )

    class Meta:
        model = User
        fields = ['pk', 'username', 'name', 'password', 'email', 'bio', 'geolocation', 'interest', 'competency']

        #read_only_fields = ('token',)  also include , 'token, in fields

        def create(self, validated_data):
            user = User.objects.create(
                username=validated_data.get("username"),
                name=validated_data.get("name"),
                email=validated_data.get("email"),
                bio=validated_data.get("bio"),
                credits=5,
                badge="Fasulye",
                geolocation=validated_data.get("geolocation"),
                interest=validated_data.get("interest"),
                competency=validated_data.get("competency"),
                **validated_data)
            return user

        def update(self, instance, validated_data):
            if instance.isActive:
                for (key, value) in validated_data.items():
                    setattr(instance, key, value)

                #if password is not None:
                #    instance.set_password(password)

                instance.save()
            return instance