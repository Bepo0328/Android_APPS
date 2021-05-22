package kr.co.bepo.androidonline.Apple

import java.io.Serializable

data class Song(
    var title: String? = null,
    var song: String? = null,
    var thumbnail: String? = null
): Serializable