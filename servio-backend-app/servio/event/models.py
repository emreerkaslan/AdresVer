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

    picture = models.CharField(verbose_name='picture', max_length=511, default='https://media-exp1.licdn.com/dms/image/C511BAQFDjtrS0u76Tw/company-background_10000/0/1585488022941?e=1641214800&v=beta&t=7-RygNRYRMFQVUc3_WeXIQNWwX-u46UAEoR-ooMWqEY')

    isActive = models.BooleanField(default=True)

    hasQuota = models.BooleanField(default=False)

    quota = models.IntegerField(null=True, blank=True)

    REQUIRED_FIELDS = ['title', 'organizer', 'date', 'geolocation', 'address', 'description',]

    def __int__(self):
        return self.pk

    def deactivate(self):
        if self.isActive:
            self.isActive = False
        else:
            self.isActive = True


admin.site.register(Event)
