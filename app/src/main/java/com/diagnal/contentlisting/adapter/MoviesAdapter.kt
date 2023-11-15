package com.diagnal.contentlisting.adapter

import android.content.Context
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.diagnal.contentlisting.R
import com.diagnal.contentlisting.data.Content
import com.diagnal.contentlisting.utils.ImageUtils

class MoviesAdapter(
    recyclerDataArrayList: ObservableArrayList<Content>,
    searchedText: ObservableField<String>,
    context: Context
) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    private val moviesDataArrayList: ObservableArrayList<Content>
    private val context: Context
    private val searchedText: ObservableField<String>

    init {
        moviesDataArrayList = recyclerDataArrayList
        this.context = context
        this.searchedText = searchedText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.grid_item_view, parent, false)
        return MoviesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie: Content = moviesDataArrayList[position]
        holder.movieTitle.text = movie.name?.let { getSpannableText(it, searchedText.get()) }
        holder.movieImageView.setImageResource(ImageUtils().getImageResource(movie.posterImage))
    }

    private fun getSpannableText(title: String, searchedText: String?): Spannable {

        val spannable = Spannable.Factory.getInstance().newSpannable(title)
        searchedText?.let {
            val index = title.indexOf(searchedText, ignoreCase = true)
            if (index != -1) {
                spannable.setSpan(
                    ForegroundColorSpan(context.getColor(R.color.searchText)),
                    index,
                    index + searchedText.toString().length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return spannable
    }

    override fun getItemCount(): Int {
        return moviesDataArrayList.size
    }

    inner class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitle: TextView
        val movieImageView: ImageView

        init {
            movieTitle = itemView.findViewById(R.id.movie_title)
            movieImageView = itemView.findViewById(R.id.movie_poster)
            movieTitle.isSelected = true // For Marquee title
        }
    }
}
