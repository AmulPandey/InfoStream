package com.example.mynews.ui.whole.viewmodels.filters


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mynews.Common.NoInternetException
import com.example.mynews.Common.dispatcher.DispatcherProvider
import com.example.mynews.Common.networkhelper.NetworkHelper
import com.example.mynews.data.model.Country
import com.example.mynews.data.repository.NewsRepository
import com.example.mynews.ui.whole.base.UIState

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryFilterViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val dispatcherProvider: DispatcherProvider,
    private val networkHelper: NetworkHelper
) :
    ViewModel() {


    private val _countryItem = MutableStateFlow<UIState<List<Country>>>(UIState.Empty)
    val countryItem: StateFlow<UIState<List<Country>>> = _countryItem

    init {
        getCountries()
    }

    fun getCountries() {
        viewModelScope.launch {
            if (!networkHelper.isNetworkConnected()) {
                _countryItem.emit(
                    UIState.Failure(
                        throwable = NoInternetException()
                    )
                )
                return@launch
            }
            _countryItem.emit(UIState.Loading)
            newsRepository.getCountries()
                .flowOn(dispatcherProvider.io)
                .catch {
                    _countryItem.emit(UIState.Failure(it))
                }
                .collect {
                    _countryItem.emit(UIState.Success(it))
                }
        }
    }
}