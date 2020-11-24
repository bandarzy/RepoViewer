package com.example.githubviewer.model.domain

data class Repo (
    val id: String,
    val name: String,
    val description: String,
    val htmlUrl: String,

    val ownerAvatarUrl: String
)