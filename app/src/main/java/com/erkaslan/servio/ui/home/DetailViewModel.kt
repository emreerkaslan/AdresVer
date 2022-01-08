package com.erkaslan.servio.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.erkaslan.servio.model.*
import com.google.gson.JsonObject
import org.json.JSONObject

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    private var _providerMutableLiveData: MutableLiveData<GenericResult<User>>? = MutableLiveData()

    val providerMutableLiveData: MutableLiveData<GenericResult<User>>?
        get() = _providerMutableLiveData

    private var _requestersMutableLiveData: MutableLiveData<GenericResult<List<User>>>? = MutableLiveData()

    val requestersMutableLiveData: MutableLiveData<GenericResult<List<User>>>?
        get() = _requestersMutableLiveData

    private var _userServicesMutableLiveData: MutableLiveData<GenericResult<List<Service>>>? = MutableLiveData()

    val userServicesMutableLiveData: MutableLiveData<GenericResult<List<Service>>>?
        get() = _userServicesMutableLiveData

    private var _serviceMutableLiveData: MutableLiveData<GenericResult<Service>>? = MutableLiveData()

    val serviceMutableLiveData: MutableLiveData<GenericResult<Service>>?
        get() = _serviceMutableLiveData

    private var _requestedServiceMutableLiveData: MutableLiveData<GenericResult<Service>>? = MutableLiveData()

    val requestedServiceMutableLiveData: MutableLiveData<GenericResult<Service>>?
        get() = _requestedServiceMutableLiveData

    private var _feedbackListMutableLiveData: MutableLiveData<GenericResult<List<Feedback>>>? = MutableLiveData()

    val feedbackListMutableLiveData: MutableLiveData<GenericResult<List<Feedback>>>?
        get() = _feedbackListMutableLiveData

    private var _feedbackMutableLiveData: MutableLiveData<GenericResult<Feedback>>? = MutableLiveData()

    val feedbackMutableLiveData: MutableLiveData<GenericResult<Feedback>>?
        get() = _feedbackMutableLiveData

    private var _eventMutableLiveData: MutableLiveData<GenericResult<Event>>? = MutableLiveData()

    val eventMutableLiveData: MutableLiveData<GenericResult<Event>>?
        get() = _eventMutableLiveData

    fun getProvider(pk: Int){
        repository.getUser(pk , object: UserInterface {
            override fun onUserResponse(user: User) {
                providerMutableLiveData?.value = GenericResult.Success(user)
            }

            override fun onUserFailure(servioException: ServioException) {
                providerMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun getRequesters(token: String, data: JSONObject){
        repository.getUsers(token, data , object: UserListInterface {
            override fun onUserListResponse(userList: List<User>) {
                requestersMutableLiveData?.value = GenericResult.Success(userList)
            }

            override fun onUserListFailure(servioException: ServioException) {
                requestersMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun getUserServices(token: String,id: Int) {
        repository.getUserServices(token, id, object: AllServicesInterface {
            override fun onAllServicesTaken(listOfServices: List<Service>) {
                userServicesMutableLiveData?.value = GenericResult.Success(listOfServices)
            }

        })
    }

    fun acceptRequest(token: String, service:Int, user:Int) {
        repository.acceptRequest(token, service, user, object: CreateServiceInterface{
            override fun onCreateService(service: Service) {
                serviceMutableLiveData?.value = GenericResult.Success(service)
                GenericResult.Success(service).data.requests
            }

            override fun onCreateServiceFailure(servioException: ServioException) {
                serviceMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun declineRequest(token: String, service:Int, user:Int) {
        repository.declineRequest(token, service, user, object: CreateServiceInterface{
            override fun onCreateService(service: Service) {
                serviceMutableLiveData?.value = GenericResult.Success(service)
                GenericResult.Success(service).data.requests
            }

            override fun onCreateServiceFailure(servioException: ServioException) {
                serviceMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun addRequest(token: String, service:Int, user:Int) {
        repository.addRequest(token, service, user, object: CreateServiceInterface{
            override fun onCreateService(service: Service) {
                requestedServiceMutableLiveData?.value = GenericResult.Success(service)
                GenericResult.Success(service).data.requests
            }

            override fun onCreateServiceFailure(servioException: ServioException) {
                requestedServiceMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun addServiceFeedback(token: String, service:Int, feedback:Int) {
        repository.addServiceFeedback(token, service, feedback, object: CreateServiceInterface{
            override fun onCreateService(service: Service) {
            }

            override fun onCreateServiceFailure(servioException: ServioException) {
            }
        })
    }

    fun getFeedbacks(data: JSONObject) {
        repository.getFeedbacks(data, object: FeedbackInterface{
            override fun onFeedbackSet(feedbackList: List<Feedback>) {
                feedbackListMutableLiveData?.value = GenericResult.Success(feedbackList)
            }

            override fun onFeedbackFailure(servioException: ServioException) {
                feedbackListMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun addFeedback(token: String, data: JsonObject) {
        repository.addFeedback(token, data, object: FeedbackAddInterface{
            override fun onFeedbackAdded(feedback: Feedback) {
                feedbackMutableLiveData?.value = GenericResult.Success(feedback)
            }

            override fun onFeedbackFailure(servioException: ServioException) {
                feedbackMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun attend(token: String, event: Int, user: Int) {
        repository.attend(token, event, user, object: EventUpdateInterface{
            override fun onEventUpdated(event: Event) {
                eventMutableLiveData?.value = GenericResult.Success(event)
            }

            override fun onEventFailure(servioException: ServioException) {
                eventMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun addCredits(token: String, user: Int, credits: Int){
        repository.addCredits(token, user, credits , object: UserInterface {
            override fun onUserResponse(user: User) {
            }

            override fun onUserFailure(servioException: ServioException) {
            }
        })
    }


    companion object {
        private const val TAG = "HomeVM"
    }

    init {
        repository = Repository(application)
    }
}