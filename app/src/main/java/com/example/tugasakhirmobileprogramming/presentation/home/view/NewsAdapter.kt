package com.example.tugasakhirmobileprogramming.presentation.home.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirmobileprogramming.R
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.databinding.ItemNewsBinding
import com.example.tugasakhirmobileprogramming.databinding.ItemPagingBinding
import com.example.tugasakhirmobileprogramming.presentation.home.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class NewsAdapter(
    private val mHomeViewModel: HomeViewModel,
    private val itemClick: (Article) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mNews = ArrayList<Article>()

    //membuat fungsi short data
    fun sortDataByTitle() {
        mNews.sortBy { it.title }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (mHomeViewModel.isLastPage.value == false && position == mNews.size) {
            R.layout.item_paging
        } else {
            R.layout.item_news
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_news -> {
                val itemBinding = ItemNewsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                NewsViewHolder(itemBinding)
            }
            else -> {
                val binding = ItemPagingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PaginateItem(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            holder.bind(mNews[position])
        }

    }

    override fun getItemCount(): Int = mNews.size + (if (mHomeViewModel.isLastPage.value == false) 1 else 0)

    fun setData(listNews: List<Article>) {
        mNews.clear()
        mNews.addAll(listNews)
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(article: Article) {
            article.url.let { url ->
                binding.root.setOnClickListener { itemClick(article)}
            }

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
            val date = inputDateFormat.parse(article.publishedAt)

            Picasso.get()
                .load(article.urlToImage)
                .error(R.drawable.broken_image)
                .into(binding.ivNews)
            binding.tvTitle.text = article.title
            binding.tvDate.text = outputDateFormat.format(date)
        }
    }
    inner class PaginateItem(private val binding: ItemPagingBinding) :
        RecyclerView.ViewHolder(binding.root)
}