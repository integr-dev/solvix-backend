package net.integr.solvix

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SolvixBackendApplication

fun main(args: Array<String>) {
    runApplication<SolvixBackendApplication>(*args)
}
