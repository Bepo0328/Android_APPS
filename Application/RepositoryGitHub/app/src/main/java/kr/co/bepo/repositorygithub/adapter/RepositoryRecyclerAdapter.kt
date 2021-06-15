package kr.co.bepo.repositorygithub.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kr.co.bepo.repositorygithub.data.entity.GithubRepoEntity
import kr.co.bepo.repositorygithub.databinding.ViewholderRepositoryItemBinding
import kr.co.bepo.repositorygithub.extensions.loadCenterInside

class RepositoryRecyclerAdapter : RecyclerView.Adapter<RepositoryRecyclerAdapter.RepositoryItemViewHolder>() {

    private var repositoryList: List<GithubRepoEntity> = listOf()
    private lateinit var repositoryClickListener: (GithubRepoEntity) -> Unit

    inner class RepositoryItemViewHolder(
        private val binding: ViewholderRepositoryItemBinding,
        val searchResultClickListener: (GithubRepoEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: GithubRepoEntity) = with(binding) {
            ownerProfileImageView.loadCenterInside(data.owner.avatarUrl, 24f)
            ownerNameTextView.text = data.owner.login
            nameTextView.text = data.fullName
            descriptionTextView.text = data.description
            stargazersCountTextView.text = data.stargazersCount.toString()
            data.language?.let { language ->
                languageTextView.isVisible = true
                languageTextView.text = language
            } ?: kotlin.run {
                languageTextView.isVisible = false
                languageTextView.text = ""
            }
        }

        fun bindViews(data: GithubRepoEntity) {
            binding.root.setOnClickListener {
                searchResultClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryItemViewHolder {
        val view = ViewholderRepositoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryItemViewHolder(view, repositoryClickListener)
    }

    override fun onBindViewHolder(holder: RepositoryItemViewHolder, position: Int) {
        holder.bindData(repositoryList[position])
        holder.bindViews(repositoryList[position])
    }

    override fun getItemCount(): Int = repositoryList.size

    fun setSearchResultList(searchResultList: List<GithubRepoEntity>, searchResultClickListener: (GithubRepoEntity) -> Unit) {
        this.repositoryList = searchResultList
        this.repositoryClickListener = searchResultClickListener
        notifyDataSetChanged()
    }
}
