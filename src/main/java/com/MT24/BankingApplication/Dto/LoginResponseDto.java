package com.MT24.BankingApplication.Dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String accountNumber;
    private String fullName;
}
