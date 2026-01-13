package com.MT24.BankingApplication.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, name = "first_name", length = 50)
    private String firstName;

    @Column(nullable = false, name = "last_name", length = 50)
    private String lastName;

    @Column(nullable = false, name = "father_name", length = 50)
    private String fatherName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(length = 6)
    private Long pincode;

    @Column(name = "account_balance", nullable = false)
    private Double accountBalance;

    @Column(name = "phone_number", unique = true, nullable = false, length = 15)
    private String phoneNumber;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime accountCreatedAt;

    @UpdateTimestamp
    private LocalDateTime accountModifiedAt;

     @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();
}
/*
@ElementCollection: Indicates a collection of simple values (not entities).

@CollectionTable: Creates a user_roles table with foreign key user_id.

@Column(name = "role"): Each role value will be stored as a row in that table.
 */