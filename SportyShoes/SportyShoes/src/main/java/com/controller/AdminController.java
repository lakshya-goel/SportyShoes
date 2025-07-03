package com.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bean.Admin;
import com.bean.Category;
import com.bean.Order;
import com.bean.Product;
import com.bean.User;
import com.service.AdminService;
import com.service.CategoryService;
import com.service.OrderService;
import com.service.ProductService;
import com.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private OrderService orderService;
    
    // Admin Login Page
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin/login";
    }
    
    // Admin Login Process
    @SuppressWarnings("unused")
	@PostMapping("/login")
    public String login(@RequestParam String username,  
                       @RequestParam String password, 
                       HttpSession session, 
                       RedirectAttributes redirectAttributes) {
        
        Admin admin = adminService.authenticateAdmin(username, password);
        
        if (admin != null) {
            session.setAttribute("admin", admin);
            adminService.updateLastLogin(admin.getAdminId());
            return "redirect:/admin/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/admin/login";
        }
    }
    
    // Admin Dashboard
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        // Dashboard statistics
        long totalProducts = productService.getTotalProductCount();
        long totalUsers = userService.getTotalUserCount();
        long totalOrders = orderService.getTotalOrderCount();
        float totalRevenue = orderService.getTotalRevenue();
        
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        
        // Recent orders
        List<Order> recentOrders = orderService.getRecentOrders(5);
        model.addAttribute("recentOrders", recentOrders);
        
        return "admin/dashboard";
    }
    
    // Admin Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
    
    // Change Password Page
    @GetMapping("/change-password")
    public String changePasswordPage(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        return "admin/change-password";
    }
    
    // Change Password Process
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                               @RequestParam String newPassword,
                               @RequestParam String confirmPassword,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }
        
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New passwords do not match");
            return "redirect:/admin/change-password";
        }
        
        if (adminService.changePassword(admin.getAdminId(), currentPassword, newPassword)) {
            redirectAttributes.addFlashAttribute("success", "Password changed successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect");
        }
        
        return "redirect:/admin/change-password";
    }
    
    // Product Management
    @GetMapping("/products")
    public String manageProducts(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        List<Product> products = productService.findAllProducts();
        List<Category> categories = categoryService.findAllCategories();
        
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("product", new Product());
        
        return "admin/products";
    }
    
    // Add Product
    @PostMapping("/products/add")
    public String addProduct(Product product, 
                           @RequestParam int categoryId,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        Category category = categoryService.findById(categoryId);
        product.setCategory(category);
        
        String result = productService.storeProduct(product);
        redirectAttributes.addFlashAttribute("success", result);
        
        return "redirect:/admin/products";
    }
    
    // Category Management
    @GetMapping("/categories")
    public String manageCategories(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());
        
        return "admin/categories";
    }
    
    // Add Category
    @PostMapping("/categories/add")
    public String addCategory(Category category,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        categoryService.saveCategory(category);
        redirectAttributes.addFlashAttribute("success", "Category added successfully");
        
        return "redirect:/admin/categories";
    }
    
    // User Management
    @GetMapping("/users")
    public String manageUsers(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        
        return "admin/users";
    }
    
    // Search Users
    @GetMapping("/users/search")
    public String searchUsers(@RequestParam(required = false) String keyword,
                            HttpSession session,
                            Model model) {
        
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        List<User> users;
        if (keyword != null && !keyword.trim().isEmpty()) {
            users = userService.searchUsers(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            users = userService.findAllUsers();
        }
        
        model.addAttribute("users", users);
        return "admin/users";
    }
    
    // Purchase Reports
    @GetMapping("/reports")
    public String purchaseReports(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        List<Category> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        
        return "admin/reports";
    }
    
    // Generate Reports
    @PostMapping("/reports/generate")
    public String generateReports(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                @RequestParam(required = false) Integer categoryId,
                                HttpSession session,
                                Model model) {
        
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        List<Order> orders = orderService.getOrdersByFilters(startDate, endDate, categoryId);
        List<Category> categories = categoryService.findAllCategories();
        
        float totalRevenue = orders.stream()
                                  .map(Order::getTotalAmount)
                                  .reduce(0f, Float::sum);
        
        model.addAttribute("orders", orders);
        model.addAttribute("categories", categories);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("categoryId", categoryId);
        
        return "admin/reports";
    }
    
    // Order Management
    @GetMapping("/orders")
    public String manageOrders(HttpSession session, Model model) {
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        List<Order> orders = orderService.findAllOrders();
        model.addAttribute("orders", orders);
        
        return "admin/orders";
    }
    
    // Update Order Status
    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam int orderId,
                                  @RequestParam String status,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {
        
        if (session.getAttribute("admin") == null) {
            return "redirect:/admin/login";
        }
        
        orderService.updateOrderStatus(orderId, status);
        redirectAttributes.addFlashAttribute("success", "Order status updated successfully");
        
        return "redirect:/admin/orders";
    }
}