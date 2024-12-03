package net.integr.solvix.db.user

import net.integr.solvix.dto.UserDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

@Document("users")
class User(
    private var username: String,
    private var firstname: String,
    private var lastname: String,
    private var email: String,
    private var password: String,
    private var termsAccepted: Boolean,
) : UserDetails {
    @Id var id: String? = null

    private var joinDate: LocalDateTime = LocalDateTime.now()
    var hasProfilePicture = false

    fun asUserDto(): UserDto {
        return UserDto(
            id = id,
            username = username,
            firstname = firstname,
            lastname = lastname,
            email = email,
            termsAccepted = termsAccepted,
            joinDate = joinDate.toString(),
            hasProfilePicture = hasProfilePicture
        )
    }

    override fun toString(): String {
        return "User [id=$id, username=$username, password=$password, firstname=$firstname, lastname=$lastname, email=$email, termsAccepted=$termsAccepted, joinDate=$joinDate]"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(GrantedAuthority { "USER" })
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}