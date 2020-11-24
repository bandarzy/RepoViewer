package com.example.githubviewer.model

import com.example.githubviewer.model.domain.Repo
import com.example.githubviewer.model.network.NetworkDataSource
import io.reactivex.Single
import javax.inject.Inject

class RepoRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource
){
    fun fetchReposFromNetwork(query: String) : Single<List<Repo>> {
        return networkDataSource.getRepos(query)
    }

    // all caching fetched data / retrieving from cache would be added here when needed
}