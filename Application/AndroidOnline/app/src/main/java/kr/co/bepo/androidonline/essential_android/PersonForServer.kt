package kr.co.bepo.androidonline.essential_android

import java.io.Serializable

class PersonForServer(
    var id: Int? = null,
    var name: String? = null,
    var age: Int? = null,
    var intro: String? = null
):Serializable