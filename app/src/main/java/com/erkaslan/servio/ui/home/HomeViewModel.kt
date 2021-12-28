package com.erkaslan.servio.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erkaslan.servio.model.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private var _userListMutableLiveData: MutableLiveData<ArrayList<User>?> = MutableLiveData()

    val userListMutableLiveData: MutableLiveData<ArrayList<User>?>
        get() = _userListMutableLiveData


    private var _serviceMutableLiveData: MutableLiveData<Service>? = MutableLiveData()

    val serviceMutableLiveData: MutableLiveData<Service>?
        get() = _serviceMutableLiveData

    private var _allServicesMutableLiveData: MutableLiveData<List<Service>?> = MutableLiveData()

    val allServicesMutableLiveData: MutableLiveData<List<Service>?>
        get() = _allServicesMutableLiveData

    fun getService(id: Int){
        repository.getService(id, object : ServiceInterface {
            override fun onSuccess(service: Service) {
                serviceMutableLiveData?.value = service
            }
        })
    }

    fun getAllServices(){
        repository.getAllServices(object : AllServicesInterface {
            override fun onAllServicesTaken(listOfServices: List<Service>) {
                allServicesMutableLiveData?.value = listOfServices
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