package id.ruangopini.data.repo

import id.ruangopini.data.model.Category
import id.ruangopini.data.model.Policy
import id.ruangopini.data.model.PolicyDocument
import kotlinx.coroutines.flow.Flow

interface IMainRepository {
    suspend fun getAllPolicyCategory(): Flow<State<Category>>
    suspend fun getPolicyByType(url: String): Flow<State<List<Policy>>>
    suspend fun getPolicyByCategory(url: String): Flow<State<List<Policy>>>
    suspend fun getDocumentPolicy(url: String): Flow<State<PolicyDocument>>
}