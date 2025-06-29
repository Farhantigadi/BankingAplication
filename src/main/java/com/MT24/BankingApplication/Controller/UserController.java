package com.MT24.BankingApplication.Controller;

import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Service.UserService;
import com.MT24.BankingApplication.util.jwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {


    private final  UserService userService;
    private final jwtUtil jwtUtil;



    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(HttpServletRequest request) {  //HttpServletRequest gives access to HTTP headers, like Authorization.
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);  //🔐 Whatever you use to log in with (in your case, accountNumber), that same value is stored inside the JWT token as the subject, and that’s what gets extracted later.
        User user = userService.getUserInfo(accountNumber);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> payload
    ) {
        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);

        Double amount = Double.parseDouble(payload.get("amount").toString());
        String message = userService.deposit(accountNumber, amount);
        return ResponseEntity.ok(message);
    }
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawMoney(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Double> requestBody
    ) {
        String token = authHeader.substring(7); // Remove "Bearer "
        String accountNumber = jwtUtil.extractUsername(token);
        Double amount = requestBody.get("amount");

        String response = userService.withdraw(accountNumber, amount);
        return ResponseEntity.ok(response);
    }

    }
