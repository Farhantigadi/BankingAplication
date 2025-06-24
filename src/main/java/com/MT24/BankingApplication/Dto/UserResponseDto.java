package com.MT24.BankingApplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String accountNumber;
    private String firstName;
    private String lastName;
    private String fatherName;
    private String email;
    private String phoneNumber;
    private Long pincode;
    private Double accountBalance;
    private LocalDateTime accountCreatedAt;
    private LocalDateTime accountModifiedAt;
}
