package id.ruangopini.data.repo.remote.firebase.firestore.issue

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Issue
import id.ruangopini.data.repo.State
import id.ruangopini.utils.COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await

@ExperimentalCoroutinesApi
class FirestoreIssueRepository : FirestoreIssueDataSource {

    private val instance = Firebase.firestore.collection(COLLECTION.ISSUE)

    override fun findIssue(name: String) = flow {
        emit(State.loading())
        val snapshot = instance.whereEqualTo("name", name.lowercase()).get()
        snapshot.await()
        if (snapshot.isSuccessful) emit(
            State.success(
                snapshot.result?.toObjects(Issue::class.java)?.first() ?: Issue()
            )
        ) else emit(State.success(Issue()))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun createNewIssue(issue: Issue) = flow {
        emit(State.loading())
        val docId = instance.document()
        val snapshot = docId.set(issue)
        snapshot.await()
        if (snapshot.isSuccessful) emit(State.success(docId.id))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getAllIssue() = callbackFlow<State<List<Issue>>> {
        trySend(State.loading()).isSuccess
        instance.addSnapshotListener { value, error ->
            if (error != null) {
                trySend(State.failed(error.message ?: ""))
                close(error)
                return@addSnapshotListener
            }

            val discussions = if (value != null && !value.isEmpty)
                value.toObjects(Issue::class.java)
            else emptyList()
            this.trySend(State.success(discussions)).isSuccess
        }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}