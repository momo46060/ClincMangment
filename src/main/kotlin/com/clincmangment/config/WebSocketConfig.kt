package com.clincmangment.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        println("🔧 تهيئة Message Broker")

        // تفعيل simple broker
        registry.enableSimpleBroker("/topic", "/queue", "/user")

        // prefix للرسائل من العميل
        registry.setApplicationDestinationPrefixes("/app")

        // ✅ مهم جداً: prefix للرسائل الموجهة لمستخدم محدد
        registry.setUserDestinationPrefix("/user")

        println("✅ تم تهيئة Message Broker بنجاح")
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        println("🔧 تسجيل STOMP Endpoints")

        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()

        println("✅ تم تسجيل endpoint: /ws")
    }
}