package id.ruangopini.data.repo.remote.firebase.firestore.discussion

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Discussion
import id.ruangopini.data.repo.State
import id.ruangopini.utils.COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

class FirestoreDiscussionRepository : FirestoreDiscussionDataSource {

    private val instance = Firebase.firestore.collection(COLLECTION.DISCUSSION)

    @ExperimentalCoroutinesApi
    override fun getLatestDiscussionRoom() = callbackFlow<State<List<Discussion>>> {
        trySend(State.loading()).isSuccess
        instance
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(State.failed(error.message ?: ""))
                    close(error)
                    return@addSnapshotListener
                }

                val discussions = if (value != null && !value.isEmpty)
                    value.toObjects(Discussion::class.java)
                else emptyList()
                this.trySend(State.success(discussions)).isSuccess
            }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun createNewDiscussion(discussion: Discussion) = flow {
        emit(State.loading())
        val snapshot = instance.document().set(discussion)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true)) else emit(State.success(false))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    // TODO: 5/26/2021 get trending discussion
}