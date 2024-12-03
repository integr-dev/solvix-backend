package net.integr.solvix.db.user

import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findByUsername(username: String): User?
}