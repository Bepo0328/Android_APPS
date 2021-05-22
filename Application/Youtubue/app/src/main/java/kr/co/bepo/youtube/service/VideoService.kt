package kr.co.bepo.youtube.service

import kr.co.bepo.youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("/v3/210c7d25-6dbd-49ec-89e9-3dfc05db7394")
    fun listVideos(): Call<VideoDto>
}