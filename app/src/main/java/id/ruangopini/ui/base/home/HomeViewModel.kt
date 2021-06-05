package id.ruangopini.ui.base.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.model.Buzzer
import id.ruangopini.data.model.Respond
import id.ruangopini.data.model.Trend
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.firebase.firestore.issue.FirestoreIssueRepository
import id.ruangopini.domain.MainUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val useCase: MainUseCase,
    private val issueRepository: FirestoreIssueRepository
) : ViewModel() {

    private val currentTrend = mutableListOf<Trend>()
    private val _trending = MutableLiveData<List<Trend>>()
    val trending: LiveData<List<Trend>> get() = _trending

    private fun clearTrending() {
        currentTrend.clear()
        _trending.value = currentTrend
    }

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getTrending() = viewModelScope.launch {
        clearTrending()
        useCase.getTrending().collect {
            when (it) {
                is State.Loading -> _loading.value = true
                is State.Success -> {
                    _loading.value = false

                    it.data.let { data ->
                        data.forEachIndexed { index, name ->
                            currentTrend.add(Trend(name, Respond(0, 0), Buzzer(0, 0)))
                            getSentiment(name, index)
                            getBuzzer(name, index)
                        }
                        _trending.value = currentTrend
                    }
                }
                is State.Failed -> {
                    Log.d("TAG", "getTrending: filed = ${it.message}")
                    getStoredIssue()
                }
            }
        }
    }

    private fun getStoredIssue() = viewModelScope.launch {
        issueRepository.getAllIssue().collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    _loading.value = false

                    it.data.let { data ->
                        data.forEachIndexed { index, issue ->
                            currentTrend.add(Trend(issue.name ?: "", Respond(0, 0), Buzzer(0, 0)))
                            getSentiment(issue.name ?: "", index)
                            getBuzzer(issue.name ?: "", index)
                        }
                        _trending.value = currentTrend
                    }
                }
                is State.Failed -> {
                    Log.d("TAG", "getStoredIssue: filed = ${it.message}")
                    _loading.value = false
                }
            }
        }
    }

    fun getSentiment(trending: String, position: Int) = viewModelScope.launch {
        useCase.getSentiment(trending).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    it.data.let { data ->
                        currentTrend[position].respond = data
                        _trending.value = currentTrend
                    }
                }
                is State.Failed -> {
                    Log.d("TAG", "getSentiment: filed = ${it.message}")
                }
            }
        }
    }

    fun getBuzzer(trending: String, position: Int) = viewModelScope.launch {
        useCase.getBuzzer(trending).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    it.data.let { data ->
                        currentTrend[position].buzzer = data
                        _trending.value = currentTrend
                    }
                }
                is State.Failed -> {
                    Log.d("TAG", "getSentiment: filed = ${it.message}")
                }
            }
        }
    }
}