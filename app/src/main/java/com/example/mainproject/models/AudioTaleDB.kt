package com.example.mainproject.models

import com.google.gson.annotations.SerializedName

data class AudioTaleDB(
    val id: Int,
    val title: String,
    val description: String,
    val duration: String,
    @SerializedName("file_url") val fileUrl: String
)
