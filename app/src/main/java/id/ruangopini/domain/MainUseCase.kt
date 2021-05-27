package id.ruangopini.domain

import id.ruangopini.data.model.Category
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

interface MainUseCase {
    suspend fun getAllPolicyCategory(): Flow<State<Category>>
}