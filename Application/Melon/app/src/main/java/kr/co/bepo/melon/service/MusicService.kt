package kr.co.bepo.melon.service

import kr.co.bepo.melon.dto.MusicDTO
import retrofit2.Call
import retrofit2.http.GET

interface MusicService {

    @GET("/v3/5f6b238a-b3a5-49b1-bbdf-32e1a37b915b")
    fun listMusic(): Call<MusicDTO>
}