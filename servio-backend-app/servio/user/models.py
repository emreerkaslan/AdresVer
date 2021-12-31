from django.conf import settings
from django.contrib.auth.models import AbstractBaseUser, BaseUserManager, PermissionsMixin
from django.db import models
from django.db.models.signals import post_save
from django.dispatch import receiver
from rest_framework.authtoken.models import Token
from django.contrib import admin


class UserManager(BaseUserManager):
    def create_user(self, username, email, name, bio, geolocation, interest, competency, password=None):
        """
        Creates and saves a User
        """
        if password is None:
            raise TypeError('Superusers must have a password.')

        if not password:
            raise TypeError('Users must have a password.')

        if not email:
            raise ValueError('Users must have an email address')

        if not name:
            raise ValueError('Users must have a name')

        if not bio:
            raise ValueError('Users must have a bio')

        if not geolocation:
            raise ValueError('Users must have a geolocation')

        if not interest:
            raise ValueError('Users must have a interest')

        if not competency:
            raise ValueError('Users must have a competency')

        user = self.model(
            username=username,
            name=name,
            bio=bio,
            email=self.normalize_email(email),
            password=password,
            geolocation=geolocation,
            interest=interest,
            competency=competency,
        )

        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, username, email, name, bio, geolocation, interest, competency, password):
        """
        Creates and saves a superuser (admin)
        """

        if password is None:
            raise TypeError('Superusers must have a password.')

        user = self.create_user(
            username=username,
            email=email,
            name=name,
            bio=bio,
            password=password,
            geolocation=geolocation,
            interest=interest,
            competency=competency,
        )
        user.is_admin = True
        user.save(using=self._db)
        return user


class User(AbstractBaseUser, PermissionsMixin):
    username = models.CharField(
        db_index=True,
        verbose_name='username',
        max_length=255,
        unique=True,
    )

    email = models.EmailField(
        verbose_name='email address',
        max_length=255,
        unique=True,
    )

    name = models.CharField(
        verbose_name='name',
        max_length=255,
    )

    bio = models.CharField(
        verbose_name='bio',
        max_length=255,
    )

    geolocation = models.CharField(
        verbose_name='geolocation',
        max_length=255,
    )

    interest = models.CharField(
        verbose_name='interest',
        max_length=255,
    )

    competency = models.CharField(
        verbose_name='competency',
        max_length=255,
    )

    badge = models.CharField(
        verbose_name='badge',
        default='Fasulye',
        max_length=255,
    )

    credits = models.IntegerField(
        verbose_name='credits',
        default=5,
    )

    profilePic = models.CharField(
        verbose_name='profile picture',
        max_length=255
    )

    isActive = models.BooleanField(default=True)
    is_admin = models.BooleanField(default=False)
    #isSuperuser = models.BooleanField(default=False)

    USERNAME_FIELD = 'username'
    REQUIRED_FIELDS = ['email', 'name', 'bio', 'geolocation', 'interest', 'competency']

    objects = UserManager()

    def __str__(self):
        return self.username

    def has_perm(self, perm, obj=None):
        "Does the user have a specific permission?"
        # Simplest possible answer: Yes, always
        return True

    def has_module_perms(self, app_label):
        "Does the user have permissions to view the app `app_label`?"
        # Simplest possible answer: Yes, always
        return True

    @property
    def is_staff(self):
        "Is the user a member of staff?"
        # Simplest possible answer: All admins are staff
        return self.is_admin

    def deactivate(self):
        if self.is_active:
            self.is_active = False
        else:
            self.is_active = True

    @receiver(post_save, sender=settings.AUTH_USER_MODEL)
    def create_auth_token(sender, instance=None, created=False, **kwargs):
        if created:
            Token.objects.create(user=instance)


admin.site.register(User)
