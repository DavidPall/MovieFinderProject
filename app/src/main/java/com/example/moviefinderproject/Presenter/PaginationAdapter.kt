package com.example.moviefinderproject.Presenter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefinderproject.Model.Movie
import com.example.moviefinderproject.R
import com.example.moviefinderproject.View.DetailFragment
import com.example.moviefinderproject.View.MainActivity
import com.squareup.picasso.Picasso
import org.w3c.dom.Text


class PaginationAdapter(val context: Context) :
    RecyclerView.Adapter<PaginationAdapter.ViewHolder>() {

    private val ITEM = 0
    private val LOADING = 1
    private var isLoadingAdded = false
    private var movieResults: ArrayList<Movie> = ArrayList()

    fun getMovies(): ArrayList<Movie>? {
        return movieResults
    }

    fun setMovies(movieResults: ArrayList<Movie>) {
        this.movieResults = movieResults

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.movie_card, parent, false)
        )
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1: View = inflater.inflate(R.layout.movie_card, parent, false)
        viewHolder = ViewHolder(v1)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return movieResults?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = movieResults[position].movieName
        holder.releaseDate.text = movieResults[position].movieReleaseDate
        holder.description.text = movieResults[position].movieDetail

        try {
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${movieResults[position].moviePoster}")
                .into(holder.poster)
        } catch (e: Exception) {
            holder.poster.setImageDrawable(null)
        }

        holder.item.setOnClickListener {
            val detailsDialogFragment =
                DetailFragment(movieResults[position])
            detailsDialogFragment.show((context as MainActivity).supportFragmentManager, "PaginationAdapter")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == movieResults!!.size - 1 && isLoadingAdded) LOADING else ITEM
    }

    fun add(r: Movie) {
        this.movieResults.add(r)
        notifyItemInserted(movieResults!!.size - 1)
    }

    fun addAll(moveResults: ArrayList<Movie>) {
        for (result in moveResults) {
            add(result)
        }
    }

    fun remove(r: Movie) {
        val position = movieResults!!.indexOf(r)
        if (position > -1) {
            movieResults.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }


    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Movie())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = movieResults!!.size - 1
        val result = getItem(position)
        if (result != null) {
            movieResults.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Movie {
        return movieResults!![position]
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val releaseDate: TextView = itemView.findViewById(R.id.releaseDateTextView)
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val poster: ImageView = itemView.findViewById(R.id.imageView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val item: ConstraintLayout = itemView.findViewById(R.id.listItem)
    }



}