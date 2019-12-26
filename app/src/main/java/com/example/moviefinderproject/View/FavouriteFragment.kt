package com.example.moviefinderproject.View


import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefinderproject.Model.Movie
import com.example.moviefinderproject.Model.userName
import com.example.moviefinderproject.Presenter.FavoriteMovieAdapter
import com.example.moviefinderproject.Presenter.PaginationAdapter
import com.example.moviefinderproject.Presenter.PaginationScroll
import com.example.moviefinderproject.Presenter.SimilarMovieAdapter
import com.example.moviefinderproject.R
import com.google.firebase.database.*

/**
 * Created by VickY on 07-09-2017.
 */
class FavouriteFragment : Fragment(){


    companion object {

        @JvmStatic
        fun newInstance() =
            FavouriteFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }



    private var myAdapter: RecyclerView.Adapter<*>? = null
    private var myRecyclerView: RecyclerView? = null


    override fun onAttach(context: Context) {
        Log.d(TAG,"onAttach")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"onCreateView")

        val rootView = inflater.inflate(R.layout.favourite_fragment,container,false)



        val  favMovies : ArrayList<Movie> = getfavMovies()



        myRecyclerView = rootView.findViewById(R.id.favorite_recyclerView)
        myRecyclerView!!.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.VERTICAL,false)

        myAdapter = FavoriteMovieAdapter(favMovies,requireContext())
        myRecyclerView!!.adapter = myAdapter

        return rootView
    }

    private fun getfavMovies(): ArrayList<Movie> {
        var movies : ArrayList<Movie> = ArrayList()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Users").child(userName).child("Movies")


        myRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                movies.clear()
                for (ds in dataSnapshot.children){
                    val current = ds.getValue(Movie::class.java)
                    if (current != null) {
                        Log.i("Movie: ", current.movieName)
                        movies.add(
                            Movie(
                                current.movieId,
                                current.movieName,
                                current.movieTitleOriginal,
                                current.movieDetail,
                                current.moviePoster,
                                current.movieReleaseDate
                            )
                        )
                        myAdapter?.notifyDataSetChanged()
                    }
                }
            }

        })

        return movies
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG,"onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d(TAG,"onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG,"onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG,"onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG,"onStop")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d(TAG,"onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d(TAG,"onDetach")
        super.onDetach()
    }
}