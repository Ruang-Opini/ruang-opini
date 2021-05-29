package id.ruangopini.data.repo.remote.firebase.auth

import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface AuthDataStore {
    fun signUpWithEmail(email: String, pass: String): Flow<State<Map<Boolean, String>>>
    fun updatePassword(email: String, pass: String, newPass: String): Flow<State<Boolean>>
}