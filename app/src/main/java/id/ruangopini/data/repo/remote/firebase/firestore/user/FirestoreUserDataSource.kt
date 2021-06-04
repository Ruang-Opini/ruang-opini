package id.ruangopini.data.repo.remote.firebase.firestore.user

import id.ruangopini.data.model.User
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface FirestoreUserDataSource {
    fun createNewUser(user: User): Flow<State<Boolean>>
    fun updatePhoto(path: String): Flow<State<Boolean>>
    fun getUserById(userId: String): Flow<State<User>>
    fun getUserByUsername(username: String): Flow<State<User>>
    fun updateUser(user: User): Flow<State<Boolean>>
}