package com.example.mainproject.data.adapter

import com.example.mainproject.data.network.UpdateResultDto
import com.example.mainproject.data.network.UploadResultDto

fun mapToUpdateResultDto(map: Map<String, Any?>): UpdateResultDto {
    return UpdateResultDto(
        dangerous = map["dangerous"] as? Boolean ?: false,
        dangerWords = (map["danger_words"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
        taleId = (map["tale_id"] as? Double)?.toInt()
    )
}

fun mapToUploadResultDto(map: Map<String, Any?>): UploadResultDto {
    return UploadResultDto(
        dangerous = map["dangerous"] as? Boolean ?: false,
        dangerWords = (map["danger_words"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
        taleId = (map["tale_id"] as? Double)?.toInt()
    )
}
