package tech.nekonyan.githubapp.util.database

import androidx.room.*
import io.reactivex.Completable

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: T): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(t: List<T>): Completable

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(t: T): Completable

    @Delete
    fun delete(t: T): Completable
}