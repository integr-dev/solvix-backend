package net.integr.solvix.rest

import jakarta.validation.Valid
import net.integr.solvix.db.ticket.TicketService
import net.integr.solvix.db.user.UserService
import net.integr.solvix.dto.CreateTicketDto
import net.integr.solvix.dto.TicketDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@CrossOrigin
@RequestMapping("/api/ticket")
class TicketController @Autowired constructor(
    val ticketService: TicketService,
    val userService: UserService
){
    @PostMapping("/create")
    fun createTicket(@Valid @RequestBody createTicketDto: CreateTicketDto, principal: Principal): ResponseEntity<String> {
        val user = userService.findByUsername(principal.name)
        var tick = ticketService.save(createTicketDto, user!!.id!!)

        return ResponseEntity.status(HttpStatus.CREATED).body(tick.id)
    }

    @GetMapping("/get")
    fun getTicket(@RequestParam(name = "id") id: String): ResponseEntity<TicketDto> {
        val requested = ticketService.findById(id)

        if (requested == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        return ResponseEntity.ok(requested.asTicketDto())
    }

    @DeleteMapping("/delete")
    fun deleteTicket(@RequestParam(name = "id") id: String): ResponseEntity<String> {
        if (!ticketService.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        ticketService.deleteById(id)

        return ResponseEntity.status(HttpStatus.OK).body("Ticket deleted!")
    }

    @PutMapping("/update")
    fun updateTicket(@RequestParam(name = "id") id: String, @Valid @RequestBody createTicketDto: CreateTicketDto): ResponseEntity<String> {
        if (!ticketService.existsById(id)) return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        ticketService.updateById(id, createTicketDto)

        return ResponseEntity.status(HttpStatus.OK).body("Ticket updated!")
    }

    @GetMapping("/all")
    fun getAllTickets(): List<String> {
        return ticketService.findAll().map { it.asListEntry() }
    }

    @GetMapping("/my")
    fun getMyTickets(principal: Principal): List<String> {
        val user = userService.findByUsername(principal.name)
        return ticketService.findAllBy(user!!.id!!).map { it.asListEntry() }
    }

    @GetMapping("/by")
    fun getTicketsBy(@RequestParam(name = "id") id: String): List<String> {
        return ticketService.findAllBy(id).map { it.asListEntry() }
    }
}