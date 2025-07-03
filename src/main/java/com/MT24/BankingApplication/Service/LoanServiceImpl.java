package com.MT24.BankingApplication.Service;

import com.MT24.BankingApplication.Dto.LoanApplicationRequest;
import com.MT24.BankingApplication.Model.Loan;
import com.MT24.BankingApplication.Model.LoanType;
import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Repositoy.LoanRepository;
import com.MT24.BankingApplication.Repositoy.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl {

    private final LoanRepository loanRepository;

    private final UserRepository userRepository;

    public Loan applyForLoan(String accountNumber, LoanApplicationRequest request) {
        // Get all existing loans for this account
        List<Loan> existingLoans = loanRepository.findByAccountNumber(accountNumber);

        // Check for duplicate active non-education loans
        if (request.getLoanType() != LoanType.EDUCATION) {
            boolean hasActiveNonEduLoan = existingLoans.stream()
                    .anyMatch(loan ->
                            (loan.getLoanType() != LoanType.EDUCATION) &&
                                    (loan.getStatus().equals("PENDING") || loan.getStatus().equals("APPROVED"))
                    );

            if (hasActiveNonEduLoan) {
                throw new IllegalStateException("You already have an active loan. You can't apply for another unless it's an education loan.");
            }
        }

        // Set interest rate based on loan type
        double interestRate = calculateInterestRate(request.getLoanType());

        Loan loan = new Loan();
        loan.setAccountNumber(accountNumber);
        loan.setLoanType(request.getLoanType());
        loan.setAmount(request.getAmount());
        loan.setInterestRate(interestRate);
        loan.setTenureMonths(request.getTenureMonths());
        loan.setStatus("PENDING");

        return loanRepository.save(loan);
    }

    public String repayLoan(String accountNumber, Double amount) {
        // 🔒 Only allow repayment if the loan is APPROVED
        List<Loan> loans = loanRepository.findByAccountNumber(accountNumber).stream()
                .filter(loan -> loan.getStatus().equals("APPROVED"))
                .toList();

        if (loans.isEmpty()) {
            throw new IllegalStateException("No approved loan found for this account.");
        }

        Loan loan = loans.get(0); // Pick the first approved loan

        double currentPaid = loan.getAmountPaid() == null ? 0.0 : loan.getAmountPaid();
        double newPaid = currentPaid + amount;

        // 🚫 Prevent overpayment
        if (newPaid > loan.getAmount()) {
            throw new IllegalStateException("Repayment exceeds remaining loan amount.");
        }

        loan.setAmountPaid(newPaid);

        // Optional: Mark as PAID if fully repaid
        if (newPaid >= loan.getAmount()) {
            loan.setStatus("PAID");
        }

        loanRepository.save(loan); // 💾 Save updated loan

        double remaining = loan.getAmount() - newPaid;
        return String.format("Payment of ₹%.2f received. Remaining loan amount: ₹%.2f", amount, remaining);
    }




    public List<Loan> getLoansByAccount(String accountNumber) {
        return loanRepository.findByAccountNumber(accountNumber);
    }

    private double calculateInterestRate(LoanType type) {
        return switch (type) {
            case EDUCATION -> 5.0;
            case HOME -> 10.0;
            case BUSINESS -> 11.0;
            case PERSONAL -> 12.0;
        };
    }
}


