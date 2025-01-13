package net.integr.solvix.dto.packet

import jakarta.validation.constraints.NotBlank

class MessagePacket(
    val message: String,
    val sender: String,
    val timestamp: String,
    val type: String
)