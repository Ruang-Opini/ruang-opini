package id.ruangopini.data.repo.remote.firebase.firestore.discussion

import com.google.firebase.firestore.FieldValue
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

@ExperimentalCoroutinesApi
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

    override fun joinDiscussion(discussionId: String, userId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot = instance.document(discussionId).update(
            mapOf(
                "members" to FieldValue.arrayUnion(userId),
                "people" to FieldValue.increment(1)
            )
        )
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
        else emit(State.success(false))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun leaveDiscussion(discussionId: String, userId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot = instance.document(discussionId).update(
            mapOf(
                "members" to FieldValue.arrayRemove(userId),
                "people" to FieldValue.increment(-1)
            )
        )
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
        else emit(State.success(false))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getDiscussionById(discussionId: String) = callbackFlow<State<Discussion>> {
        trySend(State.loading()).isSuccess
        instance.document(discussionId).addSnapshotListener { value, error ->
            if (error != null) {
                trySend(State.failed(error.message ?: ""))
                close(error)
                return@addSnapshotListener
            }

            val discussions = if (value != null && value.exists())
                value.toObject(Discussion::class.java) ?: Discussion()
            else Discussion()
            this.trySend(State.success(discussions)).isSuccess
        }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getDiscussionByIssueName(issueName: String) =
        callbackFlow<State<List<Discussion>>> {
            trySend(State.loading()).isSuccess
            instance.whereEqualTo("issueName", issueName).addSnapshotListener { value, error ->
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

    // TODO: 5/26/2021 get trending discussion
}