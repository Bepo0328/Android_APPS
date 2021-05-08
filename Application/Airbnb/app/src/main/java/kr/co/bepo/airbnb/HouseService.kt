package kr.co.bepo.airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/0d0860f6-75d8-475f-99de-c512c95569fa")
    fun getHouseList(): Call<HouseDTO>
}