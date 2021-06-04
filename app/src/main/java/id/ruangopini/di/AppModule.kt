package id.ruangopini.di

import id.ruangopini.data.repo.IMainRepository
import id.ruangopini.data.repo.MainRepository
import id.ruangopini.data.repo.RemoteDataSource
import id.ruangopini.data.repo.remote.firebase.auth.AuthRepository
import id.ruangopini.data.repo.remote.firebase.firestore.comment.FirestoreCommentRepository
import id.ruangopini.data.repo.remote.firebase.firestore.discussion.FirestoreDiscussionRepository
import id.ruangopini.data.repo.remote.firebase.firestore.issue.FirestoreIssueRepository
import id.ruangopini.data.repo.remote.firebase.firestore.post.FirestorePostRepository
import id.ruangopini.data.repo.remote.firebase.firestore.user.FirestoreUserRepository
import id.ruangopini.data.repo.remote.firebase.storage.StorageUserRepository
import id.ruangopini.data.repo.remote.retrofit.ApiService
import id.ruangopini.data.repo.remote.retrofit.BuzzerService
import id.ruangopini.data.repo.remote.retrofit.SentimentService
import id.ruangopini.data.repo.remote.retrofit.TrendingService
import id.ruangopini.domain.MainInteract
import id.ruangopini.domain.MainUseCase
import id.ruangopini.ui.base.dicussion.DiscussionViewModel
import id.ruangopini.ui.base.home.HomeViewModel
import id.ruangopini.ui.base.profile.ProfileViewModel
import id.ruangopini.ui.base.reference.ReferenceViewModel
import id.ruangopini.ui.discussion.create.CreateDiscussionViewModel
import id.ruangopini.ui.discussion.detail.DetailDiscussionViewModel
import id.ruangopini.ui.editprofile.EditProfileViewModel
import id.ruangopini.ui.login.LoginViewModel
import id.ruangopini.ui.policy.detail.DetailPolicyViewModel
import id.ruangopini.ui.policy.trending.DetailTrendingPolicyViewModel
import id.ruangopini.ui.post.create.CreatePostViewModel
import id.ruangopini.ui.post.detail.DetailPostViewModel
import id.ruangopini.ui.post.fragment.PostViewModel
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

    single {
        val sentimentRetrofit = Retrofit.Builder()
            .baseUrl("https://offensive-gateway-7q7gq5ib.uc.gateway.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        sentimentRetrofit.create(SentimentService::class.java)
    }

    single {
        val trendingRetrofit = Retrofit.Builder()
            .baseUrl("https://trending-gateway-7q7gq5ib.uc.gateway.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        trendingRetrofit.create(TrendingService::class.java)
    }

    single {
        val buzzerRetrofit = Retrofit.Builder()
            .baseUrl("https://buzzer-gateway-7q7gq5ib.uc.gateway.dev/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        buzzerRetrofit.create(BuzzerService::class.java)
    }
}

val repoModule = module {
    single { RemoteDataSource(get(), get(), get(), get()) }
    single<IMainRepository> { MainRepository(get()) }
}

val useCaseModule = module {
    factory<MainUseCase> { MainInteract(get()) }
    single { FirestoreDiscussionRepository() }
    single { FirestoreUserRepository() }
    single { AuthRepository() }
    single { StorageUserRepository() }
    single { FirestoreIssueRepository() }
    single { FirestorePostRepository() }
    single { FirestoreCommentRepository() }
}

val viewModelModule = module {
    viewModel { ReferenceViewModel(get()) }
    viewModel { DiscussionViewModel(get()) }
    viewModel { CreateAccountViewModel(get(), get()) }
    viewModel { UploadPhotoViewModel(get(), get()) }
    viewModel { DetailPolicyViewModel(get()) }
    viewModel { ChangePasswordViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { CreateDiscussionViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CreatePostViewModel(get(), get(), get()) }
    viewModel { PostViewModel(get()) }
    viewModel { DetailPostViewModel(get(), get()) }
    viewModel { DetailDiscussionViewModel(get()) }
    viewModel { DetailTrendingPolicyViewModel(get()) }
    viewModel { EditProfileViewModel(get(), get()) }
}