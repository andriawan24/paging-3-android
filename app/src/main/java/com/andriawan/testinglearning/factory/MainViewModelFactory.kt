package com.andriawan.testinglearning.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.andriawan.testinglearning.data.local.AppDatabase
import com.andriawan.testinglearning.data.local.dao.CharacterDAO
import com.andriawan.testinglearning.data.remote.ApiService
import com.andriawan.testinglearning.ui.MainViewModel
import java.lang.IllegalArgumentException

@ExperimentalPagingApi
class MainViewModelFactory(
    private val apiService: ApiService,
    private val characterDAO: CharacterDAO,
    private val appDatabase: AppDatabase
): ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(apiService, characterDAO, appDatabase) as T
        }

        throw IllegalArgumentException("Class is not assignable from view model")
    }
}