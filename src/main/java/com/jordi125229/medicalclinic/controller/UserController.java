package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.exception.ErrorMessage;
import com.jordi125229.medicalclinic.model.command.CreateUserCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.UserDto;
import com.jordi125229.medicalclinic.model.command.ChangePasswordCommand;
import com.jordi125229.medicalclinic.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get all users.", description = "Shows information about users.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping
    public PageableDto<UserDto> getUsers(@RequestParam int page, @RequestParam int size) {
        return userService.getUsers(page, size);
    }

    @Operation(summary = "Creating user.", description = "Take the information from the client and create user basing on input.")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Wrong user data.", content = @Content(mediaType = "aplication/json",
                    schema = @Schema(implementation = ErrorMessage.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody CreateUserCommand user) {
        return userService.createUser(user);
    }

    @Operation(summary = "Editing user's password.", description = "Client provide user's email, " +
            "user's being found and then client can change user's password by providing the current one and the new one.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User's password changed."),
            @ApiResponse(responseCode = "400", description = "Wrong password data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PatchMapping("/{email}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editPassword(@PathVariable("email") String email, @Valid @RequestBody ChangePasswordCommand changePassword) {
        userService.changePassword(email, changePassword);
    }

    @Operation(summary = "Deleting user.", description = "Deleting user by providing its email.")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "User deleted."),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessage.class)))})
    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }
}
