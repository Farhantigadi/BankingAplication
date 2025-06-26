package com.MT24.BankingApplication.Repositoy;

import com.MT24.BankingApplication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByRolesContaining(String role);

    Optional<User> findByAccountNumber(String accountNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByAccountNumber(String accountNumber);
}

