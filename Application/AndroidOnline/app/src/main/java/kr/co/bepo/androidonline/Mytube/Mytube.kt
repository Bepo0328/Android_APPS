package kr.co.bepo.androidonline.Mytube

import java.io.Serializable

data class Mytube(
    var title: String? = null,
    var content: String? = null,
    var video: String? = null,
    var thumbnail: String? = null
):Serializable
