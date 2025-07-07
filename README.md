# 💰 Banking Application - Spring Boot REST API

A secure, role-based online banking backend built using **Java**, **Spring Boot**, and **JWT Authentication**. It supports user registration, login, money transfers, and loan management. Designed for both **Customer** and **Admin** roles.

---

## 📄 Description

This project simulates a core banking system. It includes complete REST APIs for:

- User registration & login
- Money transfers between users
- Loan application and repayment system
- Admin functionalities like user overview and loan approvals

---

## 🔧 Tech Stack

- Java 17
- Spring Boot 3
- Spring Security (JWT)
- JPA + Hibernate
- MySQL
- Swagger (OpenAPI)
- SonarCloud
- Postman (for API testing)

---

## 🔐 Key Features

### 👤 User
- Register and log in securely using JWT tokens
- View account info, balance, and loan status
- Deposit, withdraw, and transfer money
- Apply for different types of loans
- Repay loan with real-time updates

### 🛡️ Admin
- View total users, system-wide balance
- Approve or reject loan applications
- Create other admin accounts securely

---

## 📑 API Endpoints (Examples)

### 🔓 Public
- `POST /login` – Login with credentials
- `POST /public/register-user` – User Registration
- `POST /login/register-admin` – Admin Registration

### 👤 User
- `GET /user/info` – Get user account info
- `POST /user/deposit` – Deposit money
- `POST /user/withdraw` – Withdraw money
- `POST /user/transfer` – Transfer money
- `POST /user/loan/apply` – Apply for a loan
- `GET /user/loan/status` – View loan status
- `POST /user/loan/repay` – Repay loan amount

### 🛡️ Admin
- `GET /admin/summary/balance` – View total system money
- `GET /admin/summary/users` – View total number of users
- `GET /admin/loans/pending` – View pending loan applications
- `PUT /admin/loans/{loanId}/approve` – Approve a loan
- `PUT /admin/loans/{loanId}/reject` – Reject a loan

---


---

## 🗃️ Database Schema (Simplified)

- `User`: id, accountNumber, roles, balance, etc.
- `Loan`: id, accountNumber, loanType, amount, interestRate, status, amountPaid
- `Transaction`: id, fromAccount, toAccount, amount, timestamp

---

## 🖼️ Swagger UI

Visit: `http://localhost:8080/swagger-ui.html`

---

## 🚀 Getting Started

1. **Clone the repo**
```bash
git clone https://github.com/yourusername/banking-app.git
cd banking-app
