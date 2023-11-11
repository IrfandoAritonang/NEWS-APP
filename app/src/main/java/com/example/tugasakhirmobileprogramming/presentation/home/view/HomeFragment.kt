package com.example.tugasakhirmobileprogramming.presentation.home.view

import android.content.Intent
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasakhirmobileprogramming.data.datasource.remote.ApiService
import com.example.tugasakhirmobileprogramming.data.model.ResultSet
import com.example.tugasakhirmobileprogramming.data.model.article.Article
import com.example.tugasakhirmobileprogramming.databinding.FragmentHomeBinding
import com.example.tugasakhirmobileprogramming.di.AppModule
import com.example.tugasakhirmobileprogramming.di.DaggerAppComponent
import com.example.tugasakhirmobileprogramming.presentation.home.view.NewsAdapter
import com.example.tugasakhirmobileprogramming.presentation.detail.view.DetailActivity
import com.example.tugasakhirmobileprogramming.presentation.home.viewmodel.HomeViewModel
import com.example.tugasakhirmobileprogramming.presentation.login.view.LoginActivity
import com.example.tugasakhirmobileprogramming.utils.AppEnvirontment.ConstKey.EXTRA_ARTICLE
import com.example.tugasakhirmobileprogramming.utils.extension.showToast
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData

class HomeFragment : Fragment() {

    private var _layout: FragmentHomeBinding? = null
    private val layout get() = _layout!!

    private val networkData = MutableLiveData<List<Article>>()
    private var originalData: List<Article> = emptyList()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mHomeViewModel: HomeViewModel
    private var mNewsAdapter: NewsAdapter? = null

    var page = 1
    var pageSize = 20
    var isPaginating = false
    var query = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _layout = FragmentHomeBinding.inflate(inflater, container, false)
        return layout.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        DaggerAppComponent.builder()
            .appModule(AppModule(requireContext()))
            .build().inject(this@HomeFragment)

        mHomeViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[HomeViewModel::class.java]

        mNewsAdapter = NewsAdapter(mHomeViewModel) { article ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(EXTRA_ARTICLE, article)
            }
            startActivity(intent)
        }

        layout.rvArticle.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = mNewsAdapter
        }


        layout.rvArticle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = layout.rvArticle.layoutManager
                val visibleCount = layoutManager?.childCount
                val totalItemCount = layoutManager?.itemCount
                val firstVisibleItemPosition =
                    (layout.rvArticle.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (
                    ((visibleCount!! + firstVisibleItemPosition) >= totalItemCount!! &&
                            firstVisibleItemPosition >= 0 &&
                            totalItemCount >= PAGE_SIZE)
                ) {
                    if (!isPaginating && mHomeViewModel.isLastPage.value == false) {
                        isPaginating = true
                        Handler(Looper.getMainLooper()).postDelayed({
                            page += 1
                            if (query.isNotBlank()) {
                                mHomeViewModel.getEverything(page, pageSize, query)
                            } else {
                                mHomeViewModel.getTopHeadlines(requireContext(), page, pageSize)
                            }
                        }, 500)
                    }
                }
            }
        })

        // Observer

        mHomeViewModel.isLastPage.observe(this.viewLifecycleOwner) {
            if (it) {
                layout.rvArticle.adapter?.notifyItemRemoved(
                    layout.rvArticle.adapter?.itemCount ?: 0
                )
            }
        }

        mHomeViewModel.topHeadlines.observe(this.viewLifecycleOwner) { result ->
            when (result) {
                is ResultSet.Loading -> {
                    setLayoutLoadingState()
                }

                is ResultSet.Success -> {
                    result.data.let {
                        mNewsAdapter?.setData(it)
                    }
                    isPaginating = false
                    setLayoutLoadedState()
                }

                is ResultSet.Error -> {
                    layout.tvErrorTitle.text = result.code.toString()
                    layout.tvErrorMessage.text = result.message
                    setLayoutErrorState()
                }
            }
        }

        mHomeViewModel.modeOffline.observe(this.viewLifecycleOwner, Observer {
          requireContext().showToast("Currently you are in offline mode, please check your internet connection")
        })

        mHomeViewModel.logoutState.observe(this.viewLifecycleOwner, Observer {
            if (it == "success") {
                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    addFlags(flags)
                }
                startActivity(intent)
            }
        })

        mHomeViewModel.getTopHeadlines(requireContext(), page, pageSize)
    }

    private fun setLayoutLoadingState() {
        layout.cpiArticle.isVisible = true
        layout.rvArticle.isVisible = false
        layout.tvErrorTitle.isVisible = false
        layout.tvErrorMessage.isVisible = false
    }

    private fun setLayoutLoadedState() {
        layout.cpiArticle.isVisible = false
        layout.rvArticle.isVisible = true
        layout.tvErrorTitle.isVisible = false
        layout.tvErrorMessage.isVisible = false
    }

    private fun setLayoutErrorState() {
        layout.cpiArticle.isVisible = false
        layout.rvArticle.isVisible = false
        layout.tvErrorTitle.isVisible = true
        layout.tvErrorMessage.isVisible = true
    }

    fun getTopHeadlines() {
        query = ""
        page = 1
        mHomeViewModel.getTopHeadlines(requireContext(), page, pageSize)
    }

    fun searchArticle(query: String) {
        this.query = query
        page = 1
        mHomeViewModel.getEverything(page, pageSize, query)
    }

    fun logout() {
        mHomeViewModel.logout()
    }

    fun sortArticlesByTitle() {
        mNewsAdapter?.sortDataByTitle()
    }

    fun refreshNewsData() {
        setLayoutLoadedState()

        if (query.isNotBlank()) {
            mHomeViewModel.getEverything(page,pageSize, query)
        } else {
            mHomeViewModel.getTopHeadlines(requireContext(), page, pageSize)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _layout = null
    }
}