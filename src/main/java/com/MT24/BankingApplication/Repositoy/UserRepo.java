package com.MT24.BankingApplication.Repositoy;

import com.MT24.BankingApplication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByAccountNumber(String accountNumber);

}
