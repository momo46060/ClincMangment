package com.clincmangment.controller

import com.clincmangment.repository.ChatMessageRepository
import com.clincmangment.repository.UserRepository
import com.clincmangment.model.ChatMessageDto
import com.clincmangment.service.ChatMessageService
import com.clincmangment.service.ChatService
import com.clincmangment.utils.Role
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.security.Principal

@Controller
class ChatController(
    private val chatService: ChatService,
    private val userRepository: UserRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val chatMessageService: ChatMessageService
) {

    @MessageMapping("/chat.send")
    fun processMessage(@Payload message: ChatMessageDto, principal: Principal) {
        println("\n" + "🔵".repeat(30))
        println("📨 CONTROLLER: استلام رسالة جديدة")
        println("👤 Principal Name: ${principal.name}")
        println("📝 محتوى الرسالة: ${message.content}")
        println("🎯 من ID: ${message.senderId} إلى ID: ${message.recipientId}")
        println("🔵".repeat(30) + "\n")

        try {
            val result = chatService.saveAndSendMessage(principal.name, message)
            println("✅ CONTROLLER: تمت معالجة الرسالة بنجاح - Message ID: ${result.id}")
        } catch (e: Exception) {
            println("❌ CONTROLLER: خطأ في معالجة الرسالة")
            println("   الخطأ: ${e.message}")
            e.printStackTrace()
        }
    }

    @GetMapping("/chat/unread-count")
    @ResponseBody
    fun getUnreadCount( principal: Principal): Int =
        chatMessageRepository.countByRecipientPhoneAndSeenFalse(principal.name)


    @PostMapping("/chat/mark-seen/{userId}")
    @ResponseBody
    fun markMessagesSeen(
        @PathVariable userId: Long,
        principal: Principal
    ): String {
        println("***************************************")
        println("***************************************")
        println(userId)
        println("***************************************")
        println("***************************************")
        val user = userRepository.findByPhone(principal.name)
        chatMessageService.markMessagesAsSeen(
            currentUserId = user!!.id!!,
            otherUserId = userId
        )
        return "OK"
    }

    @GetMapping("/chat")
    @Transactional
    fun chatPage(model: Model, principal: Principal): String {
        println("🌐 فتح صفحة الشات للمستخدم: ${principal.name}")

        val principalName = principal.name

        val me = userRepository.findByPhone(principalName)
            ?: userRepository.findByEmail(principalName)
            ?: run {
                principalName.toLongOrNull()?.let { id ->
                    userRepository.findById(id).orElse(null)
                }
            }
            ?: throw RuntimeException("المستخدم غير موجود: $principalName")

        val clinicId = me.clinic!!.id ?: throw RuntimeException("العيادة غير موجودة للمستخدم ${me.id}")

        val users = userRepository.findAllByClinicIdAndIdNot(clinicId, me.id!!).filter { it.role != Role.PATIENT }

        println("👥 عدد المستخدمين في العيادة: ${users.size}")

        model.addAttribute("currentUserId", me.id)
        model.addAttribute("currentUserName", me.fullName)
        model.addAttribute("currentUserRole", me.role.name)
        model.addAttribute("users", users)
        model.addAttribute("currentPrincipal", principalName)

        return "chat/chat"
    }


    @GetMapping("/api/chat/conversation/{userId}")
    @ResponseBody
    fun getConversation(
        @PathVariable userId: Long,
        principal: Principal
    ): List<ChatMessageDto> {
        val me = userRepository.findByPhone(principal.name)
            ?: userRepository.findByEmail(principal.name)
            ?: throw RuntimeException("المستخدم غير موجود")

        println("📥 جلب المحادثة بين ${me.id} و $userId")
        return chatService.getConversation(me.id!!, userId)
    }
}