package com.MT24.BankingApplication.Controller;

import com.MT24.BankingApplication.Dto.LoanApplicationRequest;
import com.MT24.BankingApplication.Model.Loan;
import com.MT24.BankingApplication.Service.LoanServiceImpl;
import com.MT24.BankingApplication.util.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/loan")
@RequiredArgsConstructor
public class LoanRequests {

    private final LoanServiceImpl loanService;
    private final jwtUtil jwtUtil;

    @PostMapping("/apply")
    public ResponseEntity<Loan> applyForLoan(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LoanApplicationRequest request) {

        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        Loan loan = loanService.applyForLoan(accountNumber, request);
        return ResponseEntity.ok(loan);
    }
    @GetMapping("/status")
    public ResponseEntity<List<Loan>> viewLoanStatus(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        List<Loan> loans = loanService.getLoansByAccount(accountNumber);
        return ResponseEntity.ok(loans);
    }

}
