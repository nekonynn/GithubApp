package tech.nekonyan.githubapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import tech.nekonyan.githubapp.data.model.GithubUser

@Entity
data class GithubFollowingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    override val username: String,
    override val avatar: String,
    val sourceUsername: String,
) : GithubUser(username, avatar) {
    constructor(user: GithubUser, username: String) : this(
        0,
        user.username,
        user.avatar,
        username
    )
}