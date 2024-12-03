package net.integr.solvix.socket.chat

import net.integr.solvix.db.user.User
import net.integr.solvix.dto.packet.SendMessagePacket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service

@Service
class ChatMessageManager @Autowired constructor(val simpMessageSendingOperations: SimpMessageSendingOperations) {
    fun broadcast(message: SendMessagePacket, user: User, ticketId: String) {
        val obj = message.asRelayMessage(user)
        simpMessageSendingOperations.convertAndSend("/public/message/$ticketId", obj)
    }
}