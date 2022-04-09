package tech.nekonyan.githubapp.data.model

import android.os.Parcelable
import android.text.Spannable
import android.text.SpannableStringBuilder
import androidx.core.text.bold
import androidx.core.text.toSpannable
import kotlinx.parcelize.Parcelize

@Parcelize
open class GithubDetailUser(
    override val username: String,
    open val name: String? = "No Name",
    override val avatar: String,
    open val company: String?,
    open val location: String?,
    open val repository: Int,
    open val followers: Int,
    open val following: Int,
    open val bio: String?,
) : GithubUser(username, avatar), Parcelable {
    fun getFollowingFollowers(): Spannable {
        return SpannableStringBuilder()
            .bold {
                append(followers.toString())
            }.append(" followers")
            .append(" \u2022 ")
            .bold {
                append(following.toString())
            }.append(" following")
            .toSpannable()
    }

    fun getRepositories(): Spannable {
        return SpannableStringBuilder()
            .bold {
                append(repository.toString())
            }.append(" repositories")
            .toSpannable()
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + avatar.hashCode()
        result = 31 * result + (company?.hashCode() ?: 0)
        result = 31 * result + (location?.hashCode() ?: 0)
        result = 31 * result + repository
        result = 31 * result + followers
        result = 31 * result + following
        result = 31 * result + (bio?.hashCode() ?: 0)
        return result
    }
}