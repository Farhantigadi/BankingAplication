package com.MT24.BankingApplication.Repositoy;

import com.MT24.BankingApplication.Model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByAccountNumber(String accountNumber);
}
