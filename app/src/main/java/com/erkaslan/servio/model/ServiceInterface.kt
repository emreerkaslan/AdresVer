package com.erkaslan.servio.model

import java.lang.Error

interface ServiceInterface {
    fun onSuccess(service: Service)
}

interface AllServicesInterface {
    fun onAllServicesTaken(listOfServices: List<Service>)
}

interface LoginInterface {
    fun onLogin(token: Token)
    fun onFailure(error: Error)
}

interface SignupInterface {
    fun onSignup(user: User)
    fun onSignupFailure(error: Error)
}

interface CreateServiceInterface {
    fun onCreateService(service: Service)
    fun onCreateServiceFailure(servioException: ServioException)
}

interface CreateEventInterface {
    fun onCreateEvent(event: Event)
    fun onCreateEventFailure(servioException: ServioException)
}