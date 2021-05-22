package kr.co.bepo.androidonline.Outstagram

import java.io.Serializable

data class User(
    var username: String? = null,
    var token: String? = null
) : Serializable