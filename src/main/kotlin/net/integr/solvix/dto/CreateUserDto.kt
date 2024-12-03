package net.integr.solvix.dto

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

class CreateUserDto(
    @field:Size(min = 3, message = "Username must be at least 3 characters long")
    @field:Size(max = 30, message = "Username must be less than 30 characters long")
    @field:Pattern(regexp = "[a-zA-Z0-9]*", message = "Username must be alphanumeric")
    var username: String?,

    @field:NotBlank(message = "First name is required")
    var firstname: String?,

    @field:NotBlank(message = "Last name is required")
    var lastname: String?,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email is invalid")
    var email: String?,

    @field:Size(min = 6, message = "Password must be at least 6 characters long")
    @field:Pattern(regexp = "\\S*", message = "Password must contain no spaces")
    var password: String?,

    @field:AssertTrue(message = "You must accept the terms and conditions")
    var termsAccepted: Boolean?
) {
    override fun toString(): String {
        return "UserDto [username=$username, password=$password, firstname=$firstname, lastname=$lastname, email=$email, termsAccepted=$termsAccepted]";
    }
}