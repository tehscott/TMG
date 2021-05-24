package scott.stromberg.tmg.api.model

import com.squareup.moshi.Json

data class AuthorData(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String
)

data class AuthorApiResponse(
    @Json(name = "data") val data: List<AuthorData>
)