package scott.stromberg.tmg.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import scott.stromberg.tmg.databinding.MainFragmentBinding
import scott.stromberg.tmg.util.observeTextChange
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var viewBinding: MainFragmentBinding
    private val disposables = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = MainFragmentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        viewBinding.recyclerView.adapter = TweetsAdapter() {
            // TODO: Open tweet in the browser
        }

        disposables.add(
            viewBinding.searchView.observeTextChange()
                .subscribe { query ->
                    doSearch(query)
                }
        )

        viewBinding.searchView.onActionViewExpanded()
    }

    private fun doSearch(query: String) {
        if (query.isNotEmpty()) {
            viewBinding.progressBar.visibility = View.VISIBLE

            disposables.add(
                viewModel.doSearch(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ tweets ->
                        val authorIds = tweets.map { it.authorId }

                        disposables.add(
                            viewModel.getAuthorsByIds(authorIds.joinToString(separator = ","))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnTerminate {
                                    viewBinding.progressBar.visibility = View.GONE
                                }
                                .subscribe({ authors ->
                                    authors.forEach { author ->
                                        tweets.find { tweet -> tweet.authorId == author.id }?.authorName = author.name
                                    }

                                    (viewBinding.recyclerView.adapter as TweetsAdapter).items = tweets
                                }, {
                                    Timber.e(it)
                                })
                        )
                    }, {
                        Timber.e(it)
                    })
            )
        }
        else {
            (viewBinding.recyclerView.adapter as TweetsAdapter).items = listOf()
            viewBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        disposables.clear()
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}