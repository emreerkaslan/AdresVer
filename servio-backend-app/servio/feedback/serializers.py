from rest_framework import serializers
from .models import Feedback


class FeedbackSerializer(serializers.ModelSerializer):

    class Meta:
        model = Feedback
        fields = ['pk', 'rating', 'comment', 'service', 'giver', 'taker']

        def create(self, validated_data):
            feedback = Feedback.objects.create(
                rating=validated_data.get("rating"),
                comment=validated_data.get("comment"),
                service=validated_data.get("service"),
                giver=validated_data.get("giver"),
                taker=validated_data.get("taker"),
                **validated_data)
            return feedback

        def update(self, instance, validated_data):
            if instance.isActive:
                for (key, value) in validated_data.items():
                    setattr(instance, key, value)

                instance.save()
            return instance