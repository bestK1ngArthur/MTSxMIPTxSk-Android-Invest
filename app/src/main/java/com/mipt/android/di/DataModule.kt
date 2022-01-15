package com.mipt.android.di

import android.content.Context
import android.content.SharedPreferences
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.TinkoffRepositoryImpl
import com.mipt.android.data.api.TinkoffAPI
import com.mipt.android.preferences.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    companion object {
        @Provides
        fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences("main", Context.MODE_PRIVATE)

        @Provides
        @Singleton
        fun provideTokenManager(sharedPreferences: SharedPreferences) =
            TokenManager(sharedPreferences)

        @Provides
        @Singleton
        fun provideApi(tokenManager: TokenManager): TinkoffAPI = Retrofit.Builder()
            .baseUrl("https://api-invest.tinkoff.ru/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain: Interceptor.Chain ->
                        val token = tokenManager.getToken()
                        val headerValue = if (token != null) "Bearer $token" else ""
                        val request = chain.request()
                            .newBuilder()
                            .header("Authorization", headerValue)
                            .build()

                        chain.proceed(request)
                    }
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .callTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(
                Json(builderAction = {
                    isLenient = true
                    ignoreUnknownKeys = true
                }).asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun getRepository(api: TinkoffAPI): TinkoffRepository = TinkoffRepositoryImpl(api)
}