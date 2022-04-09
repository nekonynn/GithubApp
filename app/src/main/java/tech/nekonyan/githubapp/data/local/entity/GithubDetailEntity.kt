package tech.nekonyan.githubapp.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import tech.nekonyan.githubapp.data.model.GithubDetailUser

@Entity
@Parcelize
data class GithubDetailEntity(
    @PrimaryKey
    override val username: String,
    override val name: String?,
    override val avatar: String,
    override val company: String?,
    override val location: String?,
    override val repository: Int,
    override val followers: Int,
    override val following: Int,
    override val bio: String?,
) : GithubDetailUser(
    username, name, avatar,
    company, location, repository,
    followers, following, bio
), Parcelable {
    constructor(data: GithubDetailUser) : this(
        data.username, data.name, data.avatar,
        data.company, data.location, data.repository,
        data.followers, data.following, data.bio
    )
}