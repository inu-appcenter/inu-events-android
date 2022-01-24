package org.inu.events.login

import android.util.Log
import org.inu.events.login.dao.LoginGoogleRequest
import org.inu.events.login.dao.LoginGoogleResponse
import org.inu.events.objects.ClientInformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginRepository {

    private val getAccessTokenBaseUrl = "https://www.googleapis.com"
    private val sendAccessTokenBaseUrl = "http://uniletter.inuappcenter.kr"

    fun getAccessToken(authCode:String) {
        LoginRetrofitClient().retrofit(getAccessTokenBaseUrl).getAccessToken(
            request = LoginGoogleRequest(
                grant_type = "authorization_code",
                client_id = ClientInformation.CLIENT_ID,
                client_secret = ClientInformation.CLIENT_SECRET,
                code = authCode.orEmpty()
            )
        ).enqueue(object : Callback<LoginGoogleResponse> {
            override fun onResponse(call: Call<LoginGoogleResponse>, response: Response<LoginGoogleResponse>) {
                if(response.isSuccessful) {
                    val accessToken = response.body()?.access_token.orEmpty()
                    Log.d(TAG, "get: $accessToken")
                    sendAccessToken(accessToken)
                }
            }
            override fun onFailure(call: Call<LoginGoogleResponse>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ",t.fillInStackTrace() )
            }
        })
    }

    fun sendAccessToken(accessToken:String){
        LoginRetrofitClient().retrofit(sendAccessTokenBaseUrl).sendAccessToken(
            accessToken = accessToken
        ).enqueue(object :Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    Log.d(TAG, "onResponse: ${response.body()}")
                }
                Log.d(TAG, "send accessToken?: $accessToken")
                Log.d(TAG, "onResponse: ${response.message()}")
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(TAG, "sendOnFailure: ${t.fillInStackTrace()}", )
            }
        })
    }

    companion object {
        const val TAG = "LoginRepository"
    }
}