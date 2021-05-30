package id.ruangopini.data.repo.remote.firebase.auth

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.R

object AuthHelpers {

    private fun Activity.getGSO(): GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    fun getGoogleClient(activity: Activity): GoogleSignInClient =
        GoogleSignIn.getClient(activity, activity.getGSO())

    private fun Activity.getLastGoogleSign() = GoogleSignIn.getLastSignedInAccount(this)

    fun signOut(activity: Activity) {
        Firebase.auth.signOut()
        Firebase.auth.currentUser?.reload()
        if (activity.getLastGoogleSign() != null) {
            getGoogleClient(activity).signOut()
            getGoogleClient(activity).revokeAccess()
        }
    }
}