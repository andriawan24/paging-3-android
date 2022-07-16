package com.andriawan.testinglearning.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.andriawan.testinglearning.databinding.LoadStateItemBinding
import timber.log.Timber

class PagingLoadStateAdapter<VH : RecyclerView.ViewHolder>(
    private val adapter: PagingDataAdapter<*, VH>
) : LoadStateAdapter<PagingLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return LoadStateViewHolder(binding) {
            adapter.retry()
        }
    }

    class LoadStateViewHolder(
        private val binding: LoadStateItemBinding,
        private val onRetryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                onRetryCallback()
            }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                Timber.d("Load state is $loadState")
                progressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
                errorMsg.isVisible =
                    (loadState as? LoadState.Error)?.error?.message?.isNotBlank() == true
                errorMsg.text = (loadState as? LoadState.Error)?.error?.message
            }
        }
    }
}