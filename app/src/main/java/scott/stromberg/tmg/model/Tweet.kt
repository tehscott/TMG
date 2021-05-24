package scott.stromberg.tmg.model

data class Tweet(
    val id: Long,
    val text: String,
    val authorId: String,
    val createdAt: String,
    var authorName: String? = null
)