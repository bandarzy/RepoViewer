package com.example.githubviewer.model.network.util

import com.example.githubviewer.model.domain.Repo
import com.example.githubviewer.model.network.entities.RepoNetworkEntity

class RepoEntityMapper {
    private fun toDomainEntity(networkEntity: RepoNetworkEntity) = Repo (
        id = networkEntity.nodeId,
        name = networkEntity.name,
        description = networkEntity.description?: "",
        htmlUrl = networkEntity.htmlUrl,
        ownerAvatarUrl = networkEntity.owner.avatarUrl
    )

    fun listToDomainEntity(networkEntities: List<RepoNetworkEntity>) =
        networkEntities.map(this::toDomainEntity)
}