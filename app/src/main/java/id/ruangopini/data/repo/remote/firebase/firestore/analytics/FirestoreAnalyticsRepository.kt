package id.ruangopini.data.repo.remote.firebase.firestore.analytics

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Analytics
import id.ruangopini.data.model.CategoryAnalytics
import id.ruangopini.data.model.DiscussionAnalytics
import id.ruangopini.data.model.PostAnalytics
import id.ruangopini.data.repo.State
import id.ruangopini.utils.COLLECTION
import id.ruangopini.utils.DateFormat
import id.ruangopini.utils.Helpers
import id.ruangopini.utils.Helpers.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class FirestoreAnalyticsRepository : FirestoreAnalyticsDataStore {

    private val instance = Firebase.firestore.collection(COLLECTION.ANALYTICS)

    override fun updateDiscussion(category: String): Flow<State<Boolean>> = flow<State<Boolean>> {
        emit(State.loading())
        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val snapshot = instance.document(date ?: "").collection(COLLECTION.CATEGORY)
            .document(category).update("discussion", FieldValue.increment(1))
        snapshot.await()
        emit(State.success(snapshot.isSuccessful))
    }.catch {
        emit(State.loading())
        val mCategory = CategoryAnalytics(category, 1, 0)

        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val docSnap = instance.document(date ?: "")
        docSnap.set(Analytics()).await()

        val snapshot = docSnap.collection(COLLECTION.CATEGORY)
            .document(category).set(mCategory)
        snapshot.await()

        if (snapshot.isSuccessful) emit(State.success(true))
        else {
            Log.d("TAG", "updateDiscussion: failed = ${it.message}")
            emit(State.failed(it.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateJoinCategory(category: String, isJoin: Boolean) = flow<State<Boolean>> {
        emit(State.loading())
        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val snapshot = instance.document(date ?: "").collection(COLLECTION.CATEGORY)
            .document(category).update("join", FieldValue.increment(if (isJoin) 1 else -1))
        snapshot.await()
        emit(State.success(snapshot.isSuccessful))
    }.catch {
        emit(State.loading())
        val mCategory = CategoryAnalytics(category, 0, 1)

        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val docSnap = instance.document(date ?: "")
        docSnap.set(Analytics()).await()

        val snapshot = docSnap.collection(COLLECTION.CATEGORY)
            .document(category).set(mCategory)
        snapshot.await()

        if (snapshot.isSuccessful) emit(State.success(true))
        else {
            Log.d("TAG", "updateJoinCategory: failed = ${it.message}")
            emit(State.failed(it.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAnotherPopular() = callbackFlow<State<List<CategoryAnalytics>>> {
        trySend(State.loading()).isSuccess
        instance.whereLessThan("createdAt", Timestamp.now())
            .whereGreaterThan("createdAt", Helpers.getSevenDayAgo())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(State.failed(error.message ?: "")).isSuccess
                    close(error)
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) value.toObjects(Analytics::class.java)
                    .forEach { data ->
                        instance.document(data.time ?: "").collection(COLLECTION.CATEGORY)
                            .addSnapshotListener { result, error ->
                                if (error != null) {
                                    trySend(State.failed(error.message ?: "")).isSuccess
                                    close(error)
                                }

                                val category = if (result != null && !result.isEmpty)
                                    result.toObjects(CategoryAnalytics::class.java)
                                else emptyList()

                                trySend(State.success(category)).isSuccess
                            }
                    }
            }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun updatePostDiscussion(discussionId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val snapshot = instance.document(date ?: "").collection(COLLECTION.DISCUSSION)
            .document(discussionId).update("post", FieldValue.increment(1))
        snapshot.await()
        emit(State.success(snapshot.isSuccessful))
    }.catch {
        emit(State.loading())
        val discussion = DiscussionAnalytics(0, 0, 1)

        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val docSnap = instance.document(date ?: "")
        docSnap.set(Analytics()).await()

        val snapshot = docSnap.collection(COLLECTION.DISCUSSION)
            .document(discussionId).set(discussion)
        snapshot.await()

        if (snapshot.isSuccessful) emit(State.success(true))
        else {
            Log.d("TAG", "updatePostDiscussion: failed = ${it.message}")
            emit(State.failed(it.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    override fun getPopularDiscussion() = callbackFlow<State<List<DiscussionAnalytics>>> {
        trySend(State.loading()).isSuccess
        instance.whereLessThan("createdAt", Timestamp.now())
            .whereGreaterThan("createdAt", Helpers.getSevenDayAgo())
            .addSnapshotListener { value, error ->
                if (error != null) {
                    trySend(State.failed(error.message ?: "")).isSuccess
                    close(error)
                    return@addSnapshotListener
                }
                if (value != null && !value.isEmpty) value.toObjects(Analytics::class.java)
                    .forEach { data ->
                        instance.document(data.time ?: "").collection(COLLECTION.DISCUSSION)
                            .addSnapshotListener { result, error ->
                                if (error != null) {
                                    trySend(State.failed(error.message ?: "")).isSuccess
                                    close(error)
                                }

                                val discussion = if (result != null && !result.isEmpty)
                                    result.toObjects(DiscussionAnalytics::class.java)
                                else emptyList()

                                trySend(State.success(discussion)).isSuccess
                            }
                    }
            }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun updateJoinDiscussion(discussionId: String, isJoin: Boolean) =
        flow<State<Boolean>> {
            emit(State.loading())
            val date = Timestamp.now().formatDate(DateFormat.SHORT)
            val snapshot = instance.document(date ?: "").collection(COLLECTION.DISCUSSION)
                .document(discussionId).update("join", FieldValue.increment(if (isJoin) 1 else -1))
            snapshot.await()
            emit(State.success(snapshot.isSuccessful))
        }.catch {
            emit(State.loading())
            val discussion = DiscussionAnalytics(1, 0, 0)

            val date = Timestamp.now().formatDate(DateFormat.SHORT)
            val docSnap = instance.document(date ?: "")
            docSnap.set(Analytics()).await()

            val snapshot = docSnap.collection(COLLECTION.DISCUSSION)
                .document(discussionId).set(discussion)
            snapshot.await()

            if (snapshot.isSuccessful) emit(State.success(true))
            else {
                Log.d("TAG", "updateJoinDiscussion: failed = ${it.message}")
                emit(State.failed(it.message ?: ""))
            }
        }.flowOn(Dispatchers.IO)

    override fun updateCommentDiscussion(discussionId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val snapshot = instance.document(date ?: "").collection(COLLECTION.DISCUSSION)
            .document(discussionId).update("comment", FieldValue.increment(1))
        snapshot.await()
        emit(State.success(snapshot.isSuccessful))
    }.catch {
        emit(State.loading())
        val discussion = DiscussionAnalytics(0, 1, 0)

        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val docSnap = instance.document(date ?: "")
        docSnap.set(Analytics()).await()

        val snapshot = docSnap.collection(COLLECTION.DISCUSSION)
            .document(discussionId).set(discussion)
        snapshot.await()

        if (snapshot.isSuccessful) emit(State.success(true))
        else {
            Log.d("TAG", "updateCommentDiscussion: failed = ${it.message}")
            emit(State.failed(it.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateCommentPost(postId: String) = flow<State<Boolean>> {
        emit(State.loading())
        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val snapshot = instance.document(date ?: "").collection(COLLECTION.POST)
            .document(postId).update("comment", FieldValue.increment(1))
        snapshot.await()
        emit(State.success(snapshot.isSuccessful))
    }.catch {
        emit(State.loading())
        val post = PostAnalytics(0, 0, 1)

        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val docSnap = instance.document(date ?: "")
        docSnap.set(Analytics()).await()

        val snapshot = docSnap.collection(COLLECTION.POST)
            .document(postId).set(post)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
        else {
            Log.d("TAG", "updateCommentPost: failed = ${it.message}")
            emit(State.failed(it.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateVoteUpPost(postId: String, vote: Int) = flow<State<Boolean>> {
        emit(State.loading())
        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val snapshot = instance.document(date ?: "").collection(COLLECTION.POST)
            .document(postId).update("voteUp", FieldValue.increment(vote.toLong()))
        snapshot.await()
        emit(State.success(snapshot.isSuccessful))
    }.catch {
        emit(State.loading())
        val post = PostAnalytics(1, 0, 0)

        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val docSnap = instance.document(date ?: "")
        docSnap.set(Analytics()).await()

        val snapshot = docSnap.collection(COLLECTION.POST)
            .document(postId).set(post)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
        else {
            Log.d("TAG", "updateVoteUpPost: failed = ${it.message}")
            emit(State.failed(it.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)

    override fun updateVoteDownPost(postId: String, vote: Int) = flow<State<Boolean>> {
        emit(State.loading())
        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val snapshot = instance.document(date ?: "").collection(COLLECTION.POST)
            .document(postId).update("voteDown", FieldValue.increment(vote.toLong()))
        snapshot.await()
        emit(State.success(snapshot.isSuccessful))
    }.catch {
        emit(State.loading())
        val post = PostAnalytics(0, 1, 0)

        val date = Timestamp.now().formatDate(DateFormat.SHORT)
        val docSnap = instance.document(date ?: "")
        docSnap.set(Analytics()).await()

        val snapshot = docSnap.collection(COLLECTION.POST)
            .document(postId).set(post)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(true))
        else {
            Log.d("TAG", "updateVoteDownPost: failed = ${it.message}")
            emit(State.failed(it.message ?: ""))
        }
    }.flowOn(Dispatchers.IO)
}