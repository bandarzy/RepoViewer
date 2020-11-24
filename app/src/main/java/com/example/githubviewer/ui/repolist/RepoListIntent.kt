package com.example.githubviewer.ui.repolist

import com.example.githubviewer.mvibase.MviIntent

sealed class RepoListIntent: MviIntent {
    data class SearchReposIntent(val query: String) : RepoListIntent()

    // more intents could be added in case if requirements for the app would change
    // for example if app should show list of most recent repositories in startup
    // following intents would be added
    // data class InitialIntent()
    // data class FetchIntents(val forceUpdate: boolean)
    // etc.
}