package com.erkaslan.servio.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erkaslan.servio.model.*

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    private var _searchedServicesLiveData: MutableLiveData<GenericResult<List<Service>>>? = MutableLiveData()

    val searchedServicesMutableLiveData: MutableLiveData<GenericResult<List<Service>>>?
        get() = _searchedServicesLiveData

    private var _searchedUsersLiveData: MutableLiveData<GenericResult<List<User>>>? = MutableLiveData()

    val searchedUsersMutableLiveData: MutableLiveData<GenericResult<List<User>>>?
        get() = _searchedUsersLiveData

    private var _searchedEventsLiveData: MutableLiveData<GenericResult<List<Event>>>? = MutableLiveData()

    val searchedEventsMutableLiveData: MutableLiveData<GenericResult<List<Event>>>?
        get() = _searchedEventsLiveData

    fun searchService(keyword: String){
        repository.searchService(keyword, object: AllServicesInterface {
            override fun onAllServicesTaken(serviceList: List<Service>) {
                searchedServicesMutableLiveData?.value = GenericResult.Success(serviceList)
            }
        })
    }

    fun searchUser(keyword: String){
        repository.searchUser(keyword, object: UserListInterface {
            override fun onUserListResponse(userList: List<User>) {
                searchedUsersMutableLiveData?.value = GenericResult.Success(userList)
            }
            override fun onUserListFailure(servioException: ServioException) {
                searchedUsersMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun searchEvent(keyword: String){
        repository.searchEvent(keyword, object: AllEventsInterface {
            override fun onAllEventsTaken(eventList: List<Event>) {
                searchedEventsMutableLiveData?.value = GenericResult.Success(eventList)
            }
        })
    }

    init {
        repository = Repository(application)
    }

}