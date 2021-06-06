package id.ruangopini.data.repo.remote.firebase.firestore.help

import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow


interface FirestoreHelpDataSource {
    fun onRespond(name: String, isHelp: Boolean): Flow<State<Boolean>>
}