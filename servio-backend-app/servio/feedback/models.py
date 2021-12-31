from django.contrib import admin
from django.db import models


class Feedback(models.Model):

    rating = models.IntegerField(default=5)

    comment = models.CharField(verbose_name='comment', max_length=511)

    service = models.ForeignKey('service.Service', on_delete=models.PROTECT, related_name='service')

    giver = models.ForeignKey('user.User', on_delete=models.PROTECT, related_name='feedback_giver')

    taker = models.ForeignKey('user.User', on_delete=models.PROTECT, related_name='feedback_taker')

    isActive = models.BooleanField(default=True)

    def __str__(self):
        return self.comment

admin.site.register(Feedback)