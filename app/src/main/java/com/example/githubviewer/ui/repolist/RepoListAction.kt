package com.example.githubviewer.ui.repolist

import com.example.githubviewer.mvibase.MviAction

sealed class RepoListAction : MviAction {
    data class SearchReposAction(val query: String) : RepoListAction()
}