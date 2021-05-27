package id.ruangopini.domain

import id.ruangopini.data.model.Category
import id.ruangopini.data.repo.IMainRepository
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

class MainInteract(
    private val repository: IMainRepository
) : MainUseCase {
    override suspend fun getAllPolicyCategory(): Flow<State<Category>> =
        repository.getAllPolicyCategory()
}