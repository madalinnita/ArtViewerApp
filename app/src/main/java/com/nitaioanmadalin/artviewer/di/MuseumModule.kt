package com.nitaioanmadalin.artviewer.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nitaioanmadalin.artviewer.core.utils.Constants
import com.nitaioanmadalin.artviewer.core.utils.coroutine.CoroutineDispatchersProvider
import com.nitaioanmadalin.artviewer.core.utils.coroutine.CoroutineDispatchersProviderImpl
import com.nitaioanmadalin.artviewer.core.utils.log.LogProvider
import com.nitaioanmadalin.artviewer.core.utils.log.LogProviderImpl
import com.nitaioanmadalin.artviewer.core.utils.network.ConnectivityUtils
import com.nitaioanmadalin.artviewer.data.local.dao.MuseumDatabase
import com.nitaioanmadalin.artviewer.data.remote.api.MuseumApi
import com.nitaioanmadalin.artviewer.data.repository.MuseumRepositoryImpl
import com.nitaioanmadalin.artviewer.domain.repository.MuseumRepository
import com.nitaioanmadalin.artviewer.domain.usecase.collections.GetCollectionsUseCase
import com.nitaioanmadalin.artviewer.domain.usecase.collections.GetCollectionsUseCaseImpl
import com.nitaioanmadalin.artviewer.data.remote.api.interceptors.TokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  MuseumModule {

    @Provides
    @Singleton
    fun getGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideGetPetsUseCase(
        repository: MuseumRepository
    ): GetCollectionsUseCase {
        return GetCollectionsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor() : TokenInterceptor{
        return TokenInterceptor(
            accessKey = "0fiuZFh4"
        )
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,tokenInterceptor : TokenInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesGithubApi(client: OkHttpClient): MuseumApi {
        return Retrofit
            .Builder()
            .baseUrl(MuseumApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MuseumApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectivityUtils(): ConnectivityUtils {
        return ConnectivityUtils()
    }

    @Provides
    @Singleton
    fun provideCoroutineDispatchersProvider(): CoroutineDispatchersProvider {
        return CoroutineDispatchersProviderImpl()
    }

    @Provides
    @Singleton
    fun provideLogProvider(): LogProvider {
        return LogProviderImpl()
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): MuseumDatabase {
        return Room.databaseBuilder(
            context,
            MuseumDatabase::class.java,
            Constants.MUSEUM_DATABASE
        ).build()
    }

    @Provides
    @Singleton
    fun providePetRepository(
        database: MuseumDatabase,
        api: MuseumApi
    ): MuseumRepository {
        return MuseumRepositoryImpl(api, database)
    }
}