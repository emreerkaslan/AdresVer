package com.erkaslan.servio.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.erkaslan.servio.model.*
import org.json.JSONObject
import kotlin.collections.HashMap

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Repository

    private var _authTokenLiveData: MutableLiveData<GenericResult<Token>>? = MutableLiveData()

    val authTokenMutableLiveData: MutableLiveData<GenericResult<Token>>?
        get() = _authTokenLiveData

    private var _currentUserLiveData: MutableLiveData<GenericResult<User>>? = MutableLiveData()

    val currentUserMutableLiveData: MutableLiveData<GenericResult<User>>?
        get() = _currentUserLiveData

    private var _serviceListLiveData: MutableLiveData<GenericResult<List<Service>>>? = MutableLiveData()

    val serviceListMutableLiveData: MutableLiveData<GenericResult<List<Service>>>?
        get() = _serviceListLiveData

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

    fun checkCredits(token: String, user: Int){
        repository.checkCredits(token, user, object: AllServicesInterface{
            override fun onAllServicesTaken(serviceList: List<Service>) {
                serviceListMutableLiveData?.value = GenericResult.Success(serviceList)
            }
        })
    }

    fun addCredits(token: String, user: Int, credits: Int){
        repository.addCredits(token, user, credits , object: UserInterface {
            override fun onUserResponse(user: User) {
                currentUserMutableLiveData?.value = GenericResult.Success(user)
            }

            override fun onUserFailure(servioException: ServioException) {
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

    fun follow(token: String, follower: Int, followed: Int){
        repository.follow(token, follower, followed, object: UserInterface{
            override fun onUserResponse(user: User) {
                currentUserMutableLiveData?.value = GenericResult.Success(user)
            }

            override fun onUserFailure(servioException: ServioException) {
                currentUserMutableLiveData?.value = GenericResult.Failure(servioException)
            }
        })
    }

    fun unfollow(token: String, follower: Int, followed: Int){
        repository.follow(token, follower, followed, object: UserInterface{
            override fun onUserResponse(user: User) {
                currentUserMutableLiveData?.value = GenericResult.Success(user)
            }

            override fun onUserFailure(servioException: ServioException) {
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