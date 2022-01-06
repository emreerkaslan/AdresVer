package com.erkaslan.servio.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erkaslan.servio.manager.RunOnceManager
import com.erkaslan.servio.model.*
import org.json.JSONObject
import java.lang.Error
import java.util.*
import kotlin.collections.HashMap

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    private var _authTokenLiveData: MutableLiveData<GenericResult<Token>>? = MutableLiveData()

    val authTokenMutableLiveData: MutableLiveData<GenericResult<Token>>?
        get() = _authTokenLiveData

    private var _currentUserLiveData: MutableLiveData<GenericResult<User>>? = MutableLiveData()

    val currentUserMutableLiveData: MutableLiveData<GenericResult<User>>?
        get() = _currentUserLiveData

    fun login(map: HashMap<String, String>){
        repository.login(map, object: LoginInterface{
            override fun onLogin(token: Token) {
                authTokenMutableLiveData?.value = GenericResult.Success(token)
            }

            override fun onFailure(servioException: ServioException) {
                authTokenMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun loginCheck(token: String, username: String){
        repository.loginCheck(token, username, object: LoginCheckInterface{
            override fun onLoginCheck(user: User) {
                currentUserMutableLiveData?.value = GenericResult.Success(user)
            }

            override fun onLoginFailure(servioException: ServioException) {
                currentUserMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun signup(map: JSONObject){
        repository.signup(map, object: SignupInterface{
            override fun onSignup(user: User) {
                currentUserMutableLiveData?.value = GenericResult.Success(user)
            }

            override fun onSignupFailure(servioException: ServioException) {
                currentUserMutableLiveData?.value = GenericResult.Failure(servioException)
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