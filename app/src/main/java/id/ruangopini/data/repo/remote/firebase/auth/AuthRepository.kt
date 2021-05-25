package id.ruangopini.data.repo.remote.firebase.auth

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.repo.State
import id.ruangopini.utils.DataHelpers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

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
        emit(State.Failed(DataHelpers.errorLoginMessage[it.message] ?: ""))
    }.flowOn(Dispatchers.IO)

}