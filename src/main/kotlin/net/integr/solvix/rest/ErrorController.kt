package net.integr.solvix.rest

import net.integr.solvix.util.error.ErrorBinding
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import java.util.function.Consumer

@RestController
@CrossOrigin
class ErrorController {
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<List<ErrorBinding>> {
        val errMap: MutableList<ErrorBinding> = mutableListOf()

        e.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = try {
                (error as FieldError).field
            } catch (ex: ClassCastException) {
                error.objectName
            }

            val message = error.defaultMessage

            val binding = ErrorBinding(message!!, fieldName)
            errMap += binding
        })

        return ResponseEntity<List<ErrorBinding>>(errMap, HttpStatus.BAD_REQUEST)
    }
}