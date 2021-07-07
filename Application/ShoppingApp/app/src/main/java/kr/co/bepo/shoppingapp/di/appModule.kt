package kr.co.bepo.shoppingapp.di

import kotlinx.coroutines.Dispatchers
import kr.co.bepo.shoppingapp.data.entity.product.provideDB
import kr.co.bepo.shoppingapp.data.entity.product.provideToDoDao
import kr.co.bepo.shoppingapp.data.network.buildOkHttpClient
import kr.co.bepo.shoppingapp.data.network.provideGsonConverterFactory
import kr.co.bepo.shoppingapp.data.network.provideProductApiService
import kr.co.bepo.shoppingapp.data.network.provideProductRetrofit
import kr.co.bepo.shoppingapp.data.preference.PreferenceManager
import kr.co.bepo.shoppingapp.data.repository.DefaultProductRepository
import kr.co.bepo.shoppingapp.data.repository.ProductRepository
import kr.co.bepo.shoppingapp.domain.*
import kr.co.bepo.shoppingapp.presentation.detail.ProductDetailViewModel
import kr.co.bepo.shoppingapp.presentation.list.ProductListViewModel
import kr.co.bepo.shoppingapp.presentation.main.MainViewModel
import kr.co.bepo.shoppingapp.presentation.profile.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    // ViewModels
    viewModel { MainViewModel() }
    viewModel { ProductListViewModel(get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { (productId: Long) -> ProductDetailViewModel(productId, get(), get()) }

    // Coroutines Dispatcher
    single { Dispatchers.Main }
    single { Dispatchers.IO }

    // UseCase
    factory { GetProductListUseCase(get()) }
    factory { GetProductItemUseCase(get()) }
    factory { OrderProductItemUseCase(get()) }
    factory { GetOrderedProductListUseCase(get()) }
    factory { DeleteOrderProductListUseCase(get()) }

    // Repositories
    single<ProductRepository> { DefaultProductRepository(get(), get(), get()) }
    single { provideProductApiService(get()) }
    single { provideProductRetrofit(get(), get()) }
    single { provideGsonConverterFactory() }
    single { buildOkHttpClient() }
    single { PreferenceManager(get()) }

    // Database
    single { provideDB(androidApplication()) }
    single { provideToDoDao(get()) }
}