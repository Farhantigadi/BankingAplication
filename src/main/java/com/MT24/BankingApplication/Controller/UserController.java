package com.MT24.BankingApplication.Controller;

import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Service.UserService;
import com.MT24.BankingApplication.util.jwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final jwtUtil jwtUtil;

    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }

        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        User user = userService.getUserInfo(accountNumber);
        return ResponseEntity.ok(user);
    }
}
