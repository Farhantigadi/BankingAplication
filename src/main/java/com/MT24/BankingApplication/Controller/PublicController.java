package com.MT24.BankingApplication.Controller;

import com.MT24.BankingApplication.Dto.LoginResponseDto;
import com.MT24.BankingApplication.Service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.MT24.BankingApplication.dto.UserRequestDto;

@RestController
@RequestMapping("public")
public class PublicController {
    @Autowired
    UserServiceImpl userService;
    @PostMapping("/register-user")
    public ResponseEntity<LoginResponseDto> registerUser(@RequestBody @Valid UserRequestDto dto) {
        try {
            return new ResponseEntity<>(userService.registerUser(dto), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

}
