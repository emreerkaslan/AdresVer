package com.erkaslan.servio.model

interface ServiceInterface {
    fun onSuccess(service: Service)
}

interface AllServicesInterface {
    fun onAllServicesTaken(listOfServices: List<Service>)
}