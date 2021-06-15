package kr.co.bepo.repositorygithub.data.response

import kr.co.bepo.repositorygithub.data.entity.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)
