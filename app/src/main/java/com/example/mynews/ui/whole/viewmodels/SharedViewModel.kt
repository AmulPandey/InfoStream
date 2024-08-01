package com.example.mynews.ui.whole.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynews.Common.dispatcher.DispatcherProvider
import com.example.mynews.data.database.entity.Article
import com.example.mynews.data.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch(dispatcherProvider.io) {
        newsRepository.upsert(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch(dispatcherProvider.io) {
        newsRepository.deleteArticle(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

}