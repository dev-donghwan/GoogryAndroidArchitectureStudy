package com.jay.architecturestudy.ui.image

import android.util.Log
import com.jay.architecturestudy.data.database.entity.ImageEntity
import com.jay.architecturestudy.data.model.Image
import com.jay.architecturestudy.data.repository.NaverSearchRepositoryImpl
import com.jay.architecturestudy.ui.BaseSearchPresenter
import com.jay.architecturestudy.util.addTo
import com.jay.architecturestudy.util.singleIoMainThread
import com.jay.architecturestudy.util.then
import io.reactivex.Single

class ImagePresenter(
    override val view: ImageContract.View,
    override val repository: NaverSearchRepositoryImpl
) : BaseSearchPresenter(view, repository), ImageContract.Presenter {

    override fun subscribe() {
        val lastKeyword = repository.getLatestImageKeyword()
        lastKeyword.isNotBlank().then {
            repository.getLatestImageResult()
                .compose(singleIoMainThread())
                .subscribe({
                    view.updateUi(lastKeyword, it)
                }, { e ->
                    val message = e.message ?: return@subscribe
                    Log.e("image", message)
                })
                .addTo(disposables)
        }
    }

    private fun loadImageSearchHistory(keyword: String) : Single<Pair<String, List<Image>>> {
        return if (keyword.isBlank()) {
            Single.just(Pair(keyword, emptyList()))
        } else {
            repository.getLatestImageResult()
                .map { Pair(keyword, it) }
                .compose(singleIoMainThread())
        }
    }

    override fun search(keyword: String) {
        repository.getImage(
            keyword = keyword
        )
            .map {
                // 기존 결과 삭제
                clearSearchHistory { repository.clearImageResult() }
                it.images.isNotEmpty().then {
                    val imageList = arrayListOf<ImageEntity>()
                    it.images.mapTo(imageList) { image ->
                        ImageEntity(
                           link = image.link,
                            sizeWidth = image.sizeWidth,
                            sizeHeight = image.sizeHeight,
                            thumbnail = image.thumbnail,
                            title = image.title
                        )
                    }
                    // 최신 결과 저장
                    updateSearchHistory { repository.saveImageResult(imageList) }
                }
                repository.saveImageKeyword(keyword)
                it.images
            }
            .compose(singleIoMainThread())
            .subscribe({ images ->
                if (images.isEmpty()) {
                    view.hideResultListView()
                    view.showEmptyResultView()
                } else {
                    view.hideEmptyResultView()
                    view.showResultListView()
                }
                view.updateResult(images)
            }, { e ->
                handleError(e)
            })
            .addTo(disposables)
    }

}