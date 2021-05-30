package id.ruangopini.data.repo.remote.firebase.auth

import android.app.Activity
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface AuthDataStore {
    fun signUpWithEmail(email: String, pass: String): Flow<State<Map<Boolean, String>>>
    fun updatePassword(email: String, pass: String, newPass: String): Flow<State<Boolean>>
    fun loginWithEmail(email: String, pass: String): Flow<State<Boolean>>
    fun loginWithGoogle(idToken: String, activity: Activity): Flow<State<Boolean>>
    fun forgotPassword(email: String): Flow<State<Boolean>>
}