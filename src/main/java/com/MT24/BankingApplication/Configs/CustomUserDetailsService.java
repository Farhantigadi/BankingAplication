package com.MT24.BankingApplication.Configs;

import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Repositoy.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
        User user = userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with account number: " + accountNumber));

        return new CustomUserDetails(user);
    }
}
