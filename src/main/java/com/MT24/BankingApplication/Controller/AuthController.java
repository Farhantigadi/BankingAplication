package com.MT24.BankingApplication.Controller;

import com.MT24.BankingApplication.Model.Loan;
import com.MT24.BankingApplication.Repositoy.LoanRepository;
import com.MT24.BankingApplication.dto.UserResponseDto;
import com.MT24.BankingApplication.dto.UserRequestDto;
import com.MT24.BankingApplication.Dto.LoginRequestDto;
import com.MT24.BankingApplication.Dto.LoginResponseDto;
import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Repositoy.UserRepository;

import com.MT24.BankingApplication.Service.UserServiceImpl;
import com.MT24.BankingApplication.util.jwtUtil;
import io.swagger.v3.oas.annotations.Operation;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class AuthController {
@Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final jwtUtil jwtUtil;
    @Autowired
    private final LoanRepository loanRepository;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    UserServiceImpl userService;


    @PostMapping("")
    @Operation(summary = "User login", description = "Authenticates user and returns JWT token")
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
    @Operation(summary = "Register admin", description = "Registers a new admin account")
    public ResponseEntity<UserResponseDto> registerAdmin(@RequestBody @Valid UserRequestDto dto) {
        try {
            return new ResponseEntity<>(userService.registerAdmin(dto), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        }
    }
    // 🔍 1. View all loan applications
    @GetMapping("/loans/all")
    @Operation(summary = "View all loans", description = "Returns a list of all loan applications")
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanRepository.findAll());
    }

    // 🔍 2. View only pending loans
    @GetMapping("/loans/pending")
    @Operation(summary = "View pending loans", description = "Fetches loans that are currently pending")
    public ResponseEntity<List<Loan>> getPendingLoans() {
        List<Loan> pendingLoans = loanRepository.findAll()
                .stream()
                .filter(loan -> loan.getStatus().equalsIgnoreCase("PENDING"))
                .collect(Collectors.toList());
        return ResponseEntity.ok(pendingLoans);
    }

    // ✅ 3. Approve a loan
    @PostMapping("/loans/{id}/approve")
    @Operation(summary = "Approve loan", description = "Approves a loan if it's in pending status")
    public ResponseEntity<String> approveLoan(@PathVariable Long id) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if (optionalLoan.isEmpty()) return ResponseEntity.badRequest().body("Loan not found");

        Loan loan = optionalLoan.get();
        if (!loan.getStatus().equals("PENDING")) return ResponseEntity.badRequest().body("Loan already processed");

        loan.setStatus("APPROVED");
        loanRepository.save(loan);
        return ResponseEntity.ok("Loan approved successfully");
    }

    // ❌ 4. Reject a loan
    @PostMapping("/loans/{id}/reject")
    @Operation(summary = "Reject loan", description = "Rejects a loan if it's in pending status")
    public ResponseEntity<String> rejectLoan(@PathVariable Long id) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if (optionalLoan.isEmpty()) return ResponseEntity.badRequest().body("Loan not found");

        Loan loan = optionalLoan.get();
        if (!loan.getStatus().equals("PENDING")) return ResponseEntity.badRequest().body("Loan already processed");

        loan.setStatus("REJECTED");
        loanRepository.save(loan);
        return ResponseEntity.ok("Loan rejected successfully");
    }

    // 📊 5. Total users
    @GetMapping("/summary/users")
    @Operation(summary = "User count summary", description = "Returns total number of registered users")
    public ResponseEntity<String> getUserCount() {
        long count = userRepository.count();
        return ResponseEntity.ok("Total users: " + count);
    }

    // 💰 6. Total balance in system
    @GetMapping("/summary/balance")
    @Operation(summary = "Total system balance", description = "Calculates and returns total account balance in system")
    public ResponseEntity<String> getTotalBalance() {
        double total = userRepository.findAll()
                .stream()
                .mapToDouble(User::getAccountBalance)
                .sum();
        return ResponseEntity.ok(String.format("Total money in system: ₹%,.2f", total));
    }

    // 📋 7. Loan summary stats
    @GetMapping("/summary/loans")
    @Operation(summary = "Loan summary stats", description = "Returns loan status breakdown including total count")
    public ResponseEntity<Map<String, Long>> getLoanSummary() {
        List<Loan> loans = loanRepository.findAll();

        Map<String, Long> summary = loans.stream()
                .collect(Collectors.groupingBy(Loan::getStatus, Collectors.counting()));

        summary.put("TOTAL", (long) loans.size());

        return ResponseEntity.ok(summary);
    }

}
/*
Pagination is a technique used to split large sets of data into smaller, manageable chunks (pages) when sending them to the frontend (UI, Postman, etc.).
🔧 Example
Let’s say you have 5,000 loans in your database.
If an admin hits /admin/loans/all, returning all 5000 loans in one response would:

⛔ Slow down backend

⛔ Crash frontend/browser

⛔ Waste memory and network

✅ Solution: Use Pagination
Instead of returning all loans at once, we return them in pages:

Example API Call:
h
Copy
Edit
GET /admin/loans/all?page=0&size=10
 */