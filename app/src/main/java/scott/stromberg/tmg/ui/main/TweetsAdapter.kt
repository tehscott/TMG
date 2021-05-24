package scott.stromberg.tmg.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import scott.stromberg.tmg.databinding.TweetListItemBinding
import scott.stromberg.tmg.model.Tweet

class TweetsAdapter(val onTweetClicked: (find: Tweet) -> Unit) : RecyclerView.Adapter<TweetsAdapter.TweetViewHolder>() {
    var items = listOf<Tweet>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        return TweetViewHolder(TweetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener { onTweetClicked(items[position]) }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class TweetViewHolder(private val viewBinding: TweetListItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        private var tweet: Tweet? = null

        fun bind(tweet: Tweet) {
            this.tweet = tweet

            viewBinding.userNameText.text = tweet.authorName
            viewBinding.tweetText.text = tweet.text
        }
    }
}