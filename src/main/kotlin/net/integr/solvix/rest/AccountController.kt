package net.integr.solvix.rest

import jakarta.validation.Valid
import net.integr.solvix.db.user.User
import net.integr.solvix.db.user.UserService
import net.integr.solvix.dto.CreateUserDto
import net.integr.solvix.dto.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
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

@RestController
@CrossOrigin
@RequestMapping("/api/account")
class AccountController @Autowired constructor(var userService: UserService) {
    @PostMapping("/register")
    fun register(@Valid @RequestBody createUserDto: CreateUserDto): ResponseEntity<String> {
        val user: User? = userService.findByUsername(createUserDto.username!!)
        return if (user != null) {
            ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists!")
        } else {
            userService.save(createUserDto)
            ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully!")
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
    fun delete(principal: Principal): ResponseEntity<String> {
        val user = userService.findByUsername(principal.name)!!

        userService.deleteById(user.id!!)

        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully!")
    }
}