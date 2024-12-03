package net.integr.solvix.dto

class UserDto(
    var id: String?,
    var username: String,
    var firstname: String,
    var lastname: String,
    var email: String,
    var termsAccepted: Boolean,
    var joinDate: String,
    var hasProfilePicture: Boolean
)