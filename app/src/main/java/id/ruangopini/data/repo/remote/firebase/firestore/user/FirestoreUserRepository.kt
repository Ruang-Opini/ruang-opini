package id.ruangopini.data.repo.remote.firebase.firestore.user

import com.google.firebase.Timestamp
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
                trySend(State.failed(error.message ?: "")).isSuccess
                close(error)
                return@addSnapshotListener
            }

            if (value != null && value.exists()) value.toObject(User::class.java).let {
                this.trySend(State.success(it ?: User())).isSuccess
            } else this.trySend(State.success(User())).isSuccess
        }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getUserByUsername(username: String) = flow {
        emit(State.loading())
        val snapshot = instance.whereEqualTo("username", username).get()
        snapshot.await()
        if (snapshot.isSuccessful) emit(
            State.success(
                snapshot.result?.toObjects(User::class.java)?.first() ?: User()
            )
        )
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun updateUser(user: User) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot = instance.document(user.userId ?: "").update(
            mapOf(
                "name" to user.name,
                "bio" to user.bio,
                "photoUrl" to user.photoUrl,
                "headerUrl" to user.headerUrl,
                "updatedAt" to Timestamp.now(),
            )
        )
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}