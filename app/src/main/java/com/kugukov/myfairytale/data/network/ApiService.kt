package com.kugukov.myfairytale.data.network

import com.kugukov.myfairytale.domain.model.AudioTaleDB
import com.kugukov.myfairytale.domain.model.TextTale
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming

interface ApiService {
    @POST("registerDevice")
    suspend fun registerDevice(
        @Body deviceInfo: Map<String, String>
    ): Response<Unit>

    @GET("checkRegisterDevice/{device_id}")
    suspend fun checkRegisterDevice(@Path("device_id") deviceId: String): Response<DeviceResponse>

    data class DeviceResponse(val exists: Boolean)

    @Multipart
    @POST("uploadAudioTale")
    suspend fun uploadAudioTale(
        @Part("device_id") deviceId: RequestBody,
        @Part("metadata") metadata: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<Map<String, Any>>

    @GET("audioTales/{device_id}")
    suspend fun getAudioTales(@Path("device_id") deviceId: String): Response<List<AudioTaleDB>>

    @GET("getFileAudioTale/{filename}")
    @Streaming
    suspend fun downloadFile(@Path("filename") filename: String): Response<ResponseBody>

    @DELETE("audioTales/{id}")
    suspend fun deleteAudioTale(@Path("id") taleId: Int): Response<Unit>

    @Multipart
    @PUT("audioTales/{id}")
    suspend fun updateAudioTale(
        @Path("id") taleId: Int,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("duration") audioDuration: RequestBody?,
        @Part file: MultipartBody.Part?
    ): Response<Map<String, Any>>

    /* Text Tales */
    @GET("textTales/{device_id}")
    suspend fun getTextTales(@Path("device_id") deviceId: String): Response<List<TextTale>>

    @Multipart
    @POST("uploadTextTale")
    suspend fun uploadTextTale(
        @Part("device_id") deviceId: RequestBody,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
    ): Response<ResponseBody>

    @DELETE("textTales/{id}")
    suspend fun deleteTextTale(@Path("id") taleId: Int): Response<Unit>

    @Multipart
    @PUT("textTales/{id}")
    suspend fun updateTextTale(
        @Path("id") taleId: Int,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody
    ): Response<ResponseBody>
}