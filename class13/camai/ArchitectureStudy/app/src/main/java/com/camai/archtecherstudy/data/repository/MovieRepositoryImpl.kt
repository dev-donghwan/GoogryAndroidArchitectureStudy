package com.camai.archtecherstudy.data.repository

import android.content.Context
import com.example.moviesearch.data.model.MovieResponseModel
import com.example.moviesearch.data.source.local.MovieLocalDataSource
import com.example.moviesearch.data.source.local.MovieLocalDataSourceImpl
import com.example.moviesearch.data.source.local.RecentSearchNameList
import com.example.moviesearch.data.source.remote.MovieRemoteDataSourceImpl

object MovieRepositoryImpl: MovieRepository {

    override fun getMovieNameSearch(
        keyword: String,
        display: Int,
        start: Int,
        success: (ArrayList<MovieResponseModel.Items>) -> Unit,
        failed: (String) -> Unit
    ) {
        MovieRemoteDataSourceImpl.getSearchMovie(keyword, display, start, success, failed)
    }

    override fun getRecentSearchList(
        namelist: (List<RecentSearchNameList>) -> Unit,
        context: Context
    ) {
        MovieLocalDataSourceImpl.getRecentMovieNameList(namelist, context)
    }


    override fun setMovieNameInsert(keyword: String, context: Context) {
        MovieLocalDataSourceImpl.saveMovieName(keyword, context)
    }

    override fun deteleDb(context: Context) {
        MovieLocalDataSourceImpl.deleteDb(context)
    }


    override fun dbclose() {
        MovieLocalDataSourceImpl.dbclose()
    }

}