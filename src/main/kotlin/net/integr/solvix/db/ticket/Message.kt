package net.integr.solvix.db.ticket

import net.integr.solvix.db.user.UserService
import net.integr.solvix.dto.packet.MessagePacket
import java.time.LocalTime

class Message(
    private var type: String,
    private var content: String,
    private var authorId: String,
    private var timestamp: LocalTime
) {
    fun asRelayMessage(userService: UserService): MessagePacket {
        val m = MessagePacket(content, userService.findById(authorId)!!.username, timestamp.toString(), type)
        return m
    }
}