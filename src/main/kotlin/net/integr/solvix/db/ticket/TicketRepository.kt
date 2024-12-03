package net.integr.solvix.db.ticket

import net.integr.solvix.db.user.User
import org.springframework.data.mongodb.repository.MongoRepository

interface TicketRepository : MongoRepository<Ticket, String> {
    fun findAllByCreatorId(creatorId: String): List<Ticket>
}