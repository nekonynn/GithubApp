package tech.nekonyan.githubapp.data.repository

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import tech.nekonyan.githubapp.data.model.GithubDetailUser
import tech.nekonyan.githubapp.data.model.GithubUser
import tech.nekonyan.githubapp.data.network.service.GithubService
import tech.nekonyan.githubapp.util.network.Resource

class GithubRemoteRepository(private val service: GithubService) {
    fun searchUser(query: String): Single<Resource<List<GithubUser>>> {
        return service.searchUser(query).flatMap { response ->
            val data = response.body()
            if (response.code() == 200 && data != null) {
                if (data.items.isNotEmpty()) {
                    return@flatMap Single.just(Resource.Success(data.items))
                } else {
                    return@flatMap Single.just(Resource.Empty())
                }
            } else {
                return@flatMap Single.just(Resource.Error(Exception("Error occurred.")))
            }
        }
    }

    fun getDetailUser(username: String): Single<Resource<GithubDetailUser>> {
        return service.getDetailUser(username).flatMap { response ->
            val data = response.body()
            if (response.code() == 200 && data != null) {
                return@flatMap Single.just(Resource.Success(data.asGithubDetailUser()))
            } else {
                return@flatMap Single.just(Resource.Error(Exception("Error occurred.")))
            }
        }.subscribeOn(Schedulers.io()).onErrorReturn { ex ->
            return@onErrorReturn Resource.Error(ex)
        }
    }

    fun getUserFollowing(username: String): Single<Resource<List<GithubUser>>> {
        return service.getUserFollowing(username).flatMap { response ->
            val data = response.body()
            if (response.code() == 200 && data != null) {
                if (data.isNotEmpty()) {
                    return@flatMap Single.just(Resource.Success(data))
                } else {
                    return@flatMap Single.just(Resource.Empty())
                }
            } else {
                return@flatMap Single.just(Resource.Error(Exception("Error occurred.")))
            }
        }
    }

    fun getUserFollowers(username: String): Single<Resource<List<GithubUser>>> {
        return service.getUserFollowers(username).flatMap { response ->
            val data = response.body()
            if (response.code() == 200 && data != null) {
                if (data.isNotEmpty()) {
                    return@flatMap Single.just(Resource.Success(data))
                } else {
                    return@flatMap Single.just(Resource.Empty())
                }
            } else {
                return@flatMap Single.just(Resource.Error(Exception("Error occurred.")))
            }
        }
    }
}