package id.ruangopini.ui.policy.trending

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.model.Buzzer
import id.ruangopini.data.model.Respond
import id.ruangopini.data.repo.State
import id.ruangopini.domain.MainUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailTrendingPolicyViewModel(
    private val useCase: MainUseCase
) : ViewModel() {

    private val _respond = MutableLiveData<Respond>()
    val respond: LiveData<Respond> get() = _respond

    fun getSentiment(trending: String) = viewModelScope.launch {
        useCase.getSentiment(trending).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    it.data.let { data -> _respond.value = data }
                }
                is State.Failed -> {
                    Log.d("TAG", "getSentiment: filed = ${it.message}")
                }
            }
        }
    }

    private val _buzzer = MutableLiveData<Buzzer>()
    val buzzer: LiveData<Buzzer> get() = _buzzer

    fun getBuzzer(trending: String) = viewModelScope.launch {
        useCase.getBuzzer(trending).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    it.data.let { data -> _buzzer.value = data }
                }
                is State.Failed -> {
                    Log.d("TAG", "getSentiment: filed = ${it.message}")
                }
            }
        }
    }
}