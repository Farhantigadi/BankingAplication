package com.MT24.BankingApplication.Service;

import com.MT24.BankingApplication.Model.User;
import com.MT24.BankingApplication.Repositoy.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class UserService {

    @Autowired
    User user;

    @Autowired
    UserRepo userRepo;

    public void createUser(User user){
       userRepo.save(user);
    }
}
