package com.ecommerce.backend.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.admin.dto.AdminDashboardResponse;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(
            AdminDashboardService adminDashboardService
    ) {
        this.adminDashboardService =
                adminDashboardService;
    }

    // ==========================================
    // Get Admin Dashboard Statistics
    // ==========================================

    @GetMapping
    public ResponseEntity<AdminDashboardResponse>
    getDashboardStats() {

        AdminDashboardResponse response =
                adminDashboardService
                        .getDashboardStats();

        return ResponseEntity.ok(response);
    }
}