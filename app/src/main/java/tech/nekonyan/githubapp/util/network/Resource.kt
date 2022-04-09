package tech.nekonyan.githubapp.util.network

import tech.nekonyan.githubapp.util.Constants

sealed class Resource<out T>(
    val data: T? = null,
    val state: String? = null,
    val exception: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data = data, state = Constants.SUCCESS)
    class Empty<T> : Resource<T>(state = Constants.EMPTY)
    class Loading<T> : Resource<T>(state = Constants.LOADING)
    class Error<T>(throwable: Throwable?) : Resource<T>(state = Constants.ERROR, exception = throwable)
}