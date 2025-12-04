package com.clincmangment.repository

import com.clincmangment.model.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun countByRecipientIdAndSeenFalse(recipientId: Long?): Int
    fun countBySenderIdAndSeenFalse(recipientId: Long?): Int

    @Query("""
        SELECT m FROM ChatMessage m
        WHERE (m.sender.id = :a AND m.recipient.id = :b) OR (m.sender.id = :b AND m.recipient.id = :a)
        ORDER BY m.createdAt ASC
    """)
    fun findConversation(@Param("a") a: Long, @Param("b") b: Long): List<ChatMessage>

    fun countByRecipientPhoneAndSeenFalse(phone: String?): Int
    @Modifying
    @Query("""
    UPDATE ChatMessage m 
    SET m.seen = true 
    WHERE m.sender.id = :otherUserId 
      AND m.recipient.id = :currentUserId 
      AND m.seen = false
""")
    fun markMessagesAsSeen(currentUserId: Long, otherUserId: Long)



}

