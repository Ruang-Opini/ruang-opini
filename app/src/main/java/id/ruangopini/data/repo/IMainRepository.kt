package id.ruangopini.data.repo

import id.ruangopini.data.model.Category
import kotlinx.coroutines.flow.Flow

interface IMainRepository {
    suspend fun getAllPolicyCategory(): Flow<State<Category>>
}