package tech.nekonyan.githubapp.data.network.wrapper

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import tech.nekonyan.githubapp.data.model.GithubDetailUser

@Parcelize
data class GithubDetailResponse(
    @SerializedName("login") val username: String,
    val name: String?,
    @SerializedName("avatar_url") val avatar: String,
    val company: String?,
    val location: String?,
    @SerializedName("public_repos") val repository: Int,
    val followers: Int,
    val following: Int,
    val bio: String?,
) : Parcelable {
    fun asGithubDetailUser(): GithubDetailUser {
        return GithubDetailUser(
            username, name, avatar, company, location, repository, followers, following, bio
        )
    }
}