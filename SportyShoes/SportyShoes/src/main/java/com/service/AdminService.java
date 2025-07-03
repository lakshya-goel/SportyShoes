package com.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bean.Admin;
import com.dao.AdminRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public Admin authenticateAdmin(String username, String password) {
        Admin admin = adminRepository.findByUsername(username);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return admin;
        }
        return null;
    }
    
    public void updateLastLogin(int adminId) {
        adminRepository.updateLastLogin(adminId, LocalDateTime.now());
    }
    
    public boolean changePassword(int adminId, String currentPassword, String newPassword) {
        Admin admin = adminRepository.findById(adminId);
        if (admin != null && passwordEncoder.matches(currentPassword, admin.getPassword())) {
            String encodedNewPassword = passwordEncoder.encode(newPassword);
            adminRepository.updatePassword(adminId, encodedNewPassword);
            return true;
        }
        return false;
    }
    
    public Admin createAdmin(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        return adminRepository.save(admin);
    }
    
    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }
}
