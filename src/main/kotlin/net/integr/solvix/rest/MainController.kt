package net.integr.solvix.rest

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping

@Controller
@CrossOrigin
class MainController {
    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/register")
    fun register(): String {
        return "register"
    }

    @GetMapping("/home")
    fun home(): String {
        return "home"
    }
}