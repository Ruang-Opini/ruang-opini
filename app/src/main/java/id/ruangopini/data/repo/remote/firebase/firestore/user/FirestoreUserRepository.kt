package id.ruangopini.data.repo.remote.firebase.firestore.user

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.User
import id.ruangopini.data.repo.State
import id.ruangopini.utils.COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
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

    @ExperimentalCoroutinesApi
    override fun getUserById(userId: String) = callbackFlow<State<User>> {
        this.trySend(State.loading()).isSuccess
        instance.document(userId).addSnapshotListener { value, error ->
            if (error != null) {
                trySend(State.failed(error.message ?: ""))
                close(error)
                return@addSnapshotListener
            }

            if (value != null && value.exists()) value.toObject(User::class.java)?.let {
                this.trySend(State.success(it)).isSuccess
            }
        }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}