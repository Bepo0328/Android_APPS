package kr.co.bepo.checkairpollution.data.models.monitoringstation

import com.google.gson.annotations.SerializedName

data class MonitoringStationsResponse(
    @SerializedName("response")
    val response: Response?
)
