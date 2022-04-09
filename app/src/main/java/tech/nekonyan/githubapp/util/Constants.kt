package tech.nekonyan.githubapp.util

object Constants {
    const val USERNAME_EXTRA = "USERNAME_EXTRA"
    const val FAVORITE_FLAG_EXTRA = "ONLINE_FLAG_EXTRA"
    const val AVATAR_URL_EXTRA = "AVATAR_URL_EXTRA"

    const val BASE_URL = "https://api.github.com"

    // State
    const val LOADING = "LOADING"
    const val SUCCESS = "SUCCESS"
    const val EMPTY = "EMPTY"
    const val ERROR = "ERROR"

    const val SPLASH_DELAY = 2000L
    const val SEARCH_DELAY = 1000L
    const val DARK_MODE_CHANGE_DELAY = 1000L

    const val DATABASE_NAME = "GithubDatabase"
    const val DATABASE_VERSION = 1
    const val PREFERENCE_NAME = "settings"
    const val PREFERENCE_DARK_MODE = "dark_mode"
}