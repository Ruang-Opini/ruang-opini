package id.ruangopini.data.repo

import id.ruangopini.data.model.*
import kotlinx.coroutines.flow.Flow

interface IMainRepository {
    suspend fun getAllPolicyCategory(): Flow<State<Category>>
    suspend fun getPolicyByType(url: String): Flow<State<List<Policy>>>
    suspend fun getPolicyByCategory(url: String): Flow<State<List<Policy>>>
    suspend fun getDocumentPolicy(url: String): Flow<State<PolicyDocument>>

    suspend fun getTrending(): Flow<State<List<String>>>
    suspend fun getSentiment(trending: String): Flow<State<Respond>>
    suspend fun getBuzzer(trending: String): Flow<State<Buzzer>>
}