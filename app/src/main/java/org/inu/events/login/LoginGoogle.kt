package org.inu.events.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import org.inu.events.objects.ClientInformation

class LoginGoogle(context: Context) {
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(ClientInformation.CLIENT_ID)
        .requestServerAuthCode(ClientInformation.CLIENT_ID)
        .requestEmail()
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(context, gso)

    fun signIn(activity: Activity) {
        val signInIntent: Intent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, 1000)

    }

    fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val authCode: String? = completedTask.getResult(ApiException::class.java)?.serverAuthCode
            LoginRepository().getAccessToken(authCode!!)
        }

        catch (e: ApiException) {
            Log.w(TAG, "handleSignInResult: error" + e.statusCode)
        }
    }
    companion object {
        const val TAG = "GoogleLoginService"
    }
//    fun autoLogin(activity: Activity, function: () -> Unit) {
//        googleSignInClient.silentSignIn()
//            .addOnCompleteListener(
//                activity,
//                OnCompleteListener<GoogleSignInAccount?> { task ->
//                    handleSignInResult(task)
//                }
//            )
//    }

//    fun isLogin(context: Context): Boolean {
//        val account = GoogleSignIn.getLastSignedInAccount(context)
//        return if (account == null) false else (true)
//    }



}