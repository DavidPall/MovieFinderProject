package com.example.moviefinderproject.Model

class User(val username: String?, val password: String?){
    constructor() : this("no username","no password")
}