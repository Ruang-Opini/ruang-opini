package id.ruangopini.data.repo.remote.firebase.storage

import android.net.Uri
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface StorageUserDataStore {
    fun uploadPhoto(uri: Uri, path: String): Flow<State<String>>
}