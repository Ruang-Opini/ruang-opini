package id.ruangopini.data.repo.remote.firebase.firestore.comment

import id.ruangopini.data.model.Comment
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface FirestoreCommentDataSource {
    fun createComment(comment: Comment): Flow<State<Boolean>>
    fun loadComment(postId: String): Flow<State<List<Comment>>>
}