package kr.co.bepo.checkairpollution.data.models.airquality

import com.google.gson.annotations.SerializedName

data class AirQualityResponse(
    @SerializedName("response")
    val response: Response?
)
