package net.integr.solvix.db.ticket

import net.integr.solvix.dto.CreateTicketDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TicketService @Autowired constructor(
    val ticketRepo: TicketRepository
) {
    fun save(ticketDto: CreateTicketDto, userId: String): Ticket {
        val ticket = Ticket(ticketDto.title, userId)
        return ticketRepo.save(ticket)
    }

    fun findById(id: String): Ticket? {
        return ticketRepo.findById(id).orElse(null)
    }

    fun findAll(): List<Ticket> {
        return ticketRepo.findAll()
    }

    fun findAllBy(id: String): List<Ticket> {
        return ticketRepo.findAllByCreatorId(id)
    }

    fun existsById(id: String): Boolean {
        return ticketRepo.existsById(id)
    }

    fun deleteById(id: String) {
        ticketRepo.deleteById(id)
    }

    fun updateById(id: String, ticketDto: CreateTicketDto) {
        var existing = ticketRepo.findById(id).orElse(null)

        if (existing == null) return

        existing.title = ticketDto.title
        ticketRepo.save(existing)
    }

    fun insertMessage(id: String, message: Message) {
        var existing = ticketRepo.findById(id).orElse(null)
        if (existing == null) return

        existing.messages.add(message)
        ticketRepo.save(existing)
    }
}