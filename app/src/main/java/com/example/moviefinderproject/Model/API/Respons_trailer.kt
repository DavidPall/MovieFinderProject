package com.example.moviefinderproject.Model.API

import com.google.gson.annotations.SerializedName

data class Respons_trailer(

    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<Result_trailer>
)