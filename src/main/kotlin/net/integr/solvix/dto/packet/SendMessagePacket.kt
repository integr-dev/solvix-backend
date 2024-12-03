package net.integr.solvix.dto.packet

import jakarta.validation.constraints.NotBlank
import net.integr.solvix.db.user.User
import java.time.LocalTime

data class SendMessagePacket(
    @field:NotBlank(message = "Message cannot be blank")
    val message: String
) {
    fun asRelayMessage(user: User): MessagePacket {
        val m = MessagePacket(message, user.username, LocalTime.now().toString())
        return m
    }
}