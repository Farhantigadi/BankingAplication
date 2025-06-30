package com.MT24.BankingApplication.Dto;

import com.MT24.BankingApplication.Model.LoanType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanApplicationRequest {
    private LoanType loanType;
    private Double amount;
    private Integer tenureMonths;
}
