package id.ruangopini.data.repo.remote.firebase.firestore.comment

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Comment
import id.ruangopini.data.repo.State
import id.ruangopini.utils.COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class FirestoreCommentRepository : FirestoreCommentDataSource {

    private val instance = Firebase.firestore.collection(COLLECTION.COMMENT)

    override fun createComment(comment: Comment) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot = instance.document().set(comment)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true)) else emit(State.success(false))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun loadComment(postId: String) = callbackFlow<State<List<Comment>>> {
        trySend(State.loading()).isSuccess
        instance.whereEqualTo("postId", postId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) trySend(State.failed(error.message ?: "")).isSuccess
                if (value != null && !value.isEmpty) trySend(State.success(value.toObjects(Comment::class.java))).isSuccess
                else trySend(State.success(emptyList())).isSuccess
            }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getCommentByUserId(userId: String) = callbackFlow<State<List<Comment>>> {
        trySend(State.loading()).isSuccess
        instance.whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) trySend(State.failed(error.message ?: "")).isSuccess
                if (value != null && !value.isEmpty) trySend(State.success(value.toObjects(Comment::class.java))).isSuccess
                else trySend(State.success(emptyList())).isSuccess
            }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}