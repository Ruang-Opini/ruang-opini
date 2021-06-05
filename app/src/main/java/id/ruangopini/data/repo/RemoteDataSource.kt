package id.ruangopini.data.repo

import id.ruangopini.data.model.*
import id.ruangopini.data.repo.remote.retrofit.ApiService
import id.ruangopini.data.repo.remote.retrofit.BuzzerService
import id.ruangopini.data.repo.remote.retrofit.SentimentService
import id.ruangopini.data.repo.remote.retrofit.TrendingService
import id.ruangopini.data.repo.remote.retrofit.response.toListPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(
    private val apiService: ApiService,
    private val sentimentService: SentimentService,
    private val trendingService: TrendingService,
    private val buzzerService: BuzzerService
) {
    suspend fun getAllPolicyCategory() = flow<State<Category>> {
        emit(State.loading())
        try {
            val response = apiService.getAllPolicyCategory()
            response.category.let {
                val result = if (
                    it?.categoryOfPolicy?.isNotEmpty() == true && it.typeOfPolicy?.isNotEmpty() == true
                ) Category(
                    it.typeOfPolicy.toPolicyCategory(), it.categoryOfPolicy.toPolicyCategory()
                ) else CategoryResponse().let { response ->
                    Category(
                            response.categoryOfPolicy?.toPolicyCategory() ?: listOf(),
                            response.categoryOfPolicy?.toPolicyCategory() ?: listOf()
                        )
                    }
                    emit(State.success(result))
                }
            } catch (e: Exception) {
                emit(State.failed<Category>(e.message.toString()))
            }
        }.flowOn(Dispatchers.IO)

    suspend fun getPolicyByType(url: String) = flow<State<List<Policy>>> {
        emit(State.loading())
        try {
            val response = apiService.getPolicyByType(url)
            response.policy.let {
                val result = if (it?.isNotEmpty() == true) it.toListPolicy() else listOf()
                emit(State.success(result))
            }
        } catch (e: Exception) {
            emit(State.failed<List<Policy>>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getPolicyByCategory(url: String) = flow<State<List<Policy>>> {
        emit(State.loading())
        try {
            val response = apiService.getPolicyByCategory(url)
            response.policy.let {
                val result = if (it?.isNotEmpty() == true) it.toListPolicy() else listOf()
                emit(State.success(result))
            }
        } catch (e: Exception) {
            emit(State.failed<List<Policy>>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getDocumentPolicy(url: String) = flow<State<PolicyDocument>> {
        emit(State.loading())
        try {
            val response = apiService.getDocumentPolicy(url)
            response.documents.let {
                val result =
                    if (it?.isNotEmpty() == true) PolicyDocument(it) else PolicyDocument(listOf())
                emit(State.success(result))
            }
        } catch (e: Exception) {
            emit(State.failed<PolicyDocument>(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getTrending() = flow<State<List<String>>> {
        emit(State.loading())
        val response = trendingService.getTrending()
        response.message.let { message ->
            if (message != null) emit(State.success(listOf(message, response.code.toString())))
            else response.trending.let {
                val result = if (it?.isNotEmpty() == true) it else emptyList()
                emit(State.success(result))
            }
        }
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun getSentiment(trending: String) = flow<State<Respond>> {
        emit(State.loading())
        val response = sentimentService.getOffense(trending)
        response.respond.let {
            if (it != null) emit(State.success(Respond(it.negative ?: 0, it.positive ?: 0)))
        }
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    suspend fun getBuzzer(trending: String) = flow<State<Buzzer>> {
        emit(State.loading())
        val response = buzzerService.getBuzzer(trending)
        response.type.let {
            if (it != null) emit(State.success(Buzzer(it.buzzer ?: 0, it.non ?: 0)))
        }
    }.catch {
        emit(State.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}