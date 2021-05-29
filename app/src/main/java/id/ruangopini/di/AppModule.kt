package id.ruangopini.di

import id.ruangopini.data.repo.IMainRepository
import id.ruangopini.data.repo.MainRepository
import id.ruangopini.data.repo.remote.RemoteDataSource
import id.ruangopini.data.repo.remote.firebase.auth.AuthRepository
import id.ruangopini.data.repo.remote.firebase.firestore.discussion.FirestoreDiscussionRepository
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserRepository
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserRepository
import id.ruangopini.data.repo.remote.retrofit.ApiService
import id.ruangopini.domain.MainInteract
import id.ruangopini.domain.MainUseCase
import id.ruangopini.ui.base.dicussion.DiscussionViewModel
import id.ruangopini.ui.base.profile.ProfileViewModel
import id.ruangopini.ui.base.reference.ReferenceViewModel
import id.ruangopini.ui.policy.detail.DetailPolicyViewModel
import id.ruangopini.ui.register.createaccount.CreateAccountViewModel
import id.ruangopini.ui.register.uploadphoto.UploadPhotoViewModel
import id.ruangopini.ui.settings.changepassword.ChangePasswordViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ropin-api-gateway-7q7gq5ib.de.gateway.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repoModule = module {
    single { RemoteDataSource(get()) }
    single<IMainRepository> { MainRepository(get()) }
}

val useCaseModule = module {
    factory<MainUseCase> { MainInteract(get()) }
    single { FirestoreDiscussionRepository() }
    single { FirestoreUserRepository() }
    single { AuthRepository() }
    single { StorageUserRepository() }
}

val viewModelModule = module {
    viewModel { ReferenceViewModel(get()) }
    viewModel { DiscussionViewModel(get()) }
    viewModel { CreateAccountViewModel(get(), get()) }
    viewModel { UploadPhotoViewModel(get(), get()) }
    viewModel { DetailPolicyViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
}