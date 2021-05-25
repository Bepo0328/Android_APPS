package kr.co.bepo.locationsearchmap.response.address

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddressInfo(
    @Expose
    @SerializedName("fullAddress") val fullAddress: String?,

    @Expose
    @SerializedName("addressType") val addressType: String?,

    @Expose
    @SerializedName("city_do") val cityDo: String?,

    @Expose
    @SerializedName("gu_gun") val guGun: String?,

    @Expose
    @SerializedName("eup_myun") val eupMyun: String?,

    @Expose
    @SerializedName("adminDong") val adminDong: String?,

    @Expose
    @SerializedName("adminDongCode") val adminDongCode: String?,

    @Expose
    @SerializedName("legalDong") val legalDong: String?,

    @Expose
    @SerializedName("legalDongCode") val legalDongCode: String?,

    @Expose
    @SerializedName("ri" ) val ri: String?,

    @Expose
    @SerializedName("bunji") val bunji: String?,

    @Expose
    @SerializedName("roadName") val roadName: String?,

    @Expose
    @SerializedName("buildingIndex") val buildingIndex: String?,

    @Expose
    @SerializedName("buildingName") val buildingName: String?,

    @Expose
    @SerializedName("mappingDistance") val mappingDistance: String?,

    @Expose
    @SerializedName("roadCode") val roadCode: String?
)

