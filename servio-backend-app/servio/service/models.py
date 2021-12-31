from django.contrib import admin
from django.db import models


class Service(models.Model):
    title = models.CharField(
        verbose_name='title',
        max_length=255,
    )

    description = models.CharField(
        verbose_name='description',
        max_length=1023,
    )

    giver = models.ForeignKey(
        'user.User', on_delete=models.PROTECT, related_name='giver'
    )

    taker = models.ForeignKey(
        'user.User', on_delete=models.PROTECT, related_name='taker', null=True, blank=True
    )

    date = models.DateTimeField()

    recurring = models.BooleanField(default=False)

    credits = models.IntegerField()

    geolocation = models.CharField(
        verbose_name='geolocation',
        max_length=255,
    )

    isActive = models.BooleanField(default=True)

    requests = models.ManyToManyField('user.User', related_name="requests")

    def __str__(self):
        return self.title

admin.site.register(Service)