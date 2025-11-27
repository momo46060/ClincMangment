package com.clincmangment.service

import com.clincmangment.repository.ChatMessageRepository
import com.clincmangment.repository.UserRepository
import com.clincmangment.repository.model.User
import com.clincmangment.utils.Role
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service


@Service
class ChatMessageService(
    private val chatMessageRepository: ChatMessageRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun markMessagesAsSeen(currentUserId: Long, otherUserId: Long) {
        chatMessageRepository.markMessagesAsSeen(currentUserId, otherUserId)
    }

    fun getAllUnreadCounts(): MutableMap<Long?, Int?> {
        val counts: MutableMap<Long?, Int?> = HashMap()

        val users: MutableList<User> = userRepository.findAll()
            .filter { it.role != Role.PATIENT } as MutableList<User>
        for (user in users) {
            val unread: Int = chatMessageRepository.countBySenderIdAndSeenFalse(user.id)
            counts.put(user.id, unread)
        }

        return counts
    }
}