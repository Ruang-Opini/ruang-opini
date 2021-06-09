package id.ruangopini.ui.base.dicussion

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.model.Discussion
import id.ruangopini.data.model.DiscussionAnalytics
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.analytics.FirestoreAnalyticsRepository
import id.ruangopini.data.repo.remote.firebase.firestore.discussion.FirestoreDiscussionRepository
import id.ruangopini.utils.COLLECTION
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DiscussionViewModel(
    private val repo: FirestoreDiscussionRepository,
    private val analyticsRepository: FirestoreAnalyticsRepository
) : ViewModel() {

    private val currentDiscussion = mutableListOf<Discussion>()
    private val currentDiscussionAnalytics = mutableListOf<DiscussionAnalytics>()
    private val _discussion = MutableLiveData<List<Discussion>>()
    val discussion: LiveData<List<Discussion>> get() = _discussion

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    @ExperimentalCoroutinesApi
    fun getLatestDiscussion(context: Context) = viewModelScope.launch {
        repo.getLatestDiscussionRoom().collect {
            when (it) {
                is State.Loading -> _isLoading.value = true
                is State.Success -> it.data.let { data ->
                    _isLoading.value = false
                    _discussion.value = if (data.isNotEmpty()) data else listOf()
                }
                is State.Failed -> it.message.let { error ->
                    _isLoading.value = false
                    Log.d("TAG", "getLatestDiscussion: error = $error")
                }
            }
        }
    }

    fun getPopularDiscussion() = viewModelScope.launch {
        currentDiscussion.clear()
        analyticsRepository.getPopularDiscussion().collect {
            when (it) {
                is State.Loading -> _isLoading.value = true
                is State.Success -> {
                    addDataPopular(it.data)
                }
                is State.Failed -> {
                    _isLoading.value = false
                    Log.d("TAG", "getPopularDiscussion: failed = ${it.message}")
                }
            }
        }
    }

    private fun addDataPopular(data: List<DiscussionAnalytics>) {
        data.forEach { discussion ->
            val existingData =
                currentDiscussionAnalytics.find { it.discussionId == discussion.discussionId }
            val isExist = existingData != null
            if (isExist) {
                currentDiscussionAnalytics[currentDiscussionAnalytics.indexOf(existingData)].apply {
                    this.comment = this.comment?.plus(discussion.comment ?: 0)
                    this.join = this.join?.plus(discussion.join ?: 0)
                    this.post = this.post?.plus(discussion.post ?: 0)
                }
            } else currentDiscussionAnalytics.add(discussion)
        }

        val sorted = currentDiscussionAnalytics.sortedByDescending {
            it.comment?.plus(it.post ?: 0)?.plus(it.join ?: 0)
        }
        getDiscussionById(sorted)
    }

    private fun getDiscussionById(discussion: List<DiscussionAnalytics>) = viewModelScope.launch {
        currentDiscussion.clear()
        discussion.forEach { discus ->
            Firebase.firestore.collection(COLLECTION.DISCUSSION)
                .document(discus.discussionId ?: "")
                .get().addOnCompleteListener {
                    it.result?.toObject(Discussion::class.java)?.let { data ->
                        addDataPopularDiscussion(data, discussion)
                    }
                }
        }
    }

    private fun addDataPopularDiscussion(data: Discussion, discussion: List<DiscussionAnalytics>) {
        val isExist = currentDiscussion.find { it.discussionId == data.discussionId } != null
        if (!isExist) {
            currentDiscussion.add(data)
            val orderById = discussion.withIndex().associate { it.value.discussionId to it.index }
            val sortedDiscussion = currentDiscussion.sortedBy { orderById[it.discussionId] }
            _isLoading.value = false
            _discussion.value = sortedDiscussion
        }
    }

    @ExperimentalCoroutinesApi
    fun getDiscussionByIssueName(issueName: String) = viewModelScope.launch {
        repo.getDiscussionByIssueName(issueName).collect {
            when (it) {
                is State.Loading -> _isLoading.value = true
                is State.Success -> it.data.let { data ->
                    _isLoading.value = false
                    _discussion.value = if (data.isNotEmpty()) data else listOf()
                }
                is State.Failed -> it.message.let { error ->
                    _isLoading.value = false
                    Log.d("TAG", "getLatestDiscussion: error = $error")
                }
            }
        }
    }
}