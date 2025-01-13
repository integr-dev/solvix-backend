package net.integr.solvix.socket.chat

import net.integr.solvix.db.ticket.TicketRepository
import net.integr.solvix.db.user.UserService
import net.integr.solvix.dto.packet.SendMessagePacket
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
@MessageMapping("/ticket")
class ChatController @Autowired constructor(
    val userService: UserService,
    val chatMessageManager: ChatMessageManager
) {
    @MessageMapping("/message/{ticketId}")
    fun message(message: SendMessagePacket, user: Principal, @DestinationVariable ticketId: String) {
        val userObj = userService.findByUsername(user.name)

        chatMessageManager.saveMessage(message, userObj!!, ticketId)
        chatMessageManager.broadcast(message, userObj, ticketId)
    }
}