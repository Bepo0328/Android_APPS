package kr.co.bepo.androidonline

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MasterApplication : Application() {
    lateinit var service: RetrofitService

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        createRetrofit()
    }

    fun createRetrofit() {
        val header = Interceptor {
            val original = it.request()
            if (checkIsLogin()) {
                getUserToken().let { token ->
                    val request = original.newBuilder()
                        .header(AUTHORIZATION, "token ${token.orEmpty()}")
                        .build()
                    it.proceed(request)
                }
            } else {
                it.proceed(original)
            }
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(header)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        service = retrofit.create(RetrofitService::class.java)
    }

    fun checkIsLogin(): Boolean {
        return getUserToken() != null
    }

    private fun getUserToken(): String? {
        val sharedPreference = getSharedPreferences("login_sp", Context.MODE_PRIVATE)
        val token = sharedPreference.getString("login_sp", "null")
        return if (token != "null") token
        else null
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val URL = "http://mellowcode.org/"
    }
}