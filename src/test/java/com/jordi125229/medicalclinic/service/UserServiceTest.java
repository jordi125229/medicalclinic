package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.WrongPasswordException;
import com.jordi125229.medicalclinic.model.command.ChangePasswordCommand;
import com.jordi125229.medicalclinic.model.command.CreateUserCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.UserDto;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.mapper.UserMapper;
import com.jordi125229.medicalclinic.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        this.userRepository = Mockito.mock(UserRepository.class);
        this.userMapper = Mappers.getMapper(UserMapper.class);
        this.userService = new UserService(userRepository, userMapper);
    }

    @Test
    void getUsers_DataCorrect_UsersReturned() {
        // given
        int pageNumber = 0;
        int pageSize = 1;

        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        List<User> users = List.of(user);
        Page<User> usersPage = new PageImpl<>(users, pageRequest, users.size());
        when(userRepository.findAll(pageRequest)).thenReturn(usersPage);

        // when
        PageableDto<UserDto> usersDtoPage = userService.getUsers(pageNumber, pageSize);

        // then
        assertAll(
                () -> assertEquals(pageNumber, usersDtoPage.getPageNumber()),
                () -> assertEquals(pageSize, usersDtoPage.getPageSize()),
                () -> assertEquals(users.size(), usersDtoPage.getContent().size()),
                () -> assertEquals("email", usersDtoPage.getContent().getFirst().getEmail())
        );
    }

    @Test
    void createUser_DataCorrect_UserCreated() {
        // given
        CreateUserCommand createUserCommand = CreateUserCommand.builder()
                .email("email")
                .password("password")
                .build();
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        // when
        UserDto userDto = userService.createUser(createUserCommand);

        // then
        assertAll(
                () -> assertEquals("email", userDto.getEmail())
        );
    }

    @Test
    void changePassword_DataCorrect_PasswordChanged() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        ChangePasswordCommand changePasswordCommand = ChangePasswordCommand.builder()
                .password("password")
                .newPassword("newPassword")
                .build();
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(user));

        // when
        userService.changePassword("email", changePasswordCommand);

        // then
        assertAll(
                () -> assertEquals("newPassword", user.getPassword())
        );
    }

    @Test
    void changePassword_DataIncorrect_WrongPassword() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();

        ChangePasswordCommand changePasswordCommand = ChangePasswordCommand.builder()
                .password("wrongPassword")
                .newPassword("newPassword")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // when
        WrongPasswordException exception = assertThrows(WrongPasswordException.class, () -> userService.changePassword("email", changePasswordCommand));

        // then
        assertAll(
                () -> assertEquals("Wrong password!", exception.getMessage()),
                () -> assertEquals(400, exception.getStatus().value())
        );
    }

    @Test
    void deleteUser_DataCorrect_UserDeleted() {
        // given
        User user = User.builder()
                .email("email")
                .password("password")
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // when
        userService.deleteUser("email");

        // then
        verify(userRepository).delete(user);
        verify(userRepository).findByEmail("email");
        verifyNoMoreInteractions(userRepository);
    }

}
