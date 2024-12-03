package net.integr.solvix.db.ticket

import net.integr.solvix.dto.TicketDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("tickets")
class Ticket(
    var title: String,
    var creatorId: String,
    var messages: MutableList<Message> = mutableListOf()
) {
    @Id var id: String? = null

    private var createDate: LocalDateTime = LocalDateTime.now()

    override fun toString(): String {
        return "Ticket [id=$id, title=$title, creatorId=$creatorId, messages=$messages, createDate=$createDate]"
    }

    fun addMessage(message: Message) {
        messages.add(message)
    }

    fun asTicketDto(): TicketDto {
        return TicketDto(
            title,
            creatorId,
            id!!,
            createDate.toString()
        )
    }

    fun asListEntry(): String {
        return "$title␀$id"
    }
}