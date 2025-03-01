package com.example.sews.service;

import com.example.sews.dto.Admin;
import com.example.sews.repo.AdminRepository;
import com.example.sews.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public ResponseEntity<Map<String, String>>    login (String username, String password) {
//        System.out.println("username = " + username+" password = "+password);
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
        Optional<Admin> admin=adminRepository.findById(id);
        admin.get().setPassword("******");
        return admin;
    }

    public Admin findByUsername(String username) {
        Admin admin=adminRepository.findByUsername(username);
        admin.setPassword("******");
        return admin;
    }

    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public List<Admin> getAllAdmin()  {
        List<Admin> admins = adminRepository.findAll();
        admins.forEach(admin -> admin.setPassword("******"));
        return admins;
    }
}
