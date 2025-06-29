package com.MT24.BankingApplication.Controller;

import com.MT24.BankingApplication.dto.UserResponseDto;
import com.MT24.BankingApplication.dto.UserRequestDto;
import com.MT24.BankingApplication.Dto.LoginRequestDto;
import com.MT24.BankingApplication.Dto.LoginResponseDto;
import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Repositoy.UserRepository;

import com.MT24.BankingApplication.Service.UserServiceImpl;
import com.MT24.BankingApplication.util.jwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {
@Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final jwtUtil jwtUtil;

    @Autowired
    UserServiceImpl userService;
    @Autowired
    private final UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getAccountNumber(),
                            request.getPassword()
                    )
            );

            User user = userRepository.findByAccountNumber(request.getAccountNumber())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtUtil.generateToken(user.getAccountNumber());

            return ResponseEntity.ok(new LoginResponseDto(
                    token,
                    user.getAccountNumber(),
                    user.getFirstName() + " " + user.getLastName()
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
    }
    @PostMapping("/register-admin")
    public ResponseEntity<UserResponseDto> registerAdmin(@RequestBody @Valid UserRequestDto dto) {
        try {
            return new ResponseEntity<>(userService.registerAdmin(dto), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }

}
