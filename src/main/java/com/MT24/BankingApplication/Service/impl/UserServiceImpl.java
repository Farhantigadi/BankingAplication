package com.MT24.BankingApplication.Service.impl;


import com.MT24.BankingApplication.Dto.LoginResponseDto;
import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Repositoy.UserRepository;
import com.MT24.BankingApplication.Service.UserService;
import com.MT24.BankingApplication.dto.UserRequestDto;
import com.MT24.BankingApplication.dto.UserResponseDto;

import com.MT24.BankingApplication.util.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    @Autowired
    private final UserRepository userRepository;
   @Autowired
   private final PasswordEncoder passwordEncoder;
    private UserResponseDto mapToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setAccountNumber(user.getAccountNumber());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFatherName(user.getFatherName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setPincode(user.getPincode());
        response.setAccountBalance(user.getAccountBalance());
        response.setAccountCreatedAt(user.getAccountCreatedAt());
        response.setAccountModifiedAt(user.getAccountModifiedAt());
        response.setRoles(user.getRoles());
        return response;
    }


    public LoginResponseDto registerUser(UserRequestDto dto) {
        if (userRepository.existsByAccountNumber(dto.getAccountNumber())) {
            throw new RuntimeException("Account number already exists");
        }
        // [validation checks...]

        User user = new User();
        user.setAccountNumber(dto.getAccountNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setFatherName(dto.getFatherName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPincode(dto.getPincode());
        user.setAccountBalance(dto.getAccountBalance());
        user.setRoles(List.of("USER"));
        user.setAccountCreatedAt(LocalDateTime.now());
        user.setAccountModifiedAt(LocalDateTime.now());

        userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(user.getAccountNumber());
        LoginResponseDto loginResponseDto = new LoginResponseDto(token, user.getAccountNumber(), user.getFirstName() + " " + user.getLastName());
        return loginResponseDto;
    }

    public UserResponseDto registerAdmin(UserRequestDto dto) {


        User user = new User();
        user.setAccountNumber(dto.getAccountNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setFatherName(dto.getFatherName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPincode(dto.getPincode());
        user.setAccountBalance(dto.getAccountBalance());
        user.setRoles(List.of("ADMIN"));

        userRepository.save(user);

        return mapToResponse(user);
    }




}

