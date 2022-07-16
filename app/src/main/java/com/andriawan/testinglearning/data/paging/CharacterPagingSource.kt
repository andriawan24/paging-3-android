package com.andriawan.testinglearning.data.paging

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andriawan.testinglearning.data.models.CharacterDTO
import com.andriawan.testinglearning.data.remote.ApiService
import com.andriawan.testinglearning.utils.mapper.CharacterMapper
import timber.log.Timber

class CharacterPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, CharacterDTO>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDTO> {
        val pageNumber = params.key ?: 1
        try {
            val response = apiService.getCharacters(pageNumber)
            val data = response.body()
            val characterMapper = CharacterMapper()

            var nextPageNumber: Int? = null
            if (data?.nextPage != null) {
                val uri = Uri.parse(data.nextPage)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery?.toInt()
            }

            return LoadResult.Page(
                data = characterMapper.mapIncoming(data?.data.orEmpty()),
                prevKey = null,
                nextKey = nextPageNumber
            )
        } catch (e: Exception) {
            Timber.e(e)
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}