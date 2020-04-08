package com.eunice.eunicehong.data.remote

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MovieAPI {
    private val service = MovieClient().getClient().create(MovieService::class.java)

    fun getMovieList(
        query: String,
        onSuccess: (movieList: List<Movie>) -> Unit,
        onFailure: (e: Throwable) -> Unit
    ) {
        service.getMovieList(query).enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                val content = response.body()
                if (response.isSuccessful && content != null) {
                    onSuccess(content.items)
                } else {
                    onFailure(Throwable(response.errorBody().toString()))
                }

            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                onFailure(t)
            }
        })
    }

}