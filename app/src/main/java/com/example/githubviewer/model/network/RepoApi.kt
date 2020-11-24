package com.example.githubviewer.model.network

import com.example.githubviewer.model.network.entities.RepoSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoApi {

    @GET("search/repositories")
    fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sortBy: String = "stars",
        @Query("order") orderBy: String = "desc",
    ) : Single<RepoSearchResponse>

}