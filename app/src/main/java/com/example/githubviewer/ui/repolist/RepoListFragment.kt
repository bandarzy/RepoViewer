package com.example.githubviewer.ui.repolist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubviewer.R
import com.example.githubviewer.mvibase.MviView
import com.example.githubviewer.util.formatQuery
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_repo_list.*
import java.util.concurrent.TimeUnit

private const val TAG = "RepoListFragment"
// amount of time that has to pass after last character was typed in search bar until the request
// to network is sent. It's a way to prevent sending unnecessary request after each character type.
const val REQUEST_DELAY = 300L

@AndroidEntryPoint
class RepoListFragment : Fragment(), MviView<RepoListIntent, RepoListViewState> {

    private val disposables = CompositeDisposable()
    private val viewModel : RepoListViewModel by viewModels()
    private val recyclerAdapter = RepoListAdaptor()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_repo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        bindWithViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()

        disposables.dispose()
    }

    private fun bindWithViewModel() {
        // pass view intents observable to viewModel
        viewModel.processIntents(intents())

        // subscribe to viewState observable from viewModel
        disposables.add(viewModel.states().subscribe(this::render))
    }

    override fun intents(): Observable<RepoListIntent> {
        return initSearchObservable().cast(RepoListIntent::class.java)

        // place where all observables would be merged in case when new logic would be added to this view
        // For example
        // Observable.merge(initSearchObservable(), initPullToRefreshObservable())
    }

    override fun render(state: RepoListViewState) {

        repo_message_tv.visibility = View.GONE

        recyclerAdapter.submitList(state.repos)

        if(state.repos.isEmpty() && !state.isLoading) {
            repo_message_tv.visibility = View.VISIBLE
        }

        repo_search_progress_bar.apply {
            visibility = if (state.isLoading) View.VISIBLE else View.GONE
        }

        state.error?.apply {
            message?.let {
                Snackbar.make(repo_search_list_recycler, it, Snackbar.LENGTH_LONG).show()
            };
        }
    }

    private fun setupRecyclerView() {
        repo_search_list_recycler.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = recyclerAdapter
        }

        disposables.add(recyclerAdapter.onClickSubject.subscribe(this::openRepoInBrowser))
    }

    private fun initSearchObservable() : Observable<RepoListIntent.SearchReposIntent> {
        return Observable.create<RepoListIntent.SearchReposIntent>{ emitter ->
            repo_search_et.doOnTextChanged { text, _, _, _ ->
                if (text?.length!! > 0) {
                    emitter.onNext(RepoListIntent.SearchReposIntent(formatQuery(text.toString())))
                }
            }
        }.debounce(REQUEST_DELAY, TimeUnit.MILLISECONDS)
    }

    private fun openRepoInBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        ContextCompat.startActivity(requireContext(), intent, null)
    }
}