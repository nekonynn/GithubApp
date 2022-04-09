package tech.nekonyan.githubapp.data.network.wrapper

import com.google.gson.annotations.SerializedName
import tech.nekonyan.githubapp.data.model.GithubUser

data class GithubUserResponse(
    @SerializedName("login") override val username: String,
    @SerializedName("avatar_url") override val avatar: String,
): GithubUser(username, avatar)