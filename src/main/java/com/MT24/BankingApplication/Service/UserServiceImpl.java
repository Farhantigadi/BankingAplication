package com.MT24.BankingApplication.Service;


import com.MT24.BankingApplication.Dto.LoginResponseDto;
import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Repositoy.UserRepository;
import com.MT24.BankingApplication.Service.UserService;
import com.MT24.BankingApplication.dto.UserRequestDto;
import com.MT24.BankingApplication.dto.UserResponseDto;

import com.MT24.BankingApplication.util.jwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    jwtUtil jwtUtil;
    @Autowired
    private final UserRepository userRepository;
   @Autowired
   private final PasswordEncoder passwordEncoder;
    private UserResponseDto mapToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setAccountNumber(user.getAccountNumber());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setFatherName(user.getFatherName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setPincode(user.getPincode());
        response.setAccountBalance(user.getAccountBalance());
        response.setAccountCreatedAt(user.getAccountCreatedAt());
        response.setAccountModifiedAt(user.getAccountModifiedAt());
        response.setRoles(user.getRoles());
        return response;
    }


    public LoginResponseDto registerUser(UserRequestDto dto) {
        if (userRepository.existsByAccountNumber(dto.getAccountNumber())) {
            throw new RuntimeException("Account number already exists");
        }
        // [validation checks...]

        User user = new User();
        user.setAccountNumber(dto.getAccountNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setFatherName(dto.getFatherName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPincode(dto.getPincode());
        user.setAccountBalance(dto.getAccountBalance());
        user.setRoles(List.of("USER"));
        user.setAccountCreatedAt(LocalDateTime.now());
        user.setAccountModifiedAt(LocalDateTime.now());

        userRepository.save(user);

        // Generate token
        String token = jwtUtil.generateToken(user.getAccountNumber());
        LoginResponseDto loginResponseDto = new LoginResponseDto(token, user.getAccountNumber(), user.getFirstName() + " " + user.getLastName());
        return loginResponseDto;
    }

    public UserResponseDto registerAdmin(UserRequestDto dto) {


        User user = new User();
        user.setAccountNumber(dto.getAccountNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setFatherName(dto.getFatherName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPincode(dto.getPincode());
        user.setAccountBalance(dto.getAccountBalance());
        user.setRoles(List.of("ADMIN"));

        userRepository.save(user);

        return mapToResponse(user);
    }

    @Override
    public User getUserInfo(String accountNumber) {
        return userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public String deposit(String accountNumber, Double amount) {
        User user = userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        user.setAccountBalance(user.getAccountBalance() + amount);
        user.setAccountModifiedAt(LocalDateTime.now());
        userRepository.save(user);

        return "₹" + amount + " deposited. New balance: ₹" + user.getAccountBalance();
    }
    public String withdraw(String accountNumber, Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        User user = userRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAccountBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        double oldBalance = user.getAccountBalance();
        user.setAccountBalance(oldBalance - amount);
        userRepository.save(user);

        return "Withdrawn ₹" + amount + " successfully. Remaining balance: ₹" + user.getAccountBalance();
    }

    public String transferMoney(String senderAcc, String receiverAcc, Double amount) {
        if (amount <= 0) {
            throw new RuntimeException("Transfer amount must be greater than 0");
        }

        User sender = userRepository.findByAccountNumber(senderAcc)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findByAccountNumber(receiverAcc)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getAccountBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        // Perform transfer
        sender.setAccountBalance(sender.getAccountBalance() - amount);
        receiver.setAccountBalance(receiver.getAccountBalance() + amount);

        sender.setAccountModifiedAt(LocalDateTime.now());
        receiver.setAccountModifiedAt(LocalDateTime.now());

        userRepository.save(sender);
        userRepository.save(receiver);

        return "Transferred ₹" + amount + " from " + senderAcc + " to " + receiverAcc +
                ". Remaining balance: ₹" + sender.getAccountBalance();
    }
        @Override
        public String getBalance(String accountNumber) {
            User user = userRepository.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return "Dear " + user.getFirstName() + ", your current account balance is ₹" + user.getAccountBalance();
        }


}












