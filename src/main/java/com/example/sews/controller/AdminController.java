package com.example.sews.controller;

import com.example.sews.dto.Admin;
import com.example.sews.dto.anno.LogOperation;
import com.example.sews.service.AdminService;
import com.example.sews.service.UserService;
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

    @LogOperation("管理员登录")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginAdmin(@RequestBody Map<String, String> credentials) {
        return adminService.login(credentials.get("username"), credentials.get("password"));
    }
    @LogOperation("创建管理员")
    @PostMapping("/create")
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        Admin savedAdmin = adminService.saveAdmin(admin);
        return ResponseEntity.ok(savedAdmin);
    }
    @LogOperation("更新管理员")
    @PostMapping("/update")
    public ResponseEntity<Admin> updateAdmin(@RequestBody Admin admin) {
        Admin updatedAdmin = adminService.updateAdmin(admin);
        return ResponseEntity.ok(updatedAdmin);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Admin> getAdminByUsername(@PathVariable String username) {
        Admin admin = adminService.findByUsername(username);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }



    @GetMapping("/list")
    public ResponseEntity<List<Admin>> getAllAdmin() {
        List<Admin> lists= adminService.getAllAdmin();
        if(lists.isEmpty()){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(lists);

    }

    @GetMapping("/userList")
    public ResponseEntity<List<Admin>> userList() {
        List<Admin> lists= adminService.getAllUser();
        if(lists.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lists);
    }

    @LogOperation("删除管理员")
    @GetMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Integer id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }


}
