package net.integr.solvix.socket.chat

import net.integr.solvix.db.ticket.Message
import net.integr.solvix.db.ticket.TicketRepository
import net.integr.solvix.db.ticket.TicketService
import net.integr.solvix.db.user.User
import net.integr.solvix.db.user.UserService
import net.integr.solvix.dto.packet.SendMessagePacket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import java.time.LocalTime

@Service
class ChatMessageManager @Autowired constructor(
    val simpMessageSendingOperations: SimpMessageSendingOperations,
    val ticketService: TicketService,
    val userService: UserService
) {
    fun broadcast(message: SendMessagePacket, user: User, ticketId: String) {
        val obj = message.asRelayMessage(user)
        simpMessageSendingOperations.convertAndSend("/public/message/$ticketId", obj)
    }

    fun saveMessage(message: SendMessagePacket, user: User, ticketId: String) {
        val message = Message(
            type = message.type,
            authorId = user.id!!,
            content = message.message,
            timestamp = LocalTime.now()
        )

        ticketService.insertMessage(ticketId, message)


    }

    fun sendInitialUserMessages(ticketId: String, user: User) {
        val messages = ticketService.findById(ticketId)!!.messages
        val obj = messages.map { it.asRelayMessage(userService) }

        simpMessageSendingOperations.convertAndSendToUser(user.username, "/public/message/$ticketId", obj)
    }
}