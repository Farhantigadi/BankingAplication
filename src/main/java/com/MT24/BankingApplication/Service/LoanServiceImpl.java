package com.MT24.BankingApplication.Service;

import com.MT24.BankingApplication.Dto.LoanApplicationRequest;
import com.MT24.BankingApplication.Model.Loan;
import com.MT24.BankingApplication.Model.LoanType;
import com.MT24.BankingApplication.Repositoy.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl {

    private final LoanRepository loanRepository;

    public Loan applyForLoan(String accountNumber, LoanApplicationRequest request) {
        double interestRate = calculateInterestRate(request.getLoanType());

        Loan loan = new Loan();
        loan.setAccountNumber(accountNumber);
        loan.setLoanType(request.getLoanType());
        loan.setAmount(request.getAmount());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setInterestRate(interestRate);
        loan.setStatus("PENDING");

        return loanRepository.save(loan);
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
