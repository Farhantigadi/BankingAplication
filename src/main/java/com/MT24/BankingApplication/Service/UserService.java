package com.MT24.BankingApplication.Service;

import com.MT24.BankingApplication.Dto.LoginResponseDto;


import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.dto.UserRequestDto;
import com.MT24.BankingApplication.dto.UserResponseDto;

public interface UserService {
    LoginResponseDto registerUser(UserRequestDto userRequestDto);
    UserResponseDto registerAdmin(UserRequestDto dto);
    User getUserInfo(String accountNumber);


    String deposit(String accountNumber, Double amount);
    String withdraw(String accountNumber, Double amount);
}

