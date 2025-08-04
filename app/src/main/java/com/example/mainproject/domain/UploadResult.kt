package com.example.mainproject.domain

data class UploadResult(
    val isDangerous: Boolean,
    val dangerWords: List<String>,
    val taleId: Int?
) {
    companion object {
        fun from(json: Map<String, Any>): UploadResult {
            val isDangerous = json["dangerous"] as? Boolean ?: false
            val dangerWords = json["danger_words"] as? List<String> ?: emptyList()
            val taleId = (json["tale_id"] as? Double)?.toInt()
            return UploadResult(isDangerous, dangerWords, taleId)
        }
    }
}
