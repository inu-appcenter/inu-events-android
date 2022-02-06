package org.inu.events.googlelogin

import android.content.Context
import android.util.Log
import org.inu.events.googlelogin.api.LoginService
import org.inu.events.googlelogin.model.LoginGoogleRequestModel
import org.inu.events.googlelogin.model.LoginGoogleResponseModel
import org.inu.events.objects.ClientInformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class TokenExchanger(
    private val context: Context
) {

    private val getAccessTokenBaseUrl = "https://www.googleapis.com"
    // private val sendAccessTokenBaseUrl = "http://uniletter.inuappcenter.kr"

    fun getAccessToken(authCode: String, onSuccess: (String) -> Unit) {
        LoginService.loginRetrofit(getAccessTokenBaseUrl).getAccessToken(
            request = LoginGoogleRequestModel(
                grant_type = "authorization_code",
                client_id = ClientInformation.CLIENT_ID,
                client_secret = ClientInformation.CLIENT_SECRET,
                code = authCode.orEmpty()
            )
        ).enqueue(object : Callback<LoginGoogleResponseModel> {
            override fun onResponse(
                call: Call<LoginGoogleResponseModel>,
                response: Response<LoginGoogleResponseModel>
            ) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.access_token.orEmpty()
                    Log.d(TAG, "getOnResponse: $accessToken")
                    // TODO
                    onSuccess(accessToken)
                } else {
                    throw RuntimeException("~~!!!!!!!!!!!!!!!!!!")
                }
            }

            override fun onFailure(call: Call<LoginGoogleResponseModel>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ", t.fillInStackTrace())
                throw t
            }
        })
    }

//    fun sendAccessToken(accessToken: String) {
//        LoginService.loginRetrofit(sendAccessTokenBaseUrl).sendAccessToken(
//            accessToken = SendAccessTokenModel(accessToken)
//        ).enqueue(object : Callback<String> {
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                if (response.isSuccessful) {
//                    Log.d(TAG, "sendOnResponse: ${response.body()}")
//                    Toast.makeText(context, "로그인 되셨습니다~!", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                Log.e(TAG, "sendOnFailure: ${t.fillInStackTrace()}")
//            }
//        })
//    }

    companion object {
        const val TAG = "LoginRepository"
    }
}