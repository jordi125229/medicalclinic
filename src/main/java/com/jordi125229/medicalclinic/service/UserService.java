package com.jordi125229.medicalclinic.service;

import com.jordi125229.medicalclinic.exception.PatientNotFoundException;
import com.jordi125229.medicalclinic.exception.PatientsEmailAlreadyExists;
import com.jordi125229.medicalclinic.exception.WrongPasswordException;
import com.jordi125229.medicalclinic.model.dto.UserDto;
import com.jordi125229.medicalclinic.model.ChangePassword;
import com.jordi125229.medicalclinic.model.entity.User;
import com.jordi125229.medicalclinic.model.mapper.UserMapper;
import com.jordi125229.medicalclinic.repository.PatientRepository;
import com.jordi125229.medicalclinic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::userToDto)
                .toList();
    }

    public UserDto createUser(User user) {
        validateEmail(user.getEmail());
        userRepository.save(user);
        return userMapper.userToDto(user);
    }

    private void validateEmail(String email) {
        Optional<User> userFoundByEmail = userRepository.findByEmail(email);
        if (userFoundByEmail.isPresent()) {
            throw new PatientsEmailAlreadyExists("User with this email already exists!", HttpStatus.CONFLICT);
        }
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new PatientNotFoundException("User wasn't found!", HttpStatus.NOT_FOUND));
    }

    public void deleteUser(String email) {
        User userByEmail = getUserByEmail(email);
        userRepository.delete(userByEmail);
    }

    public void changePassword(String email, ChangePassword changePassword) {
        User user = getUserByEmail(email);
        if (changePassword.getPassword().equals(user.getPassword())) {
            user.setPassword(changePassword.getNewPassword());
        } else {
            throw new WrongPasswordException("Wrong password!", HttpStatus.BAD_REQUEST);
        }
        userRepository.save(user);
    }
}
