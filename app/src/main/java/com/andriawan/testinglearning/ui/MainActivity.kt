package com.andriawan.testinglearning.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.andriawan.testinglearning.adapter.CharacterAdapter
import com.andriawan.testinglearning.data.local.AppDatabase
import com.andriawan.testinglearning.data.local.dao.CharacterDAO
import com.andriawan.testinglearning.data.remote.ApiService
import com.andriawan.testinglearning.databinding.ActivityMainBinding
import com.andriawan.testinglearning.factory.MainViewModelFactory
import com.andriawan.testinglearning.utils.Constants.BASE_URL
import com.andriawan.testinglearning.utils.Constants.DEFAULT_TIMEOUT
import com.andriawan.testinglearning.utils.PagingLoadStateAdapter
import com.andriawan.testinglearning.utils.ext.decideOnState
import com.andriawan.testinglearning.utils.interceptor.NetworkConnectionInterceptor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private lateinit var adapter: CharacterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory = generateViewModelFactory()
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupAdapter()
    }

    private fun setupAdapter() {
        adapter = CharacterAdapter()
        with(adapter) {
            val pagingAdapter = withLoadStateHeaderAndFooter(
                header = PagingLoadStateAdapter(this),
                footer = PagingLoadStateAdapter(this)
            )
            binding?.characterRecyclerView?.adapter = pagingAdapter
            binding?.characterRecyclerView?.layoutManager = LinearLayoutManager(this@MainActivity)
            binding?.characterRecyclerView?.apply {
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }

            binding?.refreshLayout?.setOnRefreshListener {
                adapter.refresh()
                binding?.refreshLayout?.isRefreshing = false
            }

            binding?.retryButton?.setOnClickListener {
                adapter.refresh()
            }
        }

        lifecycleScope.launch {
            viewModel.pagerRemote.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        adapter.addLoadStateListener { loadState ->
            loadState.decideOnState(
                adapter = adapter,
                showLoading = { _, _ -> },
                showEmptyState = { },
                showError = {
                    Timber.e("Error $it")
                    binding?.retryButton?.isVisible = it.isNotEmpty()
                }
            )
        }
    }

    private fun generateViewModelFactory(): MainViewModelFactory {
        val apiService: ApiService = generateApiService()
        val characterDAO = generateCharDao()
        val appDatabase = AppDatabase.newInstance(this)

        return MainViewModelFactory(
            apiService = apiService,
            characterDAO = characterDAO,
            appDatabase = appDatabase
        )
    }

    private fun generateCharDao(): CharacterDAO {
        val appDatabase = AppDatabase.newInstance(this)
        return appDatabase.characterDao()
    }

    private fun generateApiService(): ApiService {
        val networkInterceptor = NetworkConnectionInterceptor(this)
        val client = OkHttpClient.Builder()
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(networkInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}