package com.example.githubviewer.di

import com.example.githubviewer.model.network.BASE_URL
import com.example.githubviewer.model.network.NetworkDataSource
import com.example.githubviewer.model.network.RepoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideEmployeesApi(retrofit: Retrofit) : RepoApi {
        return retrofit.create(RepoApi::class.java)
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(repoApi: RepoApi) : NetworkDataSource {
        return NetworkDataSource(repoApi)
    }
}
