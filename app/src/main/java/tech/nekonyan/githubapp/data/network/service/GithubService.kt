package tech.nekonyan.githubapp.data.network.service

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tech.nekonyan.githubapp.data.network.wrapper.GithubDetailResponse
import tech.nekonyan.githubapp.data.network.wrapper.GithubSearchResponse
import tech.nekonyan.githubapp.data.network.wrapper.GithubUserResponse
import tech.nekonyan.githubapp.util.network.ApiEndPoint

interface GithubService {
    @GET(ApiEndPoint.SEARCH_USER)
    fun searchUser(@Query("q") query: String): Single<Response<GithubSearchResponse>>

    @GET(ApiEndPoint.DETAIL_USER)
    fun getDetailUser(@Path("username") username: String): Single<Response<GithubDetailResponse>>

    @GET(ApiEndPoint.LIST_FOLLOWING)
    fun getUserFollowing(@Path("username") username: String): Single<Response<List<GithubUserResponse>>>

    @GET(ApiEndPoint.LIST_FOLLOWERS)
    fun getUserFollowers(@Path("username") username: String): Single<Response<List<GithubUserResponse>>>
}