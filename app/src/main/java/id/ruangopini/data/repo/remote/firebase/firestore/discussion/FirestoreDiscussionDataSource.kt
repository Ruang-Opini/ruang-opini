package id.ruangopini.data.repo.remote.firebase.firestore.discussion

import id.ruangopini.data.model.Discussion
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface FirestoreDiscussionDataSource {
    fun getLatestDiscussionRoom(): Flow<State<List<Discussion>>>
    // TODO: 5/26/2021 get trending discussion
}