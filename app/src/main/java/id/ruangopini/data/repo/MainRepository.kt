package id.ruangopini.data.repo

import id.ruangopini.data.model.Category
import id.ruangopini.data.repo.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class MainRepository(
    private val remoteDataSource: RemoteDataSource
) : IMainRepository {
    override suspend fun getAllPolicyCategory(): Flow<State<Category>> =
        remoteDataSource.getAllPolicyCategory()
}