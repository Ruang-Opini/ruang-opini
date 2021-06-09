package id.ruangopini.data.repo.remote.firebase.firestore.discussion

import id.ruangopini.data.model.Discussion
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface FirestoreDiscussionDataSource {
    fun getLatestDiscussionRoom(): Flow<State<List<Discussion>>>
    fun createNewDiscussion(discussion: Discussion): Flow<State<Boolean>>
    fun joinDiscussion(discussionId: String, userId: String): Flow<State<Boolean>>
    fun leaveDiscussion(discussionId: String, userId: String): Flow<State<Boolean>>
    fun getDiscussionById(discussionId: String): Flow<State<Discussion>>
    fun getDiscussionByIssueName(issueName: String): Flow<State<List<Discussion>>>
}