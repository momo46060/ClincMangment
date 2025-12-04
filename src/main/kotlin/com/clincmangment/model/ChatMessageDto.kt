package com.clincmangment.model

data class ChatMessageDto(
    val id: Long? = null,
    val senderId: Long,
    val senderName: String,
    val recipientId: Long,
    val recipientName: String,
    val content: String,
    val createdAt: String? = null, // ISO string
    val seen: Boolean = false
)
