package id.ruangopini.ui.base.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.repo.State
import id.ruangopini.domain.MainUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(
    private val useCase: MainUseCase
) : ViewModel() {

    private val _trending = MutableLiveData<List<String>>()
    val trending: LiveData<List<String>> get() = _trending

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun getTrending() = viewModelScope.launch {
        useCase.getTrending().collect {
            when (it) {
                is State.Loading -> _loading.value = true
                is State.Success -> {
                    _loading.value = false
                    it.data.let { data -> _trending.value = data }
                }
                is State.Failed -> {
                    Log.d("TAG", "getTrending: filed = ${it.message}")
                    _loading.value = false
                }
            }
        }
    }
}