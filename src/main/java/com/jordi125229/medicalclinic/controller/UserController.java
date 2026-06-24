package com.jordi125229.medicalclinic.controller;

import com.jordi125229.medicalclinic.model.command.CreateUserCommand;
import com.jordi125229.medicalclinic.model.dto.UserDto;
import com.jordi125229.medicalclinic.model.command.ChangePasswordCommand;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody CreateUserCommand user) {
        return userService.createUser(user);
    }

    @PatchMapping("/{email}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editPassword(@PathVariable("email") String email, @Valid @RequestBody ChangePasswordCommand changePassword) {
        userService.changePassword(email, changePassword);
    }

    @DeleteMapping("/{email}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }
}
