package kr.co.bepo.deliverytracker.di

import android.app.Activity
import kotlinx.coroutines.Dispatchers
import kr.co.bepo.deliverytracker.BuildConfig
import kr.co.bepo.deliverytracker.data.api.SweetTrackerApi
import kr.co.bepo.deliverytracker.data.api.Url
import kr.co.bepo.deliverytracker.data.db.AppDatabase
import kr.co.bepo.deliverytracker.data.entity.TrackingInformation
import kr.co.bepo.deliverytracker.data.entity.TrackingItem
import kr.co.bepo.deliverytracker.data.preference.PreferenceManager
import kr.co.bepo.deliverytracker.data.preference.SharedPreferenceManager
import kr.co.bepo.deliverytracker.data.repository.ShippingCompanyRepository
import kr.co.bepo.deliverytracker.data.repository.ShippingCompanyRepositoryImpl
import kr.co.bepo.deliverytracker.data.repository.TrackingItemRepository
import kr.co.bepo.deliverytracker.data.repository.TrackingItemRepositoryImpl
import kr.co.bepo.deliverytracker.presentation.addtrackingitem.AddTrackingItemFragment
import kr.co.bepo.deliverytracker.presentation.addtrackingitem.AddTrackingItemPresenter
import kr.co.bepo.deliverytracker.presentation.addtrackingitem.AddTrackingItemsContract
import kr.co.bepo.deliverytracker.presentation.trackinghistory.TrackingHistoryContract
import kr.co.bepo.deliverytracker.presentation.trackinghistory.TrackingHistoryFragment
import kr.co.bepo.deliverytracker.presentation.trackinghistory.TrackingHistoryPresenter
import kr.co.bepo.deliverytracker.presentation.trackingitems.TrackingItemsContract
import kr.co.bepo.deliverytracker.presentation.trackingitems.TrackingItemsFragment
import kr.co.bepo.deliverytracker.presentation.trackingitems.TrackingItemsPresenter
import kr.co.bepo.deliverytracker.work.AppWorkerFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val appModule = module {

    single { Dispatchers.IO }

    // Database
    single { AppDatabase.build(androidContext()) }
    single { get<AppDatabase>().trackingItemDao() }
    single { get<AppDatabase>().shippingCompanyDao() }

    // Api
    single {
        OkHttpClient()
            .newBuilder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
                }
            )
            .build()
    }
    single<SweetTrackerApi> {
        Retrofit.Builder().baseUrl(Url.SWEET_TRACKER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create()
    }

    // Preference
    single { androidContext().getSharedPreferences("preference", Activity.MODE_PRIVATE) }
    single<PreferenceManager> { SharedPreferenceManager(get()) }

    // Repository
//    single<TrackingItemRepository> { TrackingItemRepositoryStub() }
    single<TrackingItemRepository> { TrackingItemRepositoryImpl(get(), get(), get()) }
    single<ShippingCompanyRepository> { ShippingCompanyRepositoryImpl(get(), get(), get(), get()) }

    // Work
    single { AppWorkerFactory(get(), get()) }

    // Presentation
    scope<TrackingItemsFragment> {
        scoped<TrackingItemsContract.Presenter> { TrackingItemsPresenter(getSource(), get()) }
    }
    scope<AddTrackingItemFragment> {
        scoped<AddTrackingItemsContract.Presenter> {
            AddTrackingItemPresenter(
                getSource(),
                get(),
                get()
            )
        }
    }
    scope<TrackingHistoryFragment> {
        scoped<TrackingHistoryContract.Presenter> { (trackingItem: TrackingItem, trackingInformation: TrackingInformation) ->
            TrackingHistoryPresenter(getSource(), get(), trackingItem, trackingInformation)
        }
    }

}