package com.andriawan.testinglearning.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.andriawan.testinglearning.data.local.AppDatabase
import com.andriawan.testinglearning.data.local.dao.CharacterDAO
import com.andriawan.testinglearning.data.models.CharacterDTO
import com.andriawan.testinglearning.data.models.paging.CharacterRemoteKey
import com.andriawan.testinglearning.data.remote.ApiService
import com.andriawan.testinglearning.utils.mapper.CharacterMapper
import timber.log.Timber

@ExperimentalPagingApi
class CharacterRemoteMediator(
    private val apiService: ApiService,
    private val appDatabase: AppDatabase
) : RemoteMediator<Int, CharacterDTO>() {

    private val characterDAO = appDatabase.characterDao()
    private val characterRemoteKeyDAO = appDatabase.characterRemoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterDTO>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentItem(state)
                    remoteKeys?.nextPage?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKey = getRemoteKeyForFirstItem(state)
                    val prevPage = remoteKey?.prevPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKey != null
                        )
                    prevPage
                }

                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                    val nextPage = remoteKey?.nextPage
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = remoteKey != null
                        )
                    nextPage
                }
            }

            val response = apiService.getCharacters(currentPage)
            val mapper = CharacterMapper()
            val characters = mapper.mapIncoming(response.body()?.data.orEmpty())

            val endOfPagination = characters.isEmpty()

            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPagination) null else currentPage + 1

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    characterDAO.deleteAllCharacters()
                    characterRemoteKeyDAO.deleteAllRemoteKeys()
                }

                val keys = characters.map {
                    CharacterRemoteKey(
                        id = it._id,
                        nextPage = nextPage,
                        prevPage = prevPage
                    )
                }

                characterRemoteKeyDAO.insertRemoteKeys(keys)
                characterDAO.insertCharacters(characters)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentItem(
        state: PagingState<Int, CharacterDTO>
    ): CharacterRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let {
                characterRemoteKeyDAO.getRemoteKey(it._id)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, CharacterDTO>
    ): CharacterRemoteKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { character ->
                characterRemoteKeyDAO.getRemoteKey(character._id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, CharacterDTO>
    ): CharacterRemoteKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { character ->
                characterRemoteKeyDAO.getRemoteKey(character._id)
            }
    }
}