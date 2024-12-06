package net.integr.solvix.rest

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import net.integr.solvix.db.user.User
import net.integr.solvix.db.user.UserService
import net.integr.solvix.dto.CreateUserDto
import net.integr.solvix.dto.UserDto
import net.integr.solvix.util.error.ErrorBinding
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.security.Principal
import java.util.function.Consumer

@RestController
@CrossOrigin
@RequestMapping("/api/account")
class AccountController @Autowired constructor(var userService: UserService) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody createUserDto: CreateUserDto, request: HttpServletRequest): ResponseEntity<String> {
        val user: User? = userService.findByUsername(createUserDto.username!!)

        if (user != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists!")
        } else {
            val newUser = userService.save(createUserDto)
            print(newUser.username)

            try {
                request.login(newUser.username, createUserDto.password)
            } catch (e: Exception) {
                e.printStackTrace()
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to login user!")
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!")
        }
    }

    @GetMapping("/me")
    fun home(principal: Principal): ResponseEntity<UserDto> {
        val user = userService.findByUsername(principal.name)!!

        return ResponseEntity.ok(user.asUserDto())
    }

    @PostMapping("/upload-profile-pic")
    fun handleFileUpload(@RequestParam("file") file: MultipartFile, principal: Principal): ResponseEntity<String>  {
        if (file.isEmpty) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty!");

        var user = userService.findByUsername(principal.name)!!;

        if (file.contentType != "image/png") {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only PNG files are allowed!");
        }

        try {
            file.bytes;
            var uploadedFile = File("./accounts/profiles/" + user.id + "_profile.png");

            file.transferTo(uploadedFile);
            userService.setHasProfilePic(user.id!!, true);

            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + file.originalFilename);
        } catch (_: IOException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file!");
        }
    }

    @DeleteMapping("/delete-profile-pic")
    fun deleteProfilePic(principal: Principal): ResponseEntity<String> {
        var user = userService.findByUsername(principal.name)!!;

        var uploadedFile = File("./accounts/profiles/" + user.id + "_profile.png");

        if (uploadedFile.exists()) {
            uploadedFile.delete();
            userService.setHasProfilePic(user.id!!, false);
            return ResponseEntity.status(HttpStatus.OK).body("Profile picture deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile picture not found!");
        }
    }

    @DeleteMapping("/delete")
    fun delete(principal: Principal, request: HttpServletRequest): ResponseEntity<String> {
        val user = userService.findByUsername(principal.name)!!

        userService.deleteById(user.id!!)

        request.logout()
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully!")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<List<ErrorBinding>> {
        val errMap: MutableList<ErrorBinding> = mutableListOf()

        e.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
            val fieldName = try {
                (error as FieldError).field
            } catch (_: ClassCastException) {
                error.objectName
            }

            val message = error.defaultMessage

            val binding = ErrorBinding(message!!, fieldName)
            errMap += binding
        })

        return ResponseEntity<List<ErrorBinding>>(errMap, HttpStatus.BAD_REQUEST)
    }
}