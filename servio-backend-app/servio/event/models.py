from django.contrib import admin
from django.db import models


class Event(models.Model):
    title = models.CharField(verbose_name='title', max_length=255,)

    organizer = models.ForeignKey('user.User', on_delete=models.PROTECT, related_name='organizer')

    attendees = models.ManyToManyField('user.User', related_name='attendee', blank=True)

    date = models.DateTimeField()

    geolocation = models.CharField(verbose_name='geolocation', max_length=255,)

    address = models.CharField(verbose_name='address', max_length=255,)

    description = models.CharField(verbose_name='description', max_length=1023,)

    isActive = models.BooleanField(default=True)

    hasQuota = models.BooleanField(default=True)

    quota = models.IntegerField(null=True, blank=True)

    def __str__(self):
        return self.title

admin.site.register(Event)