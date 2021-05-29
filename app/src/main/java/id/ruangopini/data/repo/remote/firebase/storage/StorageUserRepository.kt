package id.ruangopini.data.repo.remote.firebase.storage

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import id.ruangopini.data.repo.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

class StorageUserRepository : StorageUserDataStore {
    private val instance = Firebase.storage

    override fun uploadPhoto(uri: Uri, path: String) = flow {
        emit(State.Loading())
        val ref = instance.reference.child(path)
        val uploadTask = ref.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) task.exception.let { throw it!! }
            ref.downloadUrl
        }
        uploadTask.await()
        if (uploadTask.isSuccessful) emit(State.Success(uploadTask.result.toString()))
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)

    override fun getImageUrl(path: String): Flow<State<Uri>> = flow {
        emit(State.loading())
        val task = instance.reference.child(path).downloadUrl
        task.await()
        if (task.isSuccessful) task.result?.let { result ->
            emit(State.success(result))
        }
    }.catch {
        emit(State.failed(it.message ?: ""))
    }.flowOn(Dispatchers.IO)
}