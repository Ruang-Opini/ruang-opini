package id.ruangopini.data.repo.remote.firebase.firestore.post

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Post
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
class FirestorePostRepository : FirestorePostDataSource {

    private val instance = Firebase.firestore.collection(COLLECTION.POST)

    override fun createNewPost(post: Post, postId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot = instance.document(postId).set(post)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true)) else emit(State.success(false))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getPostByDiscussionId(discussionId: String) = callbackFlow<State<List<Post>>> {
        trySend(State.loading()).isSuccess
        instance.whereEqualTo("discussionId", discussionId).addSnapshotListener { value, error ->
            if (error != null) trySend(State.failed(error.message ?: "")).isSuccess
            if (value != null && !value.isEmpty) trySend(State.success(value.toObjects(Post::class.java))).isSuccess
            else trySend(State.success(emptyList())).isSuccess
        }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getPostByUserId(userId: String) = callbackFlow<State<List<Post>>> {
        trySend(State.loading()).isSuccess
        instance.whereEqualTo("userId", userId).addSnapshotListener { value, error ->
            if (error != null) trySend(State.failed(error.message ?: "")).isSuccess
            if (value != null && !value.isEmpty) trySend(State.success(value.toObjects(Post::class.java))).isSuccess
            else trySend(State.success(emptyList())).isSuccess
        }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun updateVoteUp(vote: Int, postId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot =
            instance.document(postId).update("voteUp", FieldValue.increment(vote.toLong()))
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun updateVoteDown(vote: Int, postId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot =
            instance.document(postId).update("voteDown", FieldValue.increment(vote.toLong()))
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun addToVote(postId: String, userId: String, isUp: Boolean) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot = instance.document(postId)
            .update(if (isUp) "peopleVoteUp" else "peopleVoteDown", FieldValue.arrayUnion(userId))
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun remoteFromVote(postId: String, userId: String, isUp: Boolean) =
        flow<State<Boolean>> {
            emit(State.loading())
            val snapshot = instance.document(postId)
                .update(
                    if (isUp) "peopleVoteUp" else "peopleVoteDown",
                    FieldValue.arrayRemove(userId)
                )
            snapshot.await()
            if (snapshot.isSuccessful) emit(State.success(true))
        }.catch {
            emit(State.failed(it.message ?: ""))
        }.flowOn(Dispatchers.IO)

    override fun addComment(postId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val snapshot =
            instance.document(postId).update("comment", FieldValue.increment(1))
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getPostById(postId: String) = callbackFlow<State<Post>> {
        trySend(State.loading()).isSuccess
        instance.document(postId).addSnapshotListener { value, error ->
            if (error != null) trySend(State.failed(error.message ?: "")).isSuccess
            if (value != null && value.exists()) trySend(
                State.success(
                    value.toObject(Post::class.java) ?: Post()
                )
            ).isSuccess
            else trySend(State.success(Post())).isSuccess
        }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}