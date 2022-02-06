package org.inu.events.googlelogin

import android.util.Log
import org.inu.events.googlelogin.api.LoginService
import org.inu.events.googlelogin.model.LoginGoogleRequestModel
import org.inu.events.googlelogin.model.LoginGoogleResponseModel
import org.inu.events.objects.ClientInformation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TokenExchanger {
    private val getAccessTokenBaseUrl = "https://www.googleapis.com"

    fun getAccessToken(
        authCode: String,
        onSuccess: (String) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        LoginService.loginRetrofit(getAccessTokenBaseUrl).getAccessToken(
            request = LoginGoogleRequestModel(
                grant_type = "authorization_code",
                client_id = ClientInformation.CLIENT_ID,
                client_secret = ClientInformation.CLIENT_SECRET,
                code = authCode
            )
        ).enqueue(object : Callback<LoginGoogleResponseModel> {
            override fun onResponse(
                call: Call<LoginGoogleResponseModel>,
                response: Response<LoginGoogleResponseModel>
            ) {
                if (response.isSuccessful) {
                    val accessToken = response.body()?.access_token.orEmpty()
                    Log.d(TAG, "getOnResponse: $accessToken")
                    onSuccess(accessToken)
                } else {
                    onFailure(RuntimeException("액세스 토큰을 가지고 싶은 와중에 구글로부터 실패 응답이 왔습니다. 어서 이 문제를 해결해주세욧!!"))
                }
            }

            override fun onFailure(call: Call<LoginGoogleResponseModel>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ", t.fillInStackTrace())
                onFailure(t)
            }
        })
    }

    companion object {
        const val TAG = "TokenExchanger"
    }
}