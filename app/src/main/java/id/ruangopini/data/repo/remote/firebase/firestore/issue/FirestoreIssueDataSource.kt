package id.ruangopini.data.repo.remote.firebase.firestore.issue

import id.ruangopini.data.model.Issue
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface FirestoreIssueDataSource {
    fun findIssue(name: String): Flow<State<Issue>>
    fun createNewIssue(issue: Issue): Flow<State<String>>
    fun getAllIssue(): Flow<State<List<Issue>>>
}