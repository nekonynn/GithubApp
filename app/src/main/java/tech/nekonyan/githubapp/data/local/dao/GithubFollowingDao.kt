package tech.nekonyan.githubapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single
import tech.nekonyan.githubapp.data.local.entity.GithubFollowingEntity
import tech.nekonyan.githubapp.util.database.BaseDao

@Dao
abstract class GithubFollowingDao : BaseDao<GithubFollowingEntity> {
    @Query("SELECT * FROM GithubFollowingEntity WHERE sourceUsername = :username")
    abstract fun get(username: String): Single<List<GithubFollowingEntity>>

    @Query("DELETE FROM GithubFollowingEntity WHERE sourceUsername = :username")
    abstract fun delete(username: String): Completable
}