package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.PatientNotFoundException;
import com.jordi125229.medicalclinic.exception.PatientsEmailAlreadyExists;
import com.jordi125229.medicalclinic.exception.WrongPasswordException;
import com.jordi125229.medicalclinic.model.command.CreateUserCommand;
import com.jordi125229.medicalclinic.model.dto.PageableDto;
import com.jordi125229.medicalclinic.model.dto.UserDto;
import com.jordi125229.medicalclinic.model.command.ChangePasswordCommand;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.mapper.UserMapper;
import com.jordi125229.medicalclinic.repository.PatientRepository;
import com.jordi125229.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PatientRepository patientRepository;

    public PageableDto<UserDto> getUsers(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDto> users = userRepository.findAll(pageable).stream()
                .map(userMapper::userToDto)
                .toList();
        return PageableDto.create(users, userPage);
    }

    public UserDto createUser(CreateUserCommand createUserCommand) {
        validateEmail(createUserCommand.getEmail());
        User user = new User(null, createUserCommand.getEmail(), createUserCommand.getPassword(), null, null);
        userRepository.save(user);
        return userMapper.userToDto(user);
    }

    private void validateEmail(String email) {
        Optional<User> userFoundByEmail = userRepository.findByEmail(email);
        if (userFoundByEmail.isPresent()) {
            throw new PatientsEmailAlreadyExists("User with this email already exists!", HttpStatus.CONFLICT);
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("User wasn't found!", HttpStatus.NOT_FOUND));
    }

    public void deleteUser(String email) {
        User user = getUserByEmail(email);
        userRepository.delete(user);
    }

    public void changePassword(String email, ChangePasswordCommand changePassword) {
        User user = getUserByEmail(email);
        if (!changePassword.getPassword().equals(user.getPassword())) {
            throw new WrongPasswordException("Wrong password!", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(changePassword.getNewPassword());
        userRepository.save(user);
    }
}
