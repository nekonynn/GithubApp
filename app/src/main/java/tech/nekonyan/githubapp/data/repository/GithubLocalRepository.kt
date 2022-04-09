package tech.nekonyan.githubapp.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import tech.nekonyan.githubapp.data.local.entity.GithubDetailEntity
import tech.nekonyan.githubapp.data.local.entity.GithubFollowersEntity
import tech.nekonyan.githubapp.data.local.entity.GithubFollowingEntity
import tech.nekonyan.githubapp.data.model.GithubDetailUser
import tech.nekonyan.githubapp.data.model.GithubUser
import tech.nekonyan.githubapp.util.database.GithubDatabase
import tech.nekonyan.githubapp.util.network.Resource

class GithubLocalRepository(private val database: GithubDatabase) {
    fun searchUser(query: String): Single<Resource<List<GithubUser>>> {
        return if (query.isBlank()) {
            selectAll()
        } else {
            selectByQuery(query)
        }
    }

    private fun selectAll(): Single<Resource<List<GithubUser>>> {
        return database.detailDao().getAll().flatMap { data ->
            if (data.isNotEmpty()) {
                return@flatMap Single.just(Resource.Success(data))
            } else {
                return@flatMap Single.just(Resource.Empty())
            }
        }
    }

    private fun selectByQuery(query: String): Single<Resource<List<GithubUser>>> {
        return database.detailDao().selectByQuery(query).flatMap { data ->
            if (data.isNotEmpty()) {
                return@flatMap Single.just(Resource.Success(data))
            } else {
                return@flatMap Single.just(Resource.Empty())
            }
        }
    }

    fun getDetailUser(username: String): Single<Resource<GithubDetailUser>> {
        return database.detailDao().get(username).flatMap { data ->
            return@flatMap Single.just(Resource.Success(data))
        }
    }

    fun getUserFollowing(username: String): Single<Resource<List<GithubUser>>> {
        return database.followingDao().get(username).flatMap { data ->
            if (data.isNotEmpty()) {
                return@flatMap Single.just(Resource.Success(data))
            } else {
                return@flatMap Single.just(Resource.Empty())
            }
        }
    }

    fun getUserFollowers(username: String): Single<Resource<List<GithubUser>>> {
        return database.followersDao().get(username).flatMap { data ->
            if (data.isNotEmpty()) {
                return@flatMap Single.just(Resource.Success(data))
            } else {
                return@flatMap Single.just(Resource.Empty())
            }
        }
    }

    fun toggleFavorite(
        data: GithubDetailEntity,
        followingUser: List<GithubFollowingEntity>,
        followersUser: List<GithubFollowersEntity>,
        isFavorite: Boolean
    ): Completable {
        return if (!isFavorite) {
            Completable.mergeArray(
                database.detailDao().insert(data),
                database.followingDao().insert(followingUser),
                database.followersDao().insert(followersUser)
            )
        } else {
            Completable.mergeArray(
                database.detailDao().delete(data),
                database.followingDao().delete(data.username),
                database.followersDao().delete(data.username)
            )
        }
    }
}