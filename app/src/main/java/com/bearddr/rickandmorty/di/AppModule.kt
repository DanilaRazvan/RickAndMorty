package com.bearddr.rickandmorty.di

import com.bearddr.rickandmorty.BuildConfig
import com.bearddr.rickandmorty.data.MainRepository
import com.bearddr.rickandmorty.data.MainRepositoryImpl
import com.bearddr.rickandmorty.data.model.Character
import com.bearddr.rickandmorty.data.remote.RickMortyApi
import com.bearddr.rickandmorty.util.Constants
import com.squareup.moshi.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@ExperimentalStdlibApi
@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .build()

    @Provides
    @Singleton
    fun provideCharacterListAdapter(
        moshi: Moshi
    ): JsonAdapter<List<Character>> {
        val type = Types.newParameterizedType(List::class.java, Character::class.java)
        return moshi.adapter(type)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi,
        BASE_URL: String,
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideRickMortyApi(
        retrofit: Retrofit
    ): RickMortyApi =
        retrofit.create(RickMortyApi::class.java)

    @Provides
    @Singleton
    fun provideMainRepository(
        api: RickMortyApi
    ): MainRepository = MainRepositoryImpl(api)
}