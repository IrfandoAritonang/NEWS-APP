package com.example.tugasakhirmobileprogramming.presentation.home.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.tugasakhirmobileprogramming.R
import com.example.tugasakhirmobileprogramming.databinding.ActivityHomeBinding
import com.example.tugasakhirmobileprogramming.presentation.home.view.HomeFragment
import com.example.tugasakhirmobileprogramming.presentation.favorite.view.FavoriteActivity

class HomeActivity : AppCompatActivity() {
    private var _layout: ActivityHomeBinding? = null

    private val layout: ActivityHomeBinding
        get() = _layout ?: throw IllegalStateException("The activity has been destroyed")

    private lateinit var fragment: HomeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        _layout = binding
        setContentView(binding.root)

        setSupportActionBar(layout.toolbar.root)
        supportActionBar?.title = getString(R.string.title_news)

        fragment = HomeFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(layout.frameHome.id, fragment)
        }.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchViewItem = menu?.findItem(R.id.menu_search)
        val searchView = searchViewItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if (query?.isNotBlank() == true) {
                    fragment.searchArticle(query)
                }
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        searchView.setOnCloseListener {
            fragment.getTopHeadlines()
            false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
                return true
            }
            R.id.menu_logout -> {
                fragment.logout()
                return true
            }
            R.id.menu_sort -> {
                fragment.sortArticlesByTitle()
                return true
            }
            R.id.menu_refresh -> {
                // Call the refreshNewsData function on the fragment
                fragment.refreshNewsData()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroy() {
        super.onDestroy()
        _layout = null
    }
}