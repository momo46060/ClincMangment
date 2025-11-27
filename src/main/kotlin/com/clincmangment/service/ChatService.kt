package com.clincmangment.service

import com.clincmangment.repository.ChatMessageRepository
import com.clincmangment.repository.UserRepository
import com.clincmangment.repository.model.ChatMessage
import com.clincmangment.repository.model.ChatMessageDto
import com.clincmangment.repository.model.User
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
class ChatService(
    private val chatRepo: ChatMessageRepository,
    private val userRepo: UserRepository,
    private val messagingTemplate: SimpMessagingTemplate
) {

    private val fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @Transactional
    fun saveAndSendMessage(senderPhoneOrPrincipal: String, dto: ChatMessageDto): ChatMessageDto {
        println("\n" + "=".repeat(60))
        println("📨 استلام رسالة جديدة")
        println("👤 المرسل Principal: $senderPhoneOrPrincipal")
        println("📝 المحتوى: ${dto.content}")
        println("🎯 المستلم ID: ${dto.recipientId}")

        // البحث عن المرسل
        val sender = findUserByPrincipal(senderPhoneOrPrincipal)
            ?: throw RuntimeException("❌ المرسل غير موجود: $senderPhoneOrPrincipal")

        println("✅ المرسل: ${sender.fullName} (ID: ${sender.id})")

        // البحث عن المستقبل
        val recipient = userRepo.findById(dto.recipientId)
            .orElseThrow { RuntimeException("❌ المستلم غير موجود برقم: ${dto.recipientId}") }

        println("✅ المستلم: ${recipient.fullName} (ID: ${recipient.id})")

        // إنشاء وحفظ الرسالة
        val msg = ChatMessage(
            sender = sender,
            recipient = recipient,
            content = dto.content,
            seen = false
        )

        val saved = chatRepo.save(msg)
        println("💾 تم حفظ الرسالة - ID: ${saved.id}")

        val out = ChatMessageDto(
            id = saved.id,
            senderId = saved.sender.id!!,
            senderName = saved.sender.fullName,
            recipientId = saved.recipient.id!!,
            recipientName = saved.recipient.fullName,
            content = saved.content,
            createdAt = saved.createdAt?.format(fmt),
            seen = saved.seen
        )

        // ✅ الطريقة الجديدة: إرسال مباشر بدون convertAndSendToUser
        try {
            // إرسال للمستقبل
            val recipientDest = "/user/${recipient.id}/queue/messages"
            println("📤 إرسال للمستلم: $recipientDest")
            messagingTemplate.convertAndSend(recipientDest, out)

            // إرسال للمرسل (echo)
            val senderDest = "/user/${sender.id}/queue/messages"
            println("📤 إرسال للمرسل (echo): $senderDest")
            messagingTemplate.convertAndSend(senderDest, out)

            println("✅ تم إرسال الرسالة بنجاح إلى الطرفين")
        } catch (e: Exception) {
            println("❌ خطأ في إرسال الرسالة: ${e.message}")
            e.printStackTrace()
        }

        println("=".repeat(60) + "\n")
        return out
    }

    @Transactional(readOnly = true)
    fun getConversation(a: Long, b: Long): List<ChatMessageDto> {
        println("🔍 جلب المحادثة بين $a و $b")
        val list = chatRepo.findConversation(a, b)
        println("📊 عدد الرسائل: ${list.size}")

        return list.map {
            ChatMessageDto(
                id = it.id,
                senderId = it.sender.id!!,
                senderName = it.sender.fullName,
                recipientId = it.recipient.id!!,
                recipientName = it.recipient.fullName,
                content = it.content,
                createdAt = it.createdAt?.format(fmt),
                seen = it.seen
            )
        }
    }

    private fun findUserByPrincipal(principalName: String): User? {
        println("🔍 البحث عن المستخدم بـ: $principalName")

        // محاولة 1: رقم الهاتف
        var user = userRepo.findByPhone(principalName)
        if (user != null) {
            println("✅ وُجد برقم الهاتف")
            return user
        }

        // محاولة 2: الإيميل
        user = userRepo.findByEmail(principalName)
        if (user != null) {
            println("✅ وُجد بالإيميل")
            return user
        }

        // محاولة 3: الـ ID
        val userId = principalName.toLongOrNull()
        if (userId != null) {
            user = userRepo.findById(userId).orElse(null)
            if (user != null) {
                println("✅ وُجد بالـ ID")
                return user
            }
        }

        println("❌ لم يُعثر على المستخدم")
        return null
    }
}