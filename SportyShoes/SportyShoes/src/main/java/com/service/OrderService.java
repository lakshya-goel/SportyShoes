package com.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.Order;
import com.bean.OrderStatus;
import com.dao.OrderRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }
    
    public List<Order> findOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }
    
    public Order findById(int orderId) {
        return orderRepository.findById(orderId);
    }
    
    public void updateOrderStatus(int orderId, String status) {
        Order order = orderRepository.findById(orderId);
        if (order != null) {
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
            orderRepository.save(order);
        }
    }
    
    public long getTotalOrderCount() {
        return orderRepository.count();
    }
    
    public float getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }
    
    public List<Order> getRecentOrders(int limit) {
        return orderRepository.findRecentOrders(limit);
    }
    
    public List<Order> getOrdersByFilters(LocalDate startDate, LocalDate endDate, Integer categoryId) {
        if (startDate != null && endDate != null && categoryId != null) {
            return orderRepository.findOrdersByDateRangeAndCategory(startDate, endDate, categoryId);
        } else if (startDate != null && endDate != null) {
            return orderRepository.findOrdersByDateRange(startDate, endDate);
        } else if (categoryId != null) {
            return orderRepository.findOrdersByCategory(categoryId);
        } else {
            return orderRepository.findAll();
        }
    }
}