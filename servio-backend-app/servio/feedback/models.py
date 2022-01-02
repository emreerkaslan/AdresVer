from django.contrib import admin
from django.db import models


class Feedback(models.Model):

    rating = models.PositiveIntegerField(default=5)

    comment = models.CharField(verbose_name='comment', max_length=511)

    service = models.ForeignKey('service.Service', on_delete=models.PROTECT, related_name='service')

    giver = models.ForeignKey('user.User', on_delete=models.PROTECT, related_name='feedback_giver')

    taker = models.ForeignKey('user.User', on_delete=models.PROTECT, related_name='feedback_taker', null=True, blank=True)

    isActive = models.BooleanField(default=True)

    REQUIRED_FIELDS = ['rating', 'comment', 'service', 'giver', 'taker']

    def __int__(self):
        return self.pk

    def deactivate(self):
        if self.isActive:
            self.isActive = False
        else:
            self.isActive = True


admin.site.register(Feedback)
