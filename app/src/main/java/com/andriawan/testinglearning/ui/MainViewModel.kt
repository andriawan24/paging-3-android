package com.andriawan.testinglearning.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.andriawan.testinglearning.data.local.AppDatabase
import com.andriawan.testinglearning.data.local.dao.CharacterDAO
import com.andriawan.testinglearning.data.paging.CharacterPagingSource
import com.andriawan.testinglearning.data.paging.CharacterRemoteMediator
import com.andriawan.testinglearning.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

@ExperimentalPagingApi
class MainViewModel(
    private val apiService: ApiService,
    private val characterDAO: CharacterDAO,
    appDatabase: AppDatabase
): ViewModel() {

    val pager = Pager(
        config = PagingConfig(pageSize = 50, prefetchDistance = 2)
    ) {
        CharacterPagingSource(apiService)
    }.flow.cachedIn(viewModelScope)

    val pagerRemote = Pager(
        config = PagingConfig(
            pageSize = 50,
            prefetchDistance = 2
        ),
        remoteMediator = CharacterRemoteMediator(apiService, appDatabase),
        pagingSourceFactory = {
            characterDAO.getCharacters()
        }
    ).flow.cachedIn(viewModelScope)
}