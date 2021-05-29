package kr.co.bepo.repositorygithub

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import kotlinx.coroutines.*
import kr.co.bepo.repositorygithub.adapter.RepositoryRecyclerAdapter
import kr.co.bepo.repositorygithub.data.entity.GithubRepoEntity
import kr.co.bepo.repositorygithub.databinding.ActivitySearchBinding
import kr.co.bepo.repositorygithub.utility.RetrofitUtil
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RepositoryRecyclerAdapter
    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
        bindViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()

    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isVisible = false
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchBarButton.setOnClickListener {
            searchKeyword(searchBarEditText.text.toString())
        }
    }

    private fun searchKeyword(keywordString: String) = launch {
        withContext(Dispatchers.IO) {
            val response = RetrofitUtil.githubApiService.searchRepositories(keywordString)
            if (response.isSuccessful) {
                val body = response.body()
                withContext(Dispatchers.Main) {
                    Log.e("response", body.toString())
                    body?.let {
                        setData(it.items)
                    }
                }
            }
        }
    }

    private fun setData(githubRepoList: List<GithubRepoEntity>) = with(binding) {
        showLoading(false)
        if (githubRepoList.isEmpty()) {
            emptyResultTextView.isVisible = true
            recyclerView.isVisible = false
        } else {
            emptyResultTextView.isVisible = false
            recyclerView.isVisible = true
            adapter.setSearchResultList(githubRepoList) {
                startActivity(Intent(this@SearchActivity, RepositoryActivity::class.java).apply {
                    putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)
                    putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
                })
            }
        }

    }

    private fun showLoading(isShown: Boolean) = with(binding) {
        progressBar.isGone = isShown.not()
    }
}