package com.example.githubviewer.model.network.entities

import com.google.gson.annotations.SerializedName

data class RepoOwner (

	@SerializedName("login") val login : String,
	@SerializedName("id") val id : Int,
	@SerializedName("node_id") val nodeId : String,
	@SerializedName("avatar_url") val avatarUrl : String,
	@SerializedName("gravatar_id") val gravatarId : String,
	@SerializedName("url") val url : String,
	@SerializedName("received_events_url") val receivedEventsUrl : String,
	@SerializedName("type") val type : String
)