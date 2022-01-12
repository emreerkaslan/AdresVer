package com.erkaslan.servio.ui.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.erkaslan.servio.model.*
import com.google.gson.JsonObject
import org.json.JSONObject
import java.lang.Error
import java.nio.channels.SeekableByteChannel

class AddViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    private var _serviceCreatedLiveData: MutableLiveData<GenericResult<Service?>> = MutableLiveData()

    val serviceCreatedMutableLiveData: MutableLiveData<GenericResult<Service?>>
        get() = _serviceCreatedLiveData

    private var _eventCreatedLiveData: MutableLiveData<GenericResult<Event?>> = MutableLiveData()

    val eventCreatedMutableLiveData: MutableLiveData<GenericResult<Event?>>
        get() = _eventCreatedLiveData


    fun createService(token: String, service: JsonObject){
        repository.createService(token, service, object: CreateServiceInterface {
            override fun onCreateService(service: Service) {
                serviceCreatedMutableLiveData.value = GenericResult.Success(service)
            }

            override fun onCreateServiceFailure(servioException: ServioException) {
                serviceCreatedMutableLiveData.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun createEvent(token: String, event: JsonObject){
        repository.createEvent(token, event, object: CreateEventInterface {
            override fun onCreateEvent(event: Event) {
                eventCreatedMutableLiveData.value = GenericResult.Success(event)
            }

            override fun onCreateEventFailure(servioException: ServioException) {
                eventCreatedMutableLiveData.value = GenericResult.Failure(servioException)
            }
        })
    }
    
    init {
        repository = Repository(application)
    }
}