package id.ruangopini.data.repo

import id.ruangopini.data.model.Category
import id.ruangopini.data.model.Policy
import id.ruangopini.data.model.PolicyDocument
import kotlinx.coroutines.flow.Flow

class MainRepository(
    private val remoteDataSource: RemoteDataSource
) : IMainRepository {
    override suspend fun getAllPolicyCategory(): Flow<State<Category>> =
        remoteDataSource.getAllPolicyCategory()

    override suspend fun getPolicyByType(url: String): Flow<State<List<Policy>>> =
        remoteDataSource.getPolicyByType(url)

    override suspend fun getPolicyByCategory(url: String): Flow<State<List<Policy>>> =
        remoteDataSource.getPolicyByCategory(url)

    override suspend fun getDocumentPolicy(url: String): Flow<State<PolicyDocument>> =
        remoteDataSource.getDocumentPolicy(url)

    override suspend fun getTrending(): Flow<State<List<String>>> =
        remoteDataSource.getTrending()
}