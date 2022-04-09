package tech.nekonyan.githubapp.data.model

import tech.nekonyan.githubapp.data.network.wrapper.GithubUserResponse

open class GithubUser(
    open val username: String,
    open val avatar: String,
) {
    constructor(response: GithubUserResponse) : this(response.username, response.avatar)
}