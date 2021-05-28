package com.laksana.consumerapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laksana.consumerapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel: ViewModel() {
    private val listUser = ArrayList<User>()
    private val mutableListUser = MutableLiveData<ArrayList<User>>()

    companion object {
        const val TOKEN: String = com.loopj.android.http.BuildConfig.BUILD_TYPE
    }

    fun setSearch(username: String) {
        val client = AsyncHttpClient()
        Log.d("username", username)
        val url = "https://api.github.com/search/users?q=$username"
        client.addHeader("Authorization", TOKEN)
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
            ) {
                val result = String(responseBody)

                try {
                    val responseObject = JSONObject(result)
                    val itemsUser = responseObject.getJSONArray("items")
                    Log.d("usernameList", itemsUser.toString())
                    listUser.clear()
                    for (i in 0 until itemsUser.length()) {
                        val searchItems = itemsUser.getJSONObject(i)
                        val searchUsers = searchItems.getString("login")
                        val searchAvatar = searchItems.getString("avatar_url")

                        val searchUser = User(searchUsers, searchAvatar)
                        searchUser.username = searchUsers
                        searchUser.avatar = searchAvatar
                        listUser.add(searchUser)
                    }
                    mutableListUser.postValue(listUser)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("error", errorMessage)
            }
        })
    }

    fun getListUsers() : LiveData<ArrayList<User>>{
        return mutableListUser
    }
}