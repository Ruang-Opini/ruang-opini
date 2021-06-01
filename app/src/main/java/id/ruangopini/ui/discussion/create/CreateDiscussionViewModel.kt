package id.ruangopini.ui.discussion.create

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.model.Discussion
import id.ruangopini.data.model.Issue
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.discussion.FirestoreDiscussionRepository
import id.ruangopini.data.repo.remote.firebase.firestore.issue.FirestoreIssueRepository
import id.ruangopini.utils.DialogHelpers
import id.ruangopini.utils.Helpers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CreateDiscussionViewModel(
    private val discussionRepository: FirestoreDiscussionRepository,
    private val issueRepository: FirestoreIssueRepository
) : ViewModel() {

    private var _isComplete = MutableLiveData<Boolean>()
    private val currentProgress = mutableListOf(false, false, false)
    val isComplete: LiveData<Boolean> get() = _isComplete

    fun setProgress(index: Int, state: Boolean) = viewModelScope.launch {
        currentProgress[index] = state
        _isComplete.value = currentProgress.count { it } == currentProgress.size
    }

    fun createNewDiscussion(discussion: Discussion, isPolicyType: Boolean, activity: Activity) =
        viewModelScope.launch {
            if (isPolicyType) issueRepository.findIssue(discussion.issueName ?: "").collect {
                when (it) {
                    is State.Loading -> DialogHelpers.showLoadingDialog(activity)
                    is State.Success -> {
                        it.data.let { issue ->
                            if (issue.name != null) {
                                discussion.issueId = issue.id
                                discussion.issueName = issue.name
                                createDiscussion(discussion, activity)
                            } else createNewIssue(discussion, activity)
                        }
                    }
                    is State.Failed -> {
                        DialogHelpers.hideLoadingDialog()
                        Helpers.showToast(activity, it.message)
                    }
                }
            } else createDiscussion(discussion, activity, true)
        }

    private fun createDiscussion(
        discussion: Discussion,
        activity: Activity,
        isDialogCreated: Boolean? = false
    ) = viewModelScope.launch {
        discussionRepository.createNewDiscussion(discussion).collect {
            when (it) {
                is State.Loading -> {
                    if (isDialogCreated == true) DialogHelpers.showLoadingDialog(activity)
                }
                is State.Success -> {
                    DialogHelpers.hideLoadingDialog()
                    activity.finish()
                }
                is State.Failed -> {
                    DialogHelpers.hideLoadingDialog()
                    Helpers.showToast(activity, it.message)
                }
            }
        }
    }

    private fun createNewIssue(discussion: Discussion, activity: Activity) = viewModelScope.launch {
        issueRepository.createNewIssue(Issue(discussion.issueName)).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    discussion.issueId = it.data
                    createDiscussion(discussion, activity)
                }
                is State.Failed -> {
                    DialogHelpers.hideLoadingDialog()
                    Helpers.showToast(activity, it.message)
                }
            }
        }
    }
}