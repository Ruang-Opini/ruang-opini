package id.ruangopini.ui.discussion.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.discussion.FirestoreDiscussionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class DetailDiscussionViewModel(
    private val discussionRepository: FirestoreDiscussionRepository
) : ViewModel() {

    fun joinDiscussion(discussId: String) = viewModelScope.launch {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        discussionRepository.joinDiscussion(discussId, userId).collect()
    }

    fun leaveDiscussion(discussId: String) = viewModelScope.launch {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        discussionRepository.leaveDiscussion(discussId, userId).collect()
    }

    private val _isJoin = MutableLiveData<Boolean>()
    val isJoin: LiveData<Boolean> get() = _isJoin

    fun getDiscussion(discussId: String) = viewModelScope.launch {
        discussionRepository.getDiscussionById(discussId).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    val userId = Firebase.auth.currentUser?.uid ?: ""
                    it.data.let { data ->
                        _isJoin.value =
                            data.members?.find { members -> members == userId }
                                ?.isNotEmpty() ?: false
                    }
                }
                is State.Failed -> {
                }
            }
        }
    }
}