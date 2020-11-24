package com.example.githubviewer.ui.repolist

import com.example.githubviewer.model.domain.Repo
import com.example.githubviewer.mvibase.MviResult

sealed class RepoListResult: MviResult {

    sealed class GetReposResult : RepoListResult() {
        data class Success(val repos: List<Repo> = emptyList()) : GetReposResult()
        data class Failure(val error: Throwable) : GetReposResult()

        object InProgress : GetReposResult()
    }

}