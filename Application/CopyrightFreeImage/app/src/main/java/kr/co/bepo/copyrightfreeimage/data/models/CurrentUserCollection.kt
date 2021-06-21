package kr.co.bepo.copyrightfreeimage.data.models


import com.google.gson.annotations.SerializedName

data class CurrentUserCollection(
    @SerializedName("cover_photo")
    val coverPhoto: Any? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("last_collected_at")
    val lastCollectedAt: String? = null,
    @SerializedName("published_at")
    val publishedAt: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null,
    @SerializedName("user")
    val user: Any? = null
)