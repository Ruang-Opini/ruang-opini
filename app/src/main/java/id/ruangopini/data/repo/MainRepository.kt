package id.ruangopini.data.repo

import id.ruangopini.data.model.*
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

    override suspend fun getSentiment(trending: String): Flow<State<Respond>> =
        remoteDataSource.getSentiment(trending)

    override suspend fun getBuzzer(trending: String): Flow<State<Buzzer>> =
        remoteDataSource.getBuzzer(trending)
}