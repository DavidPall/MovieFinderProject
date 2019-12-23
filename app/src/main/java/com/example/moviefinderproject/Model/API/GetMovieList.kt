package com.example.moviefinderproject.Model.API

import com.example.moviefinderproject.Model.API.Respons
import com.example.moviefinderproject.Model.API.Respons_trailer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GetMovieList {


    @GET("search/movie?api_key=917c3cf13361b8c2a4b0cdcd3024a8b9")
    fun getAllData(@Query("query") user: String): Call<Respons>

    @GET("movie/popular?api_key=917c3cf13361b8c2a4b0cdcd3024a8b9")
    fun getAllPopular(@Query("page") page: Int): Call<Respons>

    @GET("movie/now_playing?api_key=917c3cf13361b8c2a4b0cdcd3024a8b9")
    fun getNowPlaying(@Query("page") page: Int): Call<Respons>

    @GET(" movie/{movie_id}/similar?api_key=917c3cf13361b8c2a4b0cdcd3024a8b9")
    fun getSimilar(@Path("movie_id") id: Int, @Query("page") page: Int): Call<Respons>

    @GET("movie/{movie_id}/videos?api_key=917c3cf13361b8c2a4b0cdcd3024a8b9")
    fun getTrailer(@Path("movie_id") id: Int): Call<Respons_trailer>

}