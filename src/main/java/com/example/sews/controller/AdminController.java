package com.example.sews.controller;

import com.example.sews.dto.Admin;
import com.example.sews.service.AdminService;
import com.example.sews.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginAdmin(@RequestBody Map<String, String> credentials) {
        return adminService.login(credentials.get("username"), credentials.get("password"));
    }
    @PostMapping("/create")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin savedAdmin = adminService.saveAdmin(admin);
        return ResponseEntity.ok(savedAdmin);
    }

    @GetMapping("/id")
    public ResponseEntity<Admin> getAdminById(HttpServletRequest request)  {
        String token = request.getHeader("token");
        String adminId;
        try{
            Claims claims = JwtUtil.parseJWT(token);
            adminId = claims.getSubject();
        }catch (Exception e){
            throw new RuntimeException("Invalid token");
        }
        Optional<Admin> admin = adminService.findById(Long.valueOf(adminId));
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Admin> getAdminByUsername(@PathVariable String username) {
        Admin admin = adminService.findByUsername(username);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Admin> updateAdmin(@RequestBody Admin admin) {
        Admin updatedAdmin = adminService.updateAdmin(admin);
        return ResponseEntity.ok(updatedAdmin);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Admin>> deleteAdmin() {
        List<Admin> lists= adminService.getAllAdmin();
        if(lists.isEmpty()){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(lists);

    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
