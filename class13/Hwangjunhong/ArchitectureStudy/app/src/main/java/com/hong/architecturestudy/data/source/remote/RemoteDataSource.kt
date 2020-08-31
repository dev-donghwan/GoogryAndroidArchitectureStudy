package com.hong.architecturestudy.data.source.remote

import com.hong.architecturestudy.data.model.MovieData

interface RemoteDataSource {

    fun getMovieList(
        query: String,
        onSuccess: (List<MovieData>) -> Unit,
        onFailure: (Throwable) -> Unit
    )
}