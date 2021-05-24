package scott.stromberg.tmg.ui.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Single
import scott.stromberg.tmg.api.TweetWebService
import scott.stromberg.tmg.model.Tweet
import scott.stromberg.tmg.model.Author
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val webService: TweetWebService
) : ViewModel() {
    fun doSearch(query: String) : Single<List<Tweet>> {
        return webService.getTweets(query, "created_at,author_id")
            .map {
                it.data.map { tweet ->
                    Tweet(
                        tweet.id,
                        tweet.text,
                        tweet.authorId,
                        tweet.createdAt
                    )
                }
            }
    }

    fun getAuthorsByIds(ids: String) : Single<List<Author>> {
        return webService.getAuthorsByIds(ids)
            .map {
                it.data.map { author ->
                    Author(author.id, author.name)
                }
            }
    }
}