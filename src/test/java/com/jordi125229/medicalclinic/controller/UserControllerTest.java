package com.jordi125229.medicalclinic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jordi125229.medicalclinic.model.command.ChangePasswordCommand;
import com.jordi125229.medicalclinic.model.command.CreateUserCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.UserDto;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;

    @Test
    void getUsers_DataCorrect_DataGotten() throws Exception {
        // given
        int size = 1;
        int page = 0;

        UserDto userDto = UserDto.builder()
                .userId(1L)
                .email("email")
                .build();

        List<UserDto> userDtoList = List.of(userDto);
        PageableDto<UserDto> userDtoPage = PageableDto.<UserDto>builder()
                .pageSize(size)
                .pageNumber(page)
                .totalPages(1)
                .content(userDtoList)
                .total(1L)
                .build();

        when(userService.getUsers(page, size)).thenReturn(userDtoPage);

        mockMvc.perform(get("/users")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber").value(page))
                .andExpect(jsonPath("$.pageSize").value(size))
                .andExpect(jsonPath("$.total").value(1));

        verify(userService).getUsers(page, size);
    }

    @Test
    void createUser_DataCorrect_UserCreated() throws Exception {
        // given
        CreateUserCommand createUserCommand = CreateUserCommand.builder()
                .email("email@gmail.com")
                .password("password")
                .build();

        UserDto user = UserDto.builder()
                .email("email@gmail.com")
                .build();

        when(userService.createUser(any())).thenReturn(user);

        // when & then
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserCommand)))
                .andExpect(jsonPath("$.email").value("email@gmail.com"));
    }

    @Test
    void editPassword_DataCorrect_PasswordChanged() throws Exception {
        // given
        ChangePasswordCommand changePassword = ChangePasswordCommand.builder()
                .password("password")
                .newPassword("newPassword")
                .build();

        // when & then
        mockMvc.perform(patch("/users/email/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePassword)))
                .andExpect(status().isNoContent());

        verify(userService).changePassword("email", changePassword);
    }

    @Test
    void deleteUser_DataCorrect_UserDeleted() throws Exception {
        mockMvc.perform(delete("/users/email"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser("email");
    }
}
