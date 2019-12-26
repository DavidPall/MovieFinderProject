package com.example.moviefinderproject.Presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefinderproject.Model.Movie
import com.example.moviefinderproject.R
import com.example.moviefinderproject.View.DetailFragment
import com.example.moviefinderproject.View.MainActivity
import com.squareup.picasso.Picasso

class SimilarMovieAdapter(var movieList:ArrayList<Movie>, val context: Context) : RecyclerView.Adapter<SimilarMovieAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.similar_movie_card, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = movieList[position].movieName
        holder.date.text = movieList[position].movieReleaseDate
        holder.description.text = movieList[position].movieDetail

        try {
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${movieList[position].moviePoster}")
                .into(holder.poster)
        } catch (e: Exception) {
            holder.poster.setImageDrawable(null)
        }

        holder.item.setOnClickListener {
            val detailsDialogFragment =
                DetailFragment(movieList[position])
            detailsDialogFragment.show((context as MainActivity).supportFragmentManager, "PaginationAdapter")
        }

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var poster : ImageView
        internal var title : TextView
        internal var date : TextView
        internal var description : TextView
        internal var manager = itemView.context
        val item : LinearLayout = itemView.findViewById(R.id.similar_layout)

        init {
            poster = itemView.findViewById(R.id.poster_ImageView) as ImageView
            title = itemView.findViewById(R.id.titleTextView) as TextView
            date = itemView.findViewById(R.id.releaseTextView) as TextView
            description = itemView.findViewById(R.id.descriptionTextView) as TextView
        }


    }

}