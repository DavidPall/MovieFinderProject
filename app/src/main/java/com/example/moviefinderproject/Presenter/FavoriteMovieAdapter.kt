package com.example.moviefinderproject.Presenter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefinderproject.Model.Movie
import com.example.moviefinderproject.R
import com.example.moviefinderproject.View.DetailFragment
import com.example.moviefinderproject.View.MainActivity
import com.squareup.picasso.Picasso

class FavoriteMovieAdapter(var movieList:ArrayList<Movie>, val context: Context) : RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = movieList[position].movieName
        holder.releaseDate.text = movieList[position].movieReleaseDate
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_card, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var releaseDate: TextView = view.findViewById(R.id.releaseDateTextView)
        internal var title: TextView
        internal var poster: ImageView
        internal var description: TextView
        internal var item: ConstraintLayout

        init{
            title = view.findViewById(R.id.titleTextView)
            poster = view.findViewById(R.id.imageView)
            description = view.findViewById(R.id.descriptionTextView)
            item = view.findViewById(R.id.listItem)
        }

    }
}


