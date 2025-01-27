package com.example.sews.service;

import com.example.sews.dto.Admin;
import com.example.sews.dto.LoginUser;
import com.example.sews.repo.AdminRepository;
import com.example.sews.utils.JwtUtil;
import jakarta.servlet.http.Cookie;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public ResponseEntity<Map<String, String>>    login (String username, String password) {
        System.out.println("username = " + username+" password = "+password);

        Admin admin   = adminRepository.findByUsername(username);
        if (admin == null || !password.equals(admin.getPassword())) {
            return ResponseEntity.notFound().build();
        }
        //存放token到cookie中，最好时直接返回json
        Map<String, String> map = new HashMap<>();
        String id = admin.getId().toString();
        String jwt = JwtUtil.createJWT(id);
        map.put("token", jwt);
        return ResponseEntity.ok().body(map);
    }

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public List<Admin> getAllAdmin()  {
        return adminRepository.findAll();
    }
}
