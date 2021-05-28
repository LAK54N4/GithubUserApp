package com.laksana.consumerapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laksana.consumerapp.model.UserDetail
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.BuildConfig
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailViewModel: ViewModel() {
    private val detailUser = MutableLiveData<UserDetail>()

    fun showDetailsUser(username: String) {

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"

        val apiKey: String = BuildConfig.BUILD_TYPE
        client.addHeader("Authorization", apiKey)
        client.addHeader("User-Agent","request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    Log.d("detailUser", responseObject.toString())

                    val id = responseObject.getInt("id")
                    val userName = responseObject.getString("login")
                    val avatar = responseObject.getString("avatar_url")
                    val location = responseObject.getString("location")
                    val repository = responseObject.getString("public_repos")
                    val followers = responseObject.getString("followers")
                    val following = responseObject.getString("following")

                    val user = UserDetail (id, userName, avatar, location, repository, followers, following)
                    detailUser.postValue(user)

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

    fun getDetailUser(): LiveData<UserDetail> {
        return detailUser
    }
}