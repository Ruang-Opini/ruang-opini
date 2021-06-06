package id.ruangopini.ui.help

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ruangopini.data.model.ItemHelp
import id.ruangopini.data.repo.remote.firebase.firestore.help.FirestoreHelpRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HelpViewModel(
    private val helpRepository: FirestoreHelpRepository
) : ViewModel() {

    fun onRespond(itemHelp: ItemHelp, isHelp: Boolean) = viewModelScope.launch {
        helpRepository.onRespond(itemHelp.question, isHelp).collect()
    }
}