package org.inu.events.googlelogin

import android.app.Activity
import androidx.activity.ComponentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.inu.events.common.extension.registerForActivityResult
import org.inu.events.common.extension.toast
import org.inu.events.objects.ClientInformation

class GoogleLoginWrapper(private val activity: ComponentActivity) {
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(ClientInformation.CLIENT_ID)
        .requestServerAuthCode(ClientInformation.CLIENT_ID)
        .requestEmail()
        .build()

    private val googleSignInClient by lazy { GoogleSignIn.getClient(activity, gso) }

    private val launcher = activity.registerForActivityResult { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            activity.toast("구글 로그인 실패!")
            return@registerForActivityResult
        }

        val account = try {
            GoogleSignIn
                .getSignedInAccountFromIntent(result.data)
                .getResult(ApiException::class.java)
        } catch (e: ApiException) {
            e.printStackTrace()
            activity.toast("로그인된 구글 계정 가져오는 데에 실패함")
            return@registerForActivityResult
        }

        TokenExchanger().getAccessToken(account.serverAuthCode!!, onAccessTokenSecured) {
            it.printStackTrace()
            activity.toast("Access token을 가져오는 데에 실패함")
        }
    }

    private lateinit var onAccessTokenSecured: (String) -> Unit

    fun signIn(onAccessTokenSecured: (String) -> Unit) {
        this.onAccessTokenSecured = onAccessTokenSecured

        launcher.launch(googleSignInClient.signInIntent)
    }

    fun signOut() {
        googleSignInClient.signOut().addOnCompleteListener {
            activity.toast("로그아웃 되셨습니다!")
        }
    }
}