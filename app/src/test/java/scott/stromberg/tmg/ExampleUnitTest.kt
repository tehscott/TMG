package scott.stromberg.tmg

import com.nhaarman.mockitokotlin2.mock
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import scott.stromberg.tmg.api.TweetWebService
import scott.stromberg.tmg.api.model.TweetApiResponse
import scott.stromberg.tmg.api.model.TweetData
import scott.stromberg.tmg.ui.main.MainViewModel

class ExampleUnitTest {
    private val mockWebService: TweetWebService = mock {  }
    private val viewModel = MainViewModel(mockWebService)

    @Before
    fun init() {
        `when`(mockWebService.getTweets(eq(""), any())).thenReturn(Single.just(TweetApiResponse(listOf())))
        `when`(mockWebService.getTweets(eq("nasa"), any())).thenReturn(
            Single.just(
                TweetApiResponse(
                    listOf(
                        TweetData(0, "nasa0"),
                        TweetData(1, "nasa1"),
                        TweetData(2, "nasa2"),
                        TweetData(3, "nasa3"),
                    )
                )
            )
        )
    }

    @Test
    fun testEmptySearch() {
        viewModel.doSearch("").test().assertResult(listOf())
    }

    @Test
    fun testNasaSearch() {
        viewModel.doSearch("nasa").test().assertValue { it.isNotEmpty() }
    }
}