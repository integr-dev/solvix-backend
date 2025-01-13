package net.integr.solvix.db.user

import net.integr.solvix.dto.CreateUserDto
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class UserService(private val userRepository: UserRepository) : UserDetailsService {
    val passwordEncoder: PasswordEncoder = passwordEncoder()

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun findById(id: String): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun save(createUserDto: CreateUserDto): User {
        val user = User(
            createUserDto.username!!,
            createUserDto.firstname!!,
            createUserDto.lastname!!,
            createUserDto.email!!,
            passwordEncoder.encode(createUserDto.password!!),
            createUserDto.termsAccepted!!
        )
        return userRepository.save(user)
    }

    fun deleteById(id: String) {
        userRepository.deleteById(id)
    }

    fun setHasProfilePic(id: String, hasProfilePic: Boolean) {
        var existing = userRepository.findById(id).orElse(null)

        if (existing == null) return

        existing.hasProfilePicture = hasProfilePic
        userRepository.save(existing)
    }

    @Bean
    final fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}