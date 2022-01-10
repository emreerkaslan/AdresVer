package com.erkaslan.servio.model

import android.app.Application
import android.util.Log
import com.erkaslan.servio.model.RetrofitClient.getClient
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.HashMap

class Repository(private val application: Application) : ServiceInterface, AllServicesInterface, LoginInterface {
    lateinit var userService: UserService

    override fun onSuccess(service: Service) {}
    override fun onAllServicesTaken(listOfServices: List<Service>) {}
    override fun onLogin(token: Token) {}
    override fun onFailure(servioException: ServioException) {}

    //Gets a all services
    fun getAllServices(serviceInterface: AllServicesInterface){
        userService = getClient().create(UserService::class.java)
        var service = userService.getAllServices()

        service.enqueue(object : Callback<List<Service>> {
            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                Log.d("REPO", response.body().toString())
                if (response.isSuccessful) {
                    serviceInterface.onAllServicesTaken(response.body() as List<Service>)
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
            }
        })
    }

    //Gets services belonging to a user pk
    fun getUserServices(token: String, id: Int, serviceInterface: AllServicesInterface){
        userService = getClient().create(UserService::class.java)
        var service = userService.getServices("Token " + token, id)

        service.enqueue(object : Callback<List<Service>> {
            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                Log.d("SERV", response.body().toString())
                if (response.isSuccessful) {
                    serviceInterface.onAllServicesTaken(response.body() as List<Service>)
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
            }
        })
    }

    //Gets a all events
    fun getAllEvents(serviceInterface: AllEventsInterface){
        userService = getClient().create(UserService::class.java)
        var event = userService.getAllEvents()

        event.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                Log.d("REPO", response.body().toString())
                if (response.isSuccessful) {
                    serviceInterface.onAllEventsTaken(response.body() as List<Event>)
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
            }
        })
    }

    //Gets events belonging to a user pk
    fun getUserEvents(token: String, id: Int, eventsInterface: AllEventsInterface){
        userService = getClient().create(UserService::class.java)
        var service = userService.getEvents("Token " + token, id)

        service.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                Log.d("SERV", response.body().toString())
                if (response.isSuccessful) {
                    eventsInterface.onAllEventsTaken(response.body() as List<Event>)
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
            }
        })
    }

    //Gets a single service with id
    fun getService(id: Int , serviceInterface: ServiceInterface){
        userService = getClient().create(UserService::class.java)
        var service = userService.getService(id)

        service.enqueue(object : Callback<Service> {
            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                Log.d("EX", response.body().toString())
                if (response.isSuccessful) {
                    serviceInterface.onSuccess(response.body() as Service)
                } else if (response.code() == 400) {
                    Log.v("VER",response.errorBody().toString());
                }
                else{
                }
            }

            override fun onFailure(call: Call<Service>, t: Throwable) {
            }
        })
    }

    //Logs user in
    fun login(map: HashMap<String, String>, loginInterface: LoginInterface){
        var service = getClient().create(UserService::class.java).userLogin(map)
        service.enqueue(object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                if (response.isSuccessful) {
                    loginInterface.onLogin(response.body() as Token)
                } else {
                    loginInterface.onFailure(ServioException("Failed Login"))
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                loginInterface.onFailure(ServioException("Failed Login"))
            }
        })
    }


    //Gets user info
    fun loginCheck(token: String, username: String, loginCheckInterface: LoginCheckInterface){
        var service = getClient().create(UserService::class.java).loginCheck("Token " + token, username)
        service.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    loginCheckInterface.onLoginCheck(response.body() as User)
                } else if (response.code() == 400) {
                    loginCheckInterface.onLoginFailure(ServioException("Failed Login"))
                }
                else if (response.code() == 301){
                    loginCheckInterface.onLoginFailure(ServioException("Failed Login"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                loginCheckInterface.onLoginFailure(ServioException("Failed Login"))
            }
        })
    }

    //Gets a single user by pk
    fun getUser(id: Int, userInterface: UserInterface){
        userService = getClient().create(UserService::class.java)
        var users = userService.getUser(id)

        users.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    userInterface.onUserResponse(response.body() as User)
                } else if (response.code() == 400) {
                    userInterface.onUserFailure(ServioException("User fetch failed"))
                }
                else{
                    userInterface.onUserFailure(ServioException("User fetch failed"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                userInterface.onUserFailure(ServioException("User fetch failed"))
            }
        })
    }

    //Gets a set of users by pk list
    fun getUsers(token: String, data: JSONObject, userListInterface: UserListInterface){
        userService = getClient().create(UserService::class.java)
        var users = userService.getUsers("Token " + token, data)

        users.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userListInterface.onUserListResponse(response.body() as List<User>)
                } else if (response.code() == 400) {
                    userListInterface.onUserListFailure(ServioException("User list fetch failed"))
                }
                else{
                    userListInterface.onUserListFailure(ServioException("User list fetch failed"))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                userListInterface.onUserListFailure(ServioException("User list fetch failed"))
            }
        })
    }

    //Signs user up
    fun signup(map: JSONObject, signupInterface: SignupInterface){
        var service = getClient().create(UserService::class.java).signup(map)
        Log.d("EX", "YYY: " + map)
        service.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    signupInterface.onSignup(response.body() as User)
                } else {
                    signupInterface.onSignupFailure(ServioException("Failed Signup"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                signupInterface.onSignupFailure(ServioException("Failed Signup"))
            }
        })
    }

    //Search user
    fun searchUser(keyword: String, userListInterface: UserListInterface){
        var service = getClient().create(UserService::class.java).searchUser(keyword)
        service.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    userListInterface.onUserListResponse(response.body() as List<User>)
                } else {
                    userListInterface.onUserListFailure(ServioException("User fetch failed"))
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                userListInterface.onUserListFailure(ServioException("User fetch failed"))
            }
        })
    }

    //Follows a user
    fun follow(token: String, follower: Int, followed: Int, userInterface: UserInterface){
        userService = getClient().create(UserService::class.java)
        var users = userService.follow("Token " + token, follower, followed)

        users.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    userInterface.onUserResponse(response.body() as User)
                } else if (response.code() == 400) {
                    userInterface.onUserFailure(ServioException("User list fetch failed"))
                }
                else{
                    userInterface.onUserFailure(ServioException("User list fetch failed"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                userInterface.onUserFailure(ServioException("User list fetch failed"))
            }
        })
    }

    //Unfollows a user
    fun unfollow(token: String, follower: Int, followed: Int, userInterface: UserInterface){
        userService = getClient().create(UserService::class.java)
        var users = userService.unfollow("Token " + token, follower, followed)

        users.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    userInterface.onUserResponse(response.body() as User)
                } else if (response.code() == 400) {
                    userInterface.onUserFailure(ServioException("User list fetch failed"))
                }
                else{
                    userInterface.onUserFailure(ServioException("User list fetch failed"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                userInterface.onUserFailure(ServioException("User list fetch failed"))
            }
        })
    }

    //Creates service
    fun createService(token: String, service: JsonObject, createServiceInterface: CreateServiceInterface){
        var service = getClient().create(UserService::class.java).createService("Token " + token, service)
        service.enqueue(object : Callback<Service> {
            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                if (response.isSuccessful) {
                    createServiceInterface.onCreateService(response.body() as Service)
                } else {
                    createServiceInterface.onCreateServiceFailure(ServioException("Failed Service Creation"))
                }
            }

            override fun onFailure(call: Call<Service>, t: Throwable) {
                createServiceInterface.onCreateServiceFailure(ServioException("Failed Service Creation"))
            }
        })
    }

    //Search service
    fun searchService(keyword: String, allServicesInterface: AllServicesInterface){
        var service = getClient().create(UserService::class.java).searchService(keyword)
        service.enqueue(object : Callback<List<Service>> {
            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                if (response.isSuccessful) {
                    allServicesInterface.onAllServicesTaken(response.body() as List<Service>)
                } else {
                }
            }

            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
            }
        })
    }

    //Accepts request
    fun acceptRequest(token: String, service: Int, user: Int, createServiceInterface: CreateServiceInterface){
        var service = getClient().create(UserService::class.java).acceptRequest("Token " + token, service, user)
        Log.d("ACCEPT", service.toString() +" "+user.toString() )
        service.enqueue(object : Callback<Service> {
            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                Log.d("ACCEPT", response.toString())
                if (response.isSuccessful) {
                    createServiceInterface.onCreateService(response.body() as Service)
                } else {
                    createServiceInterface.onCreateServiceFailure(ServioException("Failed Request Acceptance"))
                }
            }

            override fun onFailure(call: Call<Service>, t: Throwable) {
                createServiceInterface.onCreateServiceFailure(ServioException("Failed Request Acceptance"))
            }
        })
    }

    //Declines request
    fun declineRequest(token: String, service: Int, user: Int, createServiceInterface: CreateServiceInterface){
        var service = getClient().create(UserService::class.java).declineRequest("Token " + token, service, user)
        service.enqueue(object : Callback<Service> {
            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                if (response.isSuccessful) {
                    createServiceInterface.onCreateService(response.body() as Service)
                } else {
                    createServiceInterface.onCreateServiceFailure(ServioException("Failed Request Decline"))
                }
            }

            override fun onFailure(call: Call<Service>, t: Throwable) {
                createServiceInterface.onCreateServiceFailure(ServioException("Failed Request Decline"))
            }
        })
    }

    //Adds request
    fun addRequest(token: String, service: Int, user: Int, createServiceInterface: CreateServiceInterface){
        var service = getClient().create(UserService::class.java).addRequest("Token " + token, service, user)
        service.enqueue(object : Callback<Service> {
            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                if (response.isSuccessful) {
                    createServiceInterface.onCreateService(response.body() as Service)
                } else {
                    createServiceInterface.onCreateServiceFailure(ServioException("Failed Request Add"))
                }
            }

            override fun onFailure(call: Call<Service>, t: Throwable) {
                createServiceInterface.onCreateServiceFailure(ServioException("Failed Request Add"))
            }
        })
    }

    //Adds feedback to service
    fun addServiceFeedback(token: String, service: Int, feedback: Int, createServiceInterface: CreateServiceInterface){
        var service = getClient().create(UserService::class.java).addServiceFeedback("Token " + token, service, feedback)
        service.enqueue(object : Callback<Service> {
            override fun onResponse(call: Call<Service>, response: Response<Service>) {
                if (response.isSuccessful) {
                    createServiceInterface.onCreateService(response.body() as Service)
                } else {
                    createServiceInterface.onCreateServiceFailure(ServioException("Failed Request Add"))
                }
            }

            override fun onFailure(call: Call<Service>, t: Throwable) {
                createServiceInterface.onCreateServiceFailure(ServioException("Failed Request Add"))
            }
        })
    }

    //Adds credits
    fun addCredits(token: String, user: Int, credits: Int, userInterface: UserInterface){
        var service = getClient().create(UserService::class.java).addCredits("Token " + token, user, credits)
        service.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    userInterface.onUserResponse(response.body() as User)
                } else {
                    userInterface.onUserFailure(ServioException("Failed Request Add"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                userInterface.onUserFailure(ServioException("Failed Request Add"))
            }
        })
    }

    //Adds credits
    fun checkCredits(token: String, user: Int, allServicesInterface: AllServicesInterface){
        var service = getClient().create(UserService::class.java).checkCredits("Token " + token, user)
        service.enqueue(object : Callback<List<Service>> {
            override fun onResponse(call: Call<List<Service>>, response: Response<List<Service>>) {
                if (response.isSuccessful) {
                    allServicesInterface.onAllServicesTaken(response.body() as List<Service>)
                } else {
                }
            }

            override fun onFailure(call: Call<List<Service>>, t: Throwable) {
            }
        })
    }

    //Creates event
    fun createEvent(token:String, map: JsonObject, createEventInterface: CreateEventInterface){
        var event = getClient().create(UserService::class.java).createEvent("Token " + token, map)
        event.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    createEventInterface.onCreateEvent(response.body() as Event)
                } else {
                    createEventInterface.onCreateEventFailure(ServioException("Failed Service Creation"))
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                createEventInterface.onCreateEventFailure(ServioException("Failed Service Creation"))
            }
        })
    }

    //Search event
    fun searchEvent(keyword: String, allEventsInterface: AllEventsInterface){
        var service = getClient().create(UserService::class.java).searchEvent(keyword)
        service.enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    allEventsInterface.onAllEventsTaken(response.body() as List<Event>)
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
            }
        })
    }

    //Adds user to attendees
    fun attend(token:String, event: Int, user: Int, eventUpdateInterface: EventUpdateInterface){
        var event = getClient().create(UserService::class.java).attend("Token " + token, event, user)
        event.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    eventUpdateInterface.onEventUpdated(response.body() as Event)
                } else {
                    eventUpdateInterface.onEventFailure(ServioException("Failed Event Update"))
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                eventUpdateInterface.onEventFailure(ServioException("Failed Event Update"))
            }
        })
    }

    //Creates Feedback
    fun addFeedback(token: String, data: JsonObject, feedbackAddInterface: FeedbackAddInterface){
        userService = getClient().create(UserService::class.java)
        var feedback = userService.addFeedback("Token " + token, data)

        feedback.enqueue(object : Callback<Feedback> {
            override fun onResponse(call: Call<Feedback>, response: Response<Feedback>) {
                if (response.isSuccessful) {
                    feedbackAddInterface.onFeedbackAdded(response.body() as Feedback)
                } else if (response.code() == 400) {
                    feedbackAddInterface.onFeedbackFailure(ServioException("Feedback addition failed"))
                }
                else{
                    feedbackAddInterface.onFeedbackFailure(ServioException("Feedback addition failed"))
                }
            }

            override fun onFailure(call: Call<Feedback>, t: Throwable) {
                feedbackAddInterface.onFeedbackFailure(ServioException("Feedback addition failed"))
            }
        })
    }

    //Gets a set of feedbacks by pk list
    fun getFeedbacks(data: JSONObject, feedbackInterface: FeedbackInterface){
        userService = getClient().create(UserService::class.java)
        var feedbacks = userService.getFeedbacks(data)

        feedbacks.enqueue(object : Callback<List<Feedback>> {
            override fun onResponse(call: Call<List<Feedback>>, response: Response<List<Feedback>>) {
                Log.d("FEEDREPO", response.toString())
                if (response.isSuccessful) {
                    feedbackInterface.onFeedbackSet(response.body() as List<Feedback>)
                } else if (response.code() == 400) {
                    feedbackInterface.onFeedbackFailure(ServioException("Feedback list fetch failed"))
                }
                else{
                    feedbackInterface.onFeedbackFailure(ServioException("Feedback list fetch failed"))
                }
            }

            override fun onFailure(call: Call<List<Feedback>>, t: Throwable) {
                feedbackInterface.onFeedbackFailure(ServioException("Feedback list fetch failed"))
            }
        })
    }
}
