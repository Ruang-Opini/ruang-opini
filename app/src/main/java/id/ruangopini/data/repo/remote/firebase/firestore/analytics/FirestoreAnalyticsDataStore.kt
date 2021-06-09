package id.ruangopini.data.repo.remote.firebase.firestore.analytics

import id.ruangopini.data.model.CategoryAnalytics
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface FirestoreAnalyticsDataStore {
    fun updateDiscussion(category: String): Flow<State<Boolean>>
    fun updateJoinCategory(category: String, isJoin: Boolean): Flow<State<Boolean>>

    fun getAnotherPopular(): Flow<State<List<CategoryAnalytics>>>

    fun updateJoinDiscussion(discussionId: String, isJoin: Boolean): Flow<State<Boolean>>
    fun updateCommentDiscussion(discussionId: String): Flow<State<Boolean>>
    fun updatePostDiscussion(discussionId: String): Flow<State<Boolean>>

    fun updateCommentPost(postId: String): Flow<State<Boolean>>
    fun updateVoteUpPost(postId: String, vote: Int): Flow<State<Boolean>>
    fun updateVoteDownPost(postId: String, vote: Int): Flow<State<Boolean>>

}