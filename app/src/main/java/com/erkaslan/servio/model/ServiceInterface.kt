package com.erkaslan.servio.model


interface ServiceInterface {
    fun onSuccess(service: Service)
}

interface AllServicesInterface {
    fun onAllServicesTaken(listOfServices: List<Service>)
}

interface AllEventsInterface {
    fun onAllEventsTaken(listOfEvents: List<Event>)
}

interface LoginInterface {
    fun onLogin(token: Token)
    fun onFailure(servioException: ServioException)
}

interface LoginCheckInterface {
    fun onLoginCheck(user: User)
    fun onLoginFailure(servioException: ServioException)
}

interface SignupInterface {
    fun onSignup(user: User)
    fun onSignupFailure(servioException: ServioException)
}

interface UserListInterface {
    fun onUserListResponse(userList: List<User>)
    fun onUserListFailure(servioException: ServioException)
}

interface UserInterface {
    fun onUserResponse(user: User)
    fun onUserFailure(servioException: ServioException)
}

interface CreateServiceInterface {
    fun onCreateService(service: Service)
    fun onCreateServiceFailure(servioException: ServioException)
}

interface CreateEventInterface {
    fun onCreateEvent(event: Event)
    fun onCreateEventFailure(servioException: ServioException)
}