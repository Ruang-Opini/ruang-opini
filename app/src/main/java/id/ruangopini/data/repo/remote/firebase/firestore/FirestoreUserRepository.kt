package id.ruangopini.data.repo.remote.firebase.firestore

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.User
import id.ruangopini.data.repo.State
import id.ruangopini.utils.COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class FirestoreUserRepository : FirestoreUserDataSource {

    private val instance = Firebase.firestore.collection(COLLECTION.USER)

    override fun createNewUser(user: User) = flow {
        emit(State.loading())
        val id = Firebase.auth.currentUser?.uid ?: ""
        val snapshot = instance.document(id).set(user)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun updatePhoto(path: String) = flow {
        emit(State.loading())
        val id = Firebase.auth.currentUser?.uid ?: ""
        val snapshot = instance.document(id).update("photoUrl", path)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}