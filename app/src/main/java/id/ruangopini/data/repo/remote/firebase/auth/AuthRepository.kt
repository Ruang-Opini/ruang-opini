package id.ruangopini.data.repo.remote.firebase.auth

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.repo.State
import id.ruangopini.utils.DataHelpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class AuthRepository : AuthDataStore {

    private val TAG = "AuthRepository"
    private val mAuth = Firebase.auth

    override fun signUpWithEmail(email: String, pass: String) = flow {
        emit(State.loading())
        val snapshot = mAuth.createUserWithEmailAndPassword(email, pass)
        snapshot.await()
        val success = snapshot.isSuccessful
        Log.d(TAG, "signUpWithEmail: ref = ${snapshot.result?.user?.uid}")
        if (success) emit(State.Success(mapOf(true to "Sukses membuat akun baru")))
    }.catch {
        emit(State.Failed(DataHelpers.errorLoginMessage[it.message] ?: it.message!!))
    }.flowOn(Dispatchers.IO)


    override fun updatePassword(
        email: String,
        pass: String,
        newPass: String
    ): Flow<State<Boolean>> = flow {
        emit(State.loading())
        val credential = EmailAuthProvider.getCredential(email, pass)
        val reAuth = mAuth.currentUser?.reauthenticate(credential)
        reAuth?.await()
        if (reAuth?.isSuccessful == true) {
            val snapshot = mAuth.currentUser?.updatePassword(newPass)
            snapshot?.await()
            if (snapshot?.isSuccessful == true) emit(State.success(true))
        } else emit(State.success(false))
    }.catch {
        emit(State.Failed(DataHelpers.errorLoginMessage[it.message] ?: it.message!!))
    }.flowOn(Dispatchers.IO)

    override fun loginWithEmail(email: String, pass: String) = flow {
        emit(State.loading())
        val snapshot = mAuth.signInWithEmailAndPassword(email, pass)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.Failed(DataHelpers.errorLoginMessage[it.message] ?: it.message!!))
    }.flowOn(Dispatchers.IO)

    override fun loginWithGoogle(idToken: String, activity: Activity) =
        callbackFlow<State<Boolean>> {
            this.trySend(State.loading()).isSuccess
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            mAuth.signInWithCredential(credential).addOnCompleteListener(activity) {
                if (it.isSuccessful) {
                    Log.d(TAG, "loginWithGoogle: ${it.result?.user?.uid}")
                    this.trySend(State.success(true)).isSuccess
                } else {
                    this.trySend(State.success(false)).isSuccess
                    Log.d(TAG, "signInWithCredential:failure", it.exception)
                }
            }
            awaitClose()
        }.catch {
            emit(State.failed(it.message ?: ""))
        }.flowOn(Dispatchers.IO)

    override fun forgotPassword(email: String) = flow {
        emit(State.loading())
        val snapshot = mAuth.sendPasswordResetEmail(email)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

}