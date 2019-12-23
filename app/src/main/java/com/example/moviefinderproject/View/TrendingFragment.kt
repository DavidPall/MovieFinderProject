package com.example.moviefinderproject.View

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviefinderproject.Model.API.GetMovieList
import com.example.moviefinderproject.Model.API.Respons
import com.example.moviefinderproject.Model.API.RetrofitMoviesClient
import com.example.moviefinderproject.Model.Movie
import com.example.moviefinderproject.Presenter.PaginationAdapter
import com.example.moviefinderproject.Presenter.PaginationScroll
import com.example.moviefinderproject.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by VickY on 07-09-2017.
 */
class TrendingFragment : Fragment(){

    companion object {

        @JvmStatic
        fun newInstance() =
            TrendingFragment().apply {
                arguments = Bundle().apply {
                    // putString(ARG_PARAM1, param1)
                }
            }
    }

    var adapter: PaginationAdapter? = null
    var linearLayoutManager: LinearLayoutManager? = null

    private lateinit var movies: ArrayList<Movie>
    private lateinit var myRecyclreView: RecyclerView


    private val PAGE_START = 1
    private var loading = false
    private var lastPage = false
    private var TOTAL_PAGES = 20
    private var currentPage = PAGE_START



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

        val rootView = inflater.inflate(R.layout.trending_fragment,container,false)

        myRecyclreView = rootView.findViewById(R.id.trending_recyclerView)
        myRecyclreView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        getTrendingMovies()

        return rootView
    }


    private fun getTrendingMovies(){
        loading = false
        lastPage = false
        currentPage = PAGE_START

        adapter = PaginationAdapter(requireContext())

        linearLayoutManager = LinearLayoutManager(context)

        myRecyclreView!!.layoutManager = linearLayoutManager
        myRecyclreView!!.itemAnimator = DefaultItemAnimator()
        myRecyclreView!!.adapter = adapter


        myRecyclreView!!.addOnScrollListener(object : PaginationScroll(linearLayoutManager!!) {
            override fun loadMoreItems() {
                loading = true
                currentPage += 1

                Handler().postDelayed(Runnable { loadNextPage() }, 1000)
            }

            override val totalPageCount: Int
                get() = TOTAL_PAGES
            override val isLastPage: Boolean
                get() = lastPage
            override val isLoading: Boolean
                get() = loading

        })

        loadFirstPage()

    }

    private fun loadFirstPage(){
        movies = ArrayList()


        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getAllTrending(currentPage)
        dataFlight?.enqueue(object : Callback<Respons> {
            override fun onFailure(call: Call<Respons>, t: Throwable) {
                Toast.makeText(context, "Something went wrong, please try later.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Respons>, response: Response<Respons>) {
                val body = response.body()

                for (element in body!!.results) {
                    movies.add(
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


                adapter!!.addAll(movies)

                if (currentPage <= TOTAL_PAGES) {
                    adapter!!.addLoadingFooter()
                } else {
                    lastPage = true
                }
            }

        })
    }

    private fun loadNextPage(){
        movies = ArrayList()



        val service = RetrofitMoviesClient.retrofitInstance?.create(GetMovieList::class.java)
        val dataFlight = service?.getAllTrending(currentPage)
        dataFlight?.enqueue(object : Callback<Respons> {
            override fun onFailure(call: Call<Respons>, t: Throwable) {
                Toast.makeText(context, "Something went wrong, please try later.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Respons>, response: Response<Respons>) {
                val body = response.body()

                for (element in body!!.results) {
                    movies.add(
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

                adapter!!.removeLoadingFooter()
                loading = false
                adapter!!.addAll(movies)

                if (currentPage != TOTAL_PAGES){
                    adapter!!.addLoadingFooter()
                } else {
                    lastPage = true
                }
            }

        })
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