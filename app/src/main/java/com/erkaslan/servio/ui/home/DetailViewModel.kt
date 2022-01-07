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

    companion object {
        private const val TAG = "HomeVM"
    }

    init {
        repository = Repository(application)
    }
}