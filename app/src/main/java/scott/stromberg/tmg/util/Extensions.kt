package scott.stromberg.tmg.util

import androidx.appcompat.widget.SearchView
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun SearchView.observeTextChange(debounceMillis: Long = 300L): Observable<String> {
    return PublishRelay.create<String> { emitter ->
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                emitter.onNext(newText.orEmpty())

                return false
            }
        }

        setOnQueryTextListener(listener)
    }
    .debounce(debounceMillis, TimeUnit.MILLISECONDS)
    .distinctUntilChanged()
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
}