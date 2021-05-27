package id.ruangopini.ui.base.reference

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.model.Category
import id.ruangopini.data.repo.State
import id.ruangopini.domain.MainUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReferenceViewModel(private val useCase: MainUseCase) : ViewModel() {

    private val _policyCategory = MutableLiveData<Category>()
    val policyCategory: LiveData<Category> get() = _policyCategory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getAllPolicyCategory() = viewModelScope.launch {
        useCase.getAllPolicyCategory().collect {
            when (it) {
                is State.Loading -> _isLoading.value = true
                is State.Success -> it.data.let { data ->
                    val listOfType = if (data.listType.isNotEmpty()) data.listType else listOf()
                    val listOfCategory =
                        if (data.listCategory.isNotEmpty()) data.listCategory else listOf()
                    _isLoading.value = false
                    _policyCategory.value = Category(listOfType, listOfCategory)
                }
                is State.Failed -> it.message.let { error ->
                    _isLoading.value = false
                    Log.d("TAG", "getLatestDiscussion: error = $error")
                }
            }
        }
    }
}