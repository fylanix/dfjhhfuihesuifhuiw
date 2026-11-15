package com.example.satuhati.domain.model

data class PregnancyNote(
    val id: String = "",
    val coupleId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val dateMillis: Long = System.currentTimeMillis(),
    val mood: String = "Senang", // Senang, Netral, Cemas, Lelah, Sakit
    val category: String = "Catatan Kehamilan",
    val content: String = "",
    val isReadByHusband: Boolean = false,
    val husbandResponse: String? = null,
    val husbandRespondedAt: Long? = null
)
