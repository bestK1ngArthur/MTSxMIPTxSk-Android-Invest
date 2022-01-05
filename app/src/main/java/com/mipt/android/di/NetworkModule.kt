package com.mipt.android.di

import com.mipt.android.data.TinkoffRepository
import com.mipt.android.data.TinkoffRepositoryImpl
import com.mipt.android.data.api.TinkoffAPI

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    companion object {
        @Provides
        @Singleton
        fun provideApi(tokenManager: TokenManager): TinkoffAPI = Retrofit.Builder()
            .baseUrl("https://api-invest.tinkoff.ru/openapi/sandbox/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain: Interceptor.Chain ->
                        val request = chain.request()
                            .newBuilder()
                            .header("Authorization",
                                tokenManager.getToken()?.map { "Bearer $it" }.toString()
                            )
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

    @Binds
    @Singleton
    abstract fun getRepository(repositoryImpl: TinkoffRepositoryImpl): TinkoffRepository
}