from rest_framework import serializers
from .models import Feedback


class FeedbackSerializer(serializers.ModelSerializer):

    rating = serializers.IntegerField(max_value=5, min_value=1)

    class Meta:
        model = Feedback
        fields = ['pk', 'rating', 'comment', 'service', 'giver', 'taker']

        def create(self, validated_data):
            from servio.user.models import User
            feedback = Feedback.objects.create(
                rating=validated_data.get("rating"),
                comment=validated_data.get("comment"),
                service=validated_data.get("service"),
                giver=validated_data.get("giver"),
                taker=User.objects.get(pk=validated_data.get("service")),
                **validated_data)
            feedback.taker = feedback.service.giver
            return feedback

        def update(self, instance, validated_data):
            if instance.isActive:
                for (key, value) in validated_data.items():
                    setattr(instance, key, value)
                instance.taker = instance.service.giver
                instance.save()
            return instance
