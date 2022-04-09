package tech.nekonyan.githubapp.util.network

object ApiEndPoint {
    const val SEARCH_USER = "search/users"
    const val DETAIL_USER = "users/{username}"
    const val LIST_FOLLOWERS = "users/{username}/followers"
    const val LIST_FOLLOWING = "users/{username}/following"
}