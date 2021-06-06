package id.ruangopini.data.repo.remote.firebase.firestore.help

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.repo.State
import id.ruangopini.utils.COLLECTION
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class FirestoreHelpRepository : FirestoreHelpDataSource {
    private val instance = Firebase.firestore.collection(COLLECTION.HELP)
    override fun onRespond(name: String, isHelp: Boolean) = callbackFlow<State<Boolean>> {
        trySend(State.loading()).isSuccess
        instance.whereEqualTo("name", name).get().addOnCompleteListener {
            it.result?.forEach { doc ->
                val id = doc.id
                instance.document(id).update(
                    if (isHelp) "helped" else "notHelped", FieldValue.increment(1)
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) trySend(State.success(true)).isSuccess
                    else trySend(State.success(false)).isSuccess
                }
            }
        }
        awaitClose()
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}