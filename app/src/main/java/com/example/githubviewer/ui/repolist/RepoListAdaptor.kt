package com.example.githubviewer.ui.repolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubviewer.databinding.ItemRepoListBinding
import com.example.githubviewer.model.domain.Repo
import io.reactivex.subjects.PublishSubject


class RepoListAdaptor() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val onClickSubject = PublishSubject.create<String>()

    private val diffCallback = object : DiffUtil.ItemCallback<Repo>() {

        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.id == newItem.id
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRepoListBinding.inflate(inflater)

        return RepoViewHolder(binding, onClickSubject)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RepoViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Repo>) {
        differ.submitList(list)
    }

    class RepoViewHolder(
        private val binding: ItemRepoListBinding,
        private val onClickListener: PublishSubject<String>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Repo) {
            with(binding) {
                repo = item
                root.setOnClickListener{ onClickListener.onNext(item.htmlUrl) }
            }
        }
    }
}