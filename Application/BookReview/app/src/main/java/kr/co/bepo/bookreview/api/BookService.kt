package kr.co.bepo.bookreview.api

import kr.co.bepo.bookreview.model.BestSellerDTO
import kr.co.bepo.bookreview.model.SearchBookDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/api/search.api?output=json")
    fun getBookByName(
        @Query("key") apiKey: String,
        @Query("query") keyword: String
    ): Call<SearchBookDTO>

    @GET("/api/bestSeller.api?output=json&categoryId=100")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDTO>
}