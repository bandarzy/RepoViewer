package com.example.githubviewer.model.network

import com.example.githubviewer.model.DataSource
import com.example.githubviewer.model.domain.Repo
import com.example.githubviewer.model.network.util.RepoEntityMapper
import io.reactivex.Single
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val repoApi: RepoApi
) : DataSource {

    private val entityMapper = RepoEntityMapper()

    override fun getRepos(filter: String): Single<List<Repo>> {
        return repoApi.searchRepositories(query = filter)
            .map {  searchResponse ->
                entityMapper.listToDomainEntity(searchResponse.repoList)
            }
    }

}