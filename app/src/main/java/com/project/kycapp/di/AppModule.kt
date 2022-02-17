package com.project.kycapp.di

import android.content.Context
import com.project.kycapp.data.api.KycApi
import com.project.kycapp.data.datastore.UserPreferences
import com.project.kycapp.data.repository.KycRepositoryImpl
import com.project.kycapp.repository.KycRepository
import com.project.kycapp.utils.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesKycApi(): KycApi {
        val httpLogger = HttpLoggingInterceptor()
            .apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }

        val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLogger)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        return retrofit.create(KycApi::class.java)
    }

    @Singleton
    @Provides
    fun providesKycRepository(
        kycApi: KycApi
    ): KycRepository = KycRepositoryImpl(kycApi)

    @Singleton
    @Provides
    fun providesUserPreferences(@ApplicationContext context: Context) = UserPreferences(context)
}