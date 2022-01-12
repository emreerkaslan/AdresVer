# Generated by Django 4.0 on 2021-12-31 12:13

from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Service',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.CharField(max_length=255, verbose_name='title')),
                ('description', models.CharField(max_length=1023, verbose_name='description')),
                ('date', models.DateTimeField()),
                ('recurring', models.BooleanField(default=False)),
                ('credits', models.IntegerField()),
                ('geolocation', models.CharField(max_length=255, verbose_name='geolocation')),
                ('isActive', models.BooleanField(default=True)),
            ],
        ),
    ]