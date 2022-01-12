from rest_framework import serializers
from .models import Event


class EventSerializer(serializers.ModelSerializer):

    class Meta:
        model = Event
        fields = ['pk', 'title', 'organizer', 'description', 'date', 'geolocation', 'address', 'hasQuota', 'quota', 'attendees', 'picture',]

        def create(self, validated_data):
            event = Event.objects.create(
                title=validated_data.get("title"),
                organizer=validated_data.get("organizer"),
                description=validated_data.get("description"),
                date=validated_data.get("date"),
                geolocation=validated_data.get("geolocation"),
                address=validated_data.get("address"),
                hasQuota=validated_data.get("hasQuota"),
                quota=validated_data.get("quota"),
                **validated_data)
            if validated_data.get("picture") is not None:
                event.picture = validated_data.get("picture")
            return event

        def update(self, instance, validated_data):
            if instance.isActive and instance.attendees.isEmpty:
                for (key, value) in validated_data.items():
                    setattr(instance, key, value)

                instance.save()
            return instance