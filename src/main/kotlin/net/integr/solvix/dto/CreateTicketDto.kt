package net.integr.solvix.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class CreateTicketDto(
    @field:NotBlank
    // No null chars because of list endpoint using them as separators
    // and there being the possibility of the frontend breaking
    @field:Pattern(regexp = "[^␀]*")
    var title: String
)