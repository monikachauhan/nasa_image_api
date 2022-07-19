package com.example.astronomicalphotooftheday.data.remote

import com.example.core.utils.Constants.API_KEY
import com.example.astronomicalphotooftheday.data.remote.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface HomeApi {
    @GET("planetary/apod?api_key=${API_KEY}")//date: String="2016-11-04"
    suspend fun getTodayPhoto(): PhotoDto

    @GET("planetary/apod?api_key=${API_KEY}")
    suspend fun getPhotoByDate(@Query("date") date:String?): PhotoDto


}