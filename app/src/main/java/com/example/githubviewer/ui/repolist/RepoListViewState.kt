package com.example.githubviewer.ui.repolist

import com.example.githubviewer.model.domain.Repo
import com.example.githubviewer.mvibase.MviViewState

data class RepoListViewState (
    val isLoading: Boolean = false,
    val repos: List<Repo> = emptyList(),
    val error: Throwable? = null,
) : MviViewState