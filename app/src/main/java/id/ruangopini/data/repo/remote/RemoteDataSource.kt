package id.ruangopini.data.repo.remote

import id.ruangopini.data.model.*
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.retrofit.ApiService
import id.ruangopini.data.repo.remote.retrofit.response.toListPolicy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getAllPolicyCategory(): Flow<State<Category>> =
        flow {
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

    suspend fun getPolicyByType(url: String): Flow<State<List<Policy>>> = flow {
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

    suspend fun getPolicyByCategory(url: String): Flow<State<List<Policy>>> = flow {
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

    suspend fun getDocumentPolicy(url: String): Flow<State<PolicyDocument>> = flow {
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
}