package kr.co.bepo.androidonline.Outstagram

import java.io.Serializable

data class Post(
    val image: String? = null,
    val owner: String? = null,
    val content: String? = null
):Serializable