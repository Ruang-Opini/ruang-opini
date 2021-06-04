package id.ruangopini.data.repo.remote.firebase.firestore.post

import id.ruangopini.data.model.Post
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface FirestorePostDataSource {
    fun createNewPost(post: Post, postId: String): Flow<State<Boolean>>
    fun getPostByDiscussionId(discussionId: String): Flow<State<List<Post>>>
    fun getPostByUserId(userId: String): Flow<State<List<Post>>>
    fun updateVoteUp(vote: Int, postId: String): Flow<State<Boolean>>
    fun updateVoteDown(vote: Int, postId: String): Flow<State<Boolean>>
    fun addToVote(postId: String, userId: String, isUp: Boolean): Flow<State<Boolean>>
    fun remoteFromVote(postId: String, userId: String, isUp: Boolean): Flow<State<Boolean>>
    fun addComment(postId: String): Flow<State<Boolean>>
    fun getPostById(postId: String): Flow<State<Post>>
}