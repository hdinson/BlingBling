package dinson.customview.api

import dinson.customview.entity.github.RepoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("https://api.github.com/search/repositories?sort=stars&q=Android")
    suspend fun searchRepos(@Query("page") page: Int, @Query("per_page") perPage: Int): RepoResponse
}