package scott.stromberg.tmg.api.model

import com.squareup.moshi.Json

data class TweetData(
    @Json(name = "id") val id: Long,
    @Json(name = "text") val text: String,
    @Json(name = "author_id") val authorId: String,
    @Json(name = "created_at") val createdAt: String,
)

data class TweetApiResponse(
    @Json(name = "data") val data: List<TweetData>
)
