package com.MT24.BankingApplication.Dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequestDto {

    @NotBlank
    private String accountNumber;

    @NotBlank
    @ToString.Exclude
    private String password;
}


