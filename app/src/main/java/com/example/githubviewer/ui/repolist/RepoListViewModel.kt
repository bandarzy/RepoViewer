package com.example.githubviewer.ui.repolist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.githubviewer.model.RepoRepository
import com.example.githubviewer.mvibase.MviViewModel
import com.example.githubviewer.ui.repolist.RepoListAction.SearchReposAction
import com.example.githubviewer.ui.repolist.RepoListResult.GetReposResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RepoListViewModel @ViewModelInject constructor(
    private val repository: RepoRepository
) : ViewModel(), MviViewModel<RepoListIntent, RepoListViewState> {

    private val intentsSubject: PublishSubject<RepoListIntent> = PublishSubject.create()
    private val disposables = CompositeDisposable()

    override fun processIntents(intents: Observable<RepoListIntent>) {
        disposables.add(intents.subscribe(intentsSubject::onNext))
    }

    override fun states(): Observable<RepoListViewState> {
        return intentsSubject
            // translate intents to action to separate view actions from business logic
            .map(this::actionFromIntent)
            // handle perform the business logic
            .compose(actionHandler)
            // reduce received business logic result to view state
            .scan(RepoListViewState(), reducer)
            // skip first unwanted emission from scan operator
            .skip(1)
            // go back to android ui thread
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun actionFromIntent(intent: RepoListIntent) = when (intent) {
        is RepoListIntent.SearchReposIntent -> SearchReposAction(intent.query)

        // more intent -> action mappings can be added there when new business logic will be added
        // to the repo list view in the future
    }

    private val actionHandler = ObservableTransformer<RepoListAction, RepoListResult> { actions ->

        actions.publish { shared ->
            Observable.merge(
                // handle all type of actions. When new business logic is added then new actions from
                // repos list will be handled there
                shared.ofType(SearchReposAction::class.java).compose(getReposHandler),

                // emit an error when not expected action is received
                shared
                    .filter { it !is SearchReposAction }
                    .flatMap { Observable.error(IllegalArgumentException("Unknown Action type: $it")) }
            )
        }
    }

    private val getReposHandler =
        ObservableTransformer<SearchReposAction, GetReposResult> { actions ->
            actions.switchMap {
                repository
                    .fetchReposFromNetwork(it.query)
                    .toObservable()
                    .map { repoList -> GetReposResult.Success(repoList) }
                    .cast(GetReposResult::class.java)
                    .onErrorReturn { GetReposResult.Failure(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .startWith(GetReposResult.InProgress)
            }
        }

    private val reducer = BiFunction { previousState: RepoListViewState, result: RepoListResult ->
        when (result) {
            is GetReposResult -> when (result) {
                is GetReposResult.Success -> previousState.copy(
                    isLoading = false,
                    repos = result.repos,
                )
                is GetReposResult.Failure -> previousState.copy(
                    isLoading = false,
                    error = result.error,
                )
                is GetReposResult.InProgress -> previousState.copy(
                    isLoading = true
                )
            }
        }
    }
}