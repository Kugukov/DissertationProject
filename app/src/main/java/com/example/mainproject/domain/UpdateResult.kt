package com.example.mainproject.domain

data class UpdateResult(
    val isDangerous: Boolean,
    val dangerWords: List<String>,
    val taleId: Int?
) {
    companion object {
        fun from(map: Map<String, Any?>): UpdateResult {
            return UpdateResult(
                isDangerous = map["dangerous"] as? Boolean ?: false,
                dangerWords = (map["danger_words"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                taleId = (map["tale_id"] as? Double)?.toInt()
            )
        }
    }
}
