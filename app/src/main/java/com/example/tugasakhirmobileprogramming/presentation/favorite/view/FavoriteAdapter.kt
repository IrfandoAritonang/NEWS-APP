package com.example.tugasakhirmobileprogramming.presentation.favorite.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirmobileprogramming.R
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale

class FavoriteAdapter(
    private val itemClick: (Article) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var mFavorites = ArrayList<Article>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(mFavorites[position])
    }

    override fun getItemCount(): Int = mFavorites.size

    fun setData(listArticle: List<Article>) {
        mFavorites.clear()
        mFavorites.addAll(listArticle)
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(article: Article) {
            binding.root.setOnClickListener { itemClick(article) }
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
}
