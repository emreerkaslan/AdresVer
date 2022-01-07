package com.erkaslan.servio.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.erkaslan.servio.model.*
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

    fun getRequesters(data: JSONObject){
        repository.getUsers(data , object: UserListInterface {
            override fun onUserListResponse(userList: List<User>) {
                requestersMutableLiveData?.value = GenericResult.Success(userList)
            }

            override fun onUserListFailure(servioException: ServioException) {
                requestersMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun getUserServices(id: Int) {
        repository.getUserServices(id, object: AllServicesInterface {
            override fun onAllServicesTaken(listOfServices: List<Service>) {
                userServicesMutableLiveData?.value = GenericResult.Success(listOfServices)
            }

        })
    }

    fun acceptRequest(service:Int, user:Int) {
        repository.acceptRequest(service, user, object: CreateServiceInterface{
            override fun onCreateService(service: Service) {
                serviceMutableLiveData?.value = GenericResult.Success(service)
                GenericResult.Success(service).data.requests
            }

            override fun onCreateServiceFailure(servioException: ServioException) {
                serviceMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun declineRequest(service:Int, user:Int) {
        repository.declineRequest(service, user, object: CreateServiceInterface{
            override fun onCreateService(service: Service) {
                serviceMutableLiveData?.value = GenericResult.Success(service)
                GenericResult.Success(service).data.requests
            }

            override fun onCreateServiceFailure(servioException: ServioException) {
                serviceMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun addRequest(service:Int, user:Int) {
        repository.addRequest(service, user, object: CreateServiceInterface{
            override fun onCreateService(service: Service) {
                requestedServiceMutableLiveData?.value = GenericResult.Success(service)
                GenericResult.Success(service).data.requests
            }

            override fun onCreateServiceFailure(servioException: ServioException) {
                requestedServiceMutableLiveData?.value = GenericResult.Failure(servioException)
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