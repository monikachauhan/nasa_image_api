package com.example.astronomicalphotooftheday.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Environment
import com.example.astronomicalphotooftheday.ApodApplication.Companion.context
import com.example.astronomicalphotooftheday.data.remote.HomeApi
import com.example.core.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import feature_home.data.repository.HomeRepositoryImpl
import feature_home.domain.repository.HomeRepository
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideHomeApi(): HomeApi {
        val cacheFile = File(getDiskCacheDir(context,"dir"), "CookiesO")
        val cacheSize = 10 * 1024 * 1024 // 10 MB

        val cache = Cache(cacheFile, cacheSize.toLong())
        val client = OkHttpClient().newBuilder()
            .cache(cache)
            .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
            .addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE)
            .build()
        return  Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(HomeApi::class.java)
       /* return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(HomeApi::class.java)*/
    }
    private val REWRITE_RESPONSE_INTERCEPTOR = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        val cacheControl = originalResponse.header("Cache-Control")
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
            cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")
        ) {
            originalResponse.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + 5000)
                .build()
        } else {
            originalResponse
        }
    }

    private val REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = Interceptor { chain ->
        var request = chain.request()
        if (hasNetwork(context)!=true) {
            request = request.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached")
                .build()
        }
        chain.proceed(request)
    }
    @Provides
    @Singleton
    fun provideHomeRepository(api: HomeApi): HomeRepository {
        return HomeRepositoryImpl(api)
    }
    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
    fun getDiskCacheDir(context: Context, dirName: String): String? {
        var cachePath: String? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val externalCacheDir = context.externalCacheDir
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.path
            }
        }
        if (cachePath == null) {
            cachePath = context.cacheDir.path
        }
        return cachePath + File.separator + dirName
    }
}