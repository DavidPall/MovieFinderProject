package com.example.moviefinderproject.View

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefinderproject.Model.API.GetMovieList
import com.example.moviefinderproject.Model.API.Respons
import com.example.moviefinderproject.Model.API.Respons_trailer
import com.example.moviefinderproject.Model.API.RetrofitMoviesClient
import com.example.moviefinderproject.Model.Movie
import com.example.moviefinderproject.Model.userName
import com.example.moviefinderproject.Presenter.SimilarMovieAdapter
import com.example.moviefinderproject.R
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.detail_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment(private val movie: Movie) : DialogFragment(){

    private lateinit var database: DatabaseReference
    private var isFavorite: Boolean = false

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DetailsTheme)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.detail_fragment, container, false)

        database = FirebaseDatabase.getInstance().reference

        val closeWindow : ImageButton = view.findViewById(R.id.close_btn)
        closeWindow.setOnClickListener { dismiss() }

        val title : TextView = view.findViewById(R.id.titleTextView)
        val release : TextView = view.findViewById(R.id.releaseDateTextView)
        val description : TextView = view.findViewById(R.id.descriptionTextView)
        val poster : ImageView = view.findViewById(R.id.posterImageView)
        val favButton : ImageButton = view.findViewById(R.id.fav_btn)
        val trailer : WebView = view.findViewById(R.id.trailerView)

        title.text = movie.movieName
        release.text = movie.movieReleaseDate
        description.text = movie.movieDetail
        try {
            Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w500/${movie.moviePoster}")
                .into(poster)
        } catch (e: Exception) {
            poster.setImageDrawable(null)
        }

        favButton.setBackgroundResource(R.drawable.ic_favorite_empty)


        val myRef = database.child("Users").child(userName).child("Movies")

        myRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children){
                    val current = ds.getValue(Movie::class.java)
                    if(movie.movieId == current?.movieId){
                        favButton.setBackgroundResource(R.drawable.ic_favorite_full)
                        isFavorite = true
                    }
                }
            }

        })




        favButton.setOnClickListener {
            if(!isFavorite){

                database.child("Users").child(userName).child("Movies").child(movie.movieId.toString()).setValue(movie)
                Toast.makeText(context,"Movie added to the favourites.", Toast.LENGTH_SHORT).show()

                favButton.setBackgroundResource(R.drawable.ic_favorite_full)
                isFavorite = true
            } else {

                database.child("Users").child(userName).child("Movies").child(movie.movieId.toString()).removeValue()
                Toast.makeText(context,"Movie removed from the favourites.", Toast.LENGTH_SHORT).show()

                favButton.setBackgroundResource(R.drawable.ic_favorite_empty)
                isFavorite = false
            }
        }




        Log.i("Movie Name",movie.movieName)

        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getTrailer(movie.movieId)
        dataFlight?.enqueue(object : Callback<Respons_trailer> {
            override fun onFailure(call: Call<Respons_trailer>, t: Throwable) {
                Toast.makeText(context, "Trailer not found.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Respons_trailer>, response: Response<Respons_trailer>) {
                val body = response.body()
                var key: String = ""
                for (element in body!!.results) {
                    key = element.key
                    if (key.isNotEmpty()) {
                        break
                    }
                }
                if (key.isNotEmpty()) {
                    trailer.webViewClient = object : WebViewClient() {}
                    trailer.settings.javaScriptEnabled = true
                    trailer.loadUrl("https://www.youtube.com/watch?v=" + key)
                } else {
                    Toast.makeText(context, "This film don't have trailer.", Toast.LENGTH_LONG).show()
                }

            }

        })


        var similarMovies : ArrayList<Movie> = loadSimilar()

        mRecyclerView = view.findViewById(R.id.similar_RecyclerView)
        mRecyclerView!!.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)

        mAdapter = SimilarMovieAdapter(similarMovies,requireContext())
        mRecyclerView!!.adapter = mAdapter

        return view
    }

    private fun loadSimilar(): ArrayList<Movie> {



        var similarMovies : ArrayList<Movie> = ArrayList()

        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getSimilar(movie.movieId, 1)

        dataFlight?.enqueue(object : Callback<Respons> {
            override fun onFailure(call: Call<Respons>, t: Throwable) {
                Toast.makeText(context, "Error please try again", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Respons>, response: Response<Respons>) {
                val body = response.body()

                for (element in body!!.results) {
                    similarMovies.add(
                        Movie(
                            element.id,
                            element.title,
                            element.original_title,
                            element.overview,
                            element.poster_path,
                            element.release_date
                        )
                    )
                }
            }

        })

        return similarMovies

    }


    override fun onStart() {
        super.onStart()
        if(dialog != null){
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window?.setLayout(width,height)
        }
    }
}