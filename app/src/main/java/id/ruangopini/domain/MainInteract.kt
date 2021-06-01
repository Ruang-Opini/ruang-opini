package id.ruangopini.domain

import id.ruangopini.data.model.Category
import id.ruangopini.data.model.Policy
import id.ruangopini.data.model.PolicyDocument
import id.ruangopini.data.repo.IMainRepository
import id.ruangopini.data.repo.State
import kotlinx.coroutines.flow.Flow

class MainInteract(
    private val repository: IMainRepository
) : MainUseCase {
    override suspend fun getAllPolicyCategory(): Flow<State<Category>> =
        repository.getAllPolicyCategory()

    override suspend fun getPolicyByType(url: String): Flow<State<List<Policy>>> =
        repository.getPolicyByType(url)

    override suspend fun getPolicyByCategory(url: String): Flow<State<List<Policy>>> =
        repository.getPolicyByCategory(url)

    override suspend fun getDocumentPolicy(url: String): Flow<State<PolicyDocument>> =
        repository.getDocumentPolicy(url)

    override suspend fun getTrending(): Flow<State<List<String>>> = repository.getTrending()
}