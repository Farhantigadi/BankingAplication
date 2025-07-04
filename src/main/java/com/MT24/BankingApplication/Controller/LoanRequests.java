package com.MT24.BankingApplication.Controller;

import com.MT24.BankingApplication.Dto.LoanApplicationRequest;
import com.MT24.BankingApplication.Dto.LoanRepaymentRequest;
import com.MT24.BankingApplication.Model.Loan;
import com.MT24.BankingApplication.Service.LoanServiceImpl;
import com.MT24.BankingApplication.util.jwtUtil;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Apply for loan", description = "Allows a user to apply for a loan by providing necessary details")
    public ResponseEntity<Loan> applyForLoan(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LoanApplicationRequest request) {

        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        Loan loan = loanService.applyForLoan(accountNumber, request);
        return ResponseEntity.ok(loan);
    }
    @GetMapping("/status")
    @Operation(summary = "View loan status", description = "Retrieves the status of loans associated with the authenticated user")
    public ResponseEntity<List<Loan>> viewLoanStatus(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        List<Loan> loans = loanService.getLoansByAccount(accountNumber);
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/repay")
    @Operation(summary = "Repay loan", description = "Repays an existing loan by deducting the specified amount from user's balance")
    public ResponseEntity<String> repayLoan(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LoanRepaymentRequest request
    ) {
        String token = authHeader.substring(7);
        String accountNumber = jwtUtil.extractUsername(token);
        String result = loanService.repayLoan(accountNumber, request.getAmount());
        return ResponseEntity.ok(result);
    }




}
