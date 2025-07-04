package com.MT24.BankingApplication.Controller;

import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Service.UserService;
import com.MT24.BankingApplication.util.jwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;
import java.util.RandomAccess;

@Tag(name = "User APIs", description = "Operations for authenticated users like balance, deposit, transfer, etc.")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final jwtUtil jwtUtil;

    @Operation(summary = "Get user info", description = "Fetches authenticated user's profile data using JWT")
    @GetMapping("/info")
    public ResponseEntity<User> getUserInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        User user = userService.getUserInfo(accountNumber);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Deposit money", description = "Deposits specified amount to user's account")
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> payload) {

        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        Double amount = Double.parseDouble(payload.get("amount").toString());
        String message = userService.deposit(accountNumber, amount);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "Withdraw money", description = "Withdraws specified amount from user's account")
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawMoney(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Double> requestBody) {

        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        Double amount = requestBody.get("amount");
        String response = userService.withdraw(accountNumber, amount);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Transfer money", description = "Transfers specified amount from sender to receiver account")
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> payload) {

        String token = authHeader.substring(7);
        String senderAccount = jwtUtil.extractUsername(token);
        String receiverAccount = payload.get("receiverAccountNumber").toString();
        Double amount = Double.parseDouble(payload.get("amount").toString());
        String result = userService.transferMoney(senderAccount, receiverAccount, amount);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Get account balance", description = "Returns current account balance for authenticated user")
    @GetMapping("/balance")
    public ResponseEntity<String> getBalance(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        String message = userService.getBalance(accountNumber);
        return ResponseEntity.ok(message);
    }
}
