package com.example.githubviewer.model.network.entities

import com.google.gson.annotations.SerializedName

data class RepoSearchResponse (

	@SerializedName("total_count") val totalCount : Int,
	@SerializedName("incomplete_results") val incompleteResults : Boolean,
	@SerializedName("items") val repoList : List<RepoNetworkEntity>
)