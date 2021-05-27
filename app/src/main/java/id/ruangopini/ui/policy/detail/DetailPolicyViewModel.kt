package id.ruangopini.ui.policy.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.model.Policy
import id.ruangopini.data.repo.State
import id.ruangopini.domain.MainUseCase
import id.ruangopini.utils.Helpers.getUrlPath
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailPolicyViewModel(
    private val useCase: MainUseCase
) : ViewModel() {

    private val dataPolicy = mutableListOf<Policy>()
    private fun updateData() = viewModelScope.launch { _listPolicy.value = dataPolicy }

    private val _listPolicy = MutableLiveData<List<Policy>>()
    val listPolicy: LiveData<List<Policy>> get() = _listPolicy

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getPolicyByType(url: String) = viewModelScope.launch {
        useCase.getPolicyByType(url).collect {
            when (it) {
                is State.Loading -> _isLoading.value = true
                is State.Success -> {
                    _isLoading.value = false
                    it.data.let { data -> _listPolicy.value = data }
                }
                is State.Failed -> {
                    _isLoading.value = false
                    Log.d("TAG", "getPolicyByType: error = ${it.message}")
                }
            }
        }
    }

    fun getPolicyByCategory(url: String) = viewModelScope.launch {
        useCase.getPolicyByCategory(url).collect {
            when (it) {
                is State.Loading -> _isLoading.value = true
                is State.Success -> {
                    _isLoading.value = false
                    it.data.let { data ->
                        dataPolicy.addAll(data)
                        _listPolicy.value = data
                        updateData()
                        loadDocument(data)
                    }
                }
                is State.Failed -> {
                    _isLoading.value = false
                    Log.d("TAG", "getPolicyByCategory: error = ${it.message}")
                }
            }
        }
    }

    private fun loadDocument(data: List<Policy>) = viewModelScope.launch {
        data.forEachIndexed { index, policy ->
            getDocumentPolicy(policy.url.getUrlPath(), index)
        }
    }

    private fun getDocumentPolicy(url: String, index: Int) = viewModelScope.launch {
        useCase.getDocumentPolicy(url).collect {
            when (it) {
                is State.Loading -> {
                }
                is State.Success -> {
                    dataPolicy[index].documents = it.data
                    updateData()
                }
                is State.Failed -> {
                    Log.d("TAG", "getDocumentPolicy: error = ${it.message}")
                }
            }
        }
    }
}