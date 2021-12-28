package com.erkaslan.servio.manager

import com.erkaslan.servio.model.UserService
import com.erkaslan.servio.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RunOnceManager {
    lateinit var userService: UserService
    lateinit var userList: MutableList<User>
    /*
    userService = RetrofitClient.getClient().create(UserService::class.java)
    var users = userService.getUsers()

    users.enqueue(object : Callback<List<User>> {
        override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

            if (response.isSuccessful) {
                userList = (response.body() as MutableList<User>?)!!
            }
        }

        override fun onFailure(call: Call<List<User>>, t: Throwable) {

        }
    })

     */
}