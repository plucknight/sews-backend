package com.example.sews.security;

import com.example.sews.dto.Admin;
import com.example.sews.dto.LoginUser;
import com.example.sews.repo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private AdminRepository adminRepository ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("username = " + username);
        Admin admin = adminRepository.findByUsername(username);
        if (Objects.isNull(admin)) {
            throw new UsernameNotFoundException("User not found");
        }
        return  new  LoginUser(admin);
    }
}
