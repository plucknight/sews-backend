package com.example.sews.service;

import com.example.sews.dto.Admin;
import com.example.sews.repo.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // Save a new admin
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    // Find admin by ID
    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    // Find admin by username
    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    // Update an existing admin
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    // Delete an admin
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public List<Admin> getAllAdmin()  {
        return adminRepository.findAll();
    }
}
