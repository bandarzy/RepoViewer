package com.example.githubviewer.model

import com.example.githubviewer.model.domain.Repo
import io.reactivex.Single

interface DataSource {
    fun getRepos(filter: String): Single<List<Repo>>
}