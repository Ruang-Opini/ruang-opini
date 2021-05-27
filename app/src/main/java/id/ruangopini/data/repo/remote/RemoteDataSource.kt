package id.ruangopini.data.repo.remote

import id.ruangopini.data.model.Category
import id.ruangopini.data.model.CategoryResponse
import id.ruangopini.data.model.toPolicyCategory
import id.ruangopini.data.repo.State
import id.ruangopini.data.repo.remote.retrofit.ApiService
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
}