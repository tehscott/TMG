package scott.stromberg.tmg.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import scott.stromberg.tmg.api.model.TweetApiResponse
import scott.stromberg.tmg.api.model.AuthorApiResponse


interface TweetWebService {
    @Headers("Authorization: Bearer $bearerToken")
    @GET("tweets/search/recent")
    fun getTweets(
        @Query("query") query: String,
        @Query("tweet.fields") fields: String
    ) : Single<TweetApiResponse>

    @Headers("Authorization: Bearer $bearerToken")
    @GET("users")
    fun getAuthorsByIds(
        @Query("ids") authorIds: String
    ) : Single<AuthorApiResponse>

    companion object {
        const val bearerToken = "AAAAAAAAAAAAAAAAAAAAAFS2PwEAAAAAOZcO%2BIKsUXr99r0PKPnti8v3QNU%3DVGUP5Ly22jPoQHGGMTT2NZqX3QMPKIL01RXpOhUgOZn4XhzXtW"

        fun createWebService(): TweetWebService {
            return Retrofit.Builder()
                .baseUrl("https://api.twitter.com/2/")
                .addConverterFactory(
                    MoshiConverterFactory.create(
                        Moshi.Builder().add(
                            KotlinJsonAdapterFactory()
                        ).build()
                    )
                )
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TweetWebService::class.java)
        }
    }
}