package id.ruangopini.ui.base.dicussion

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.model.Discussion
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.discussion.FirestoreDiscussionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DiscussionViewModel(
    private val repo: FirestoreDiscussionRepository
) : ViewModel() {

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
}