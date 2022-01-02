package com.erkaslan.servio.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erkaslan.servio.manager.RunOnceManager
import com.erkaslan.servio.model.*
import java.lang.Error
import java.util.*
import kotlin.collections.HashMap

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    private var _authTokenLiveData: MutableLiveData<Token>? = MutableLiveData()

    val authTokenMutableLiveData: MutableLiveData<Token>?
        get() = _authTokenLiveData

    private var _currentUserLiveData: MutableLiveData<User>? = MutableLiveData()

    val currentUserMutableLiveData: MutableLiveData<User>?
        get() = _currentUserLiveData

    fun login(map: HashMap<String, String>){
        repository.login(map, object: LoginInterface{
            override fun onLogin(token: Token) {
                authTokenMutableLiveData?.value = token
            }

            override fun onFailure(error: Error) {

            }
        })
    }

    fun signup(map: HashMap<String, String>){
        repository.signup(map, object: SignupInterface{
            override fun onSignup(user: User) {
                currentUserMutableLiveData?.value = user
            }

            override fun onSignupFailure(error: Error) {
            }
        })
    }

    fun logout(){
        currentUserMutableLiveData?.value = null
        authTokenMutableLiveData?.value = null
    }

    init {
        repository = Repository(application)
    }
}