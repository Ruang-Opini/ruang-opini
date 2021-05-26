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
import id.ruangopini.utils.Helpers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DiscussionViewModel : ViewModel() {
    val repo = FirestoreDiscussionRepository()

    private val _discussion = MutableLiveData<List<Discussion>>()
    val discussion: LiveData<List<Discussion>> get() = _discussion

    @ExperimentalCoroutinesApi
    fun getLatestDiscussion(context: Context) = viewModelScope.launch {
        repo.getLatestDiscussionRoom().collect {
            when (it) {
                is State.Loading -> {
                    // TODO: 5/26/2021 loading list with simmer
                }
                is State.Success -> it.data.let { data ->
                    _discussion.value = if (data.isNotEmpty()) data else listOf()
                }
                is State.Failed -> it.message.let { error ->
                    Helpers.showToast(context, error)
                    Log.d("TAG", "getLatestDiscussion: error = $error")
                }
            }
        }
    }
}