package com.example.moviefinderproject.Model

class Movie{

    var movieId: Int = 0
    var movieName: String = ""
    var movieDetail: String = ""
    var moviePoster: String? = ""
    var movieReleaseDate: String = ""
    var movieTitleOriginal: String = ""

    constructor(_movieId: Int, _movieName: String, _movieOriginalName:String, _movieDetail:String, _picture: String?, _movieReleaseDate: String){
        this.movieId = _movieId
        this.movieName=_movieName
        this.movieDetail=_movieDetail
        this.moviePoster=_picture
        this.movieReleaseDate = _movieReleaseDate
        this.movieTitleOriginal=_movieOriginalName
    }

    constructor(){}
}