package tech.nekonyan.githubapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import tech.nekonyan.githubapp.data.local.entity.GithubFollowersEntity
import tech.nekonyan.githubapp.util.database.BaseDao

@Dao
abstract class GithubFollowersDao : BaseDao<GithubFollowersEntity> {
    @Query("SELECT * FROM GithubFollowersEntity WHERE sourceUsername = :username")
    abstract fun get(username: String): Single<List<GithubFollowersEntity>>

    @Query("DELETE FROM GithubFollowersEntity WHERE sourceUsername = :username")
    abstract fun delete(username: String): Completable
}