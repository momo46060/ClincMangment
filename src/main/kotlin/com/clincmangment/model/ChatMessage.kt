package com.clincmangment.model


import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "chat_messages", indexes = [
    Index(name = "idx_chat_sender", columnList = "sender_id"),
    Index(name = "idx_chat_recipient", columnList = "recipient_id")
])
data class ChatMessage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    var sender: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    var recipient: User,

    @Column(nullable = false, length = 2000)
    var content: String,

    @CreationTimestamp
    var createdAt: LocalDateTime? = null,

    @Column(nullable = false)
    var seen: Boolean = false
)
