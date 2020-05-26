package com.example.architecture.activity.search

import android.util.Log
import android.view.inputmethod.EditorInfo
import com.example.architecture.R
import com.example.architecture.data.model.MovieModel
import com.example.architecture.data.repository.NaverRepository

class SearchPresenter(
    private val view: SearchContract.View,
    private val naverRepository: NaverRepository
) : SearchContract.Presenter {

    override fun searchMovie(keyword: String) {
        if (isValidKeyword(keyword)) {
            naverRepository.getMovieList(keyword, this::onSuccess, this::onFailure)
        }
    }

    override fun searchMovie(actionId: Int, keyword: String): Boolean {
        return if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            searchMovie(keyword)
            true
        } else {
            false
        }
    }

    private fun onSuccess(movieList: List<MovieModel>) {
        if (movieList.isNotEmpty()) {
            view.showMovieList(movieList)
        } else {
            view.showMessageEmptyResult()
        }
    }

    private fun onFailure(t: Throwable) {
        Log.d("chul", "OnFailure : $t")
    }

    override fun isValidKeyword(keyword: String): Boolean {

        return if (keyword.isBlank()) {
            view.showMessageEmptyKeyword()
            return false
        }
        else {
            true
        }
    }

    override fun clearLocalData(keyword: String) {
        naverRepository.clearCacheData()
    }

}

