from django.contrib import admin
from django.db import models
from django.contrib.postgres.fields import ArrayField


class Service(models.Model):
    title = models.CharField(verbose_name='title', max_length=255,)

    description = models.CharField(verbose_name='description', max_length=1023,)

    giver = models.ForeignKey('user.User', on_delete=models.PROTECT, related_name='giver')

    taker = models.ForeignKey('user.User', on_delete=models.PROTECT, related_name='taker', null=True, blank=True)

    feedbackGiven = models.BooleanField(default=False)

    date = models.DateTimeField()

    recurring = models.BooleanField(default=False)

    credits = models.IntegerField()

    geolocation = models.CharField(verbose_name='geolocation', max_length=255,)

    isActive = models.BooleanField(verbose_name='active', default=True)

    requests = models.ManyToManyField('user.User', related_name="requests", blank=True)

    picture = models.CharField(verbose_name='picture', max_length=511, default='https://bcw.org.au/wp-content/uploads/2020/02/BCWS-rebrand-37-2048x668.png')

    tags = ArrayField(models.CharField(verbose_name='tag', max_length=255), null=True, blank=True)

    REQUIRED_FIELDS = ['title', 'description', 'giver', 'date', 'geolocation', 'credits']

    def __int__(self):
        return self.pk

    def deactivate(self):
        if self.isActive:
            self.isActive = False
        else:
            self.isActive = True


admin.site.register(Service)
