package com.laksana.consumerapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laksana.consumerapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.BuildConfig
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel: ViewModel() {
    val listAllFollowing = MutableLiveData<ArrayList<User>>()

    fun setFollowing(username: String) {
        val listFollowing = ArrayList<User>()
        Log.d("usernameSetFollowing", username)

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/following"
        val apiKey: String = BuildConfig.BUILD_TYPE
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    Log.d("usernameResult", result)

                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val followingName = item.getString("login")
                        val avatar = item.getString("avatar_url")

                        val user = User(followingName, avatar)
                        user.username = followingName
                        user.avatar = avatar
                        listFollowing.add(user)
                        Log.d("usernameListUser", username)
                    }
                    listAllFollowing.postValue(listFollowing)
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
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getFollowing(): LiveData<ArrayList<User>> {
        return listAllFollowing
    }

}