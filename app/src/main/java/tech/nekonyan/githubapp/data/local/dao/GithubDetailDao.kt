package tech.nekonyan.githubapp.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import io.reactivex.Single
import tech.nekonyan.githubapp.data.local.entity.GithubDetailEntity
import tech.nekonyan.githubapp.util.database.BaseDao

@Dao
abstract class GithubDetailDao : BaseDao<GithubDetailEntity> {
    @Query("SELECT * FROM GithubDetailEntity WHERE username LIKE '%' || :query || '%'")
    abstract fun selectByQuery(query: String): Single<List<GithubDetailEntity>>

    @Query("SELECT * FROM GithubDetailEntity")
    abstract fun getAll(): Single<List<GithubDetailEntity>>

    @Query("SELECT * FROM GithubDetailEntity WHERE username = :username LIMIT 1")
    abstract fun get(username: String): Single<GithubDetailEntity>
}