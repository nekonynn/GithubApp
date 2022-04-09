package tech.nekonyan.githubapp.util.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.nekonyan.githubapp.data.local.dao.GithubDetailDao
import tech.nekonyan.githubapp.data.local.dao.GithubFollowersDao
import tech.nekonyan.githubapp.data.local.dao.GithubFollowingDao
import tech.nekonyan.githubapp.data.local.entity.GithubDetailEntity
import tech.nekonyan.githubapp.data.local.entity.GithubFollowersEntity
import tech.nekonyan.githubapp.data.local.entity.GithubFollowingEntity
import tech.nekonyan.githubapp.util.Constants

@Database(
    entities = [GithubDetailEntity::class, GithubFollowingEntity::class, GithubFollowersEntity::class],
    version = Constants.DATABASE_VERSION,
    exportSchema = false
)
abstract class GithubDatabase : RoomDatabase() {
    companion object {
        private var instance: GithubDatabase? = null

        fun getDatabase(context: Context): GithubDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    GithubDatabase::class.java,
                    Constants.DATABASE_NAME
                ).build()
            }
        }
    }

    abstract fun detailDao(): GithubDetailDao

    abstract fun followingDao(): GithubFollowingDao

    abstract fun followersDao(): GithubFollowersDao
}