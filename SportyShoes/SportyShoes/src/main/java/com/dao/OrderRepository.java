package com.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bean.Order;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@Repository
public class OrderRepository {

    @Autowired
    EntityManagerFactory emf;
    
    public Order save(Order order) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            if (order.getOrderId() == 0) {
                manager.persist(order);
            } else {
                order = manager.merge(order);
            }
            tran.commit();
            return order;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public Order findById(int orderId) {
        try {
            EntityManager manager = emf.createEntityManager();
            return manager.find(Order.class, orderId);
        } catch (Exception e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Order> findAll() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT o FROM Order o ORDER BY o.orderDate DESC");
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Order> findByUserId(int userId) {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT o FROM Order o WHERE o.user.userId = :userId ORDER BY o.orderDate DESC");
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    public long count() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT COUNT(o) FROM Order o");
        return (Long) query.getSingleResult();
    }
    
    public float getTotalRevenue() {
        try {
            EntityManager manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status != 'CANCELLED'");
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).floatValue() : 0f;
        } catch (Exception e) {
            return 0f;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Order> findRecentOrders(int limit) {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT o FROM Order o ORDER BY o.orderDate DESC");
        query.setMaxResults(limit);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Order> findOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT o FROM Order o WHERE DATE(o.orderDate) BETWEEN :startDate AND :endDate ORDER BY o.orderDate DESC");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Order> findOrdersByCategory(int categoryId) {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.product.category.categoryId = :categoryId ORDER BY o.orderDate DESC");
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Order> findOrdersByDateRangeAndCategory(LocalDate startDate, LocalDate endDate, int categoryId) {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE DATE(o.orderDate) BETWEEN :startDate AND :endDate AND oi.product.category.categoryId = :categoryId ORDER BY o.orderDate DESC");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("categoryId", categoryId);
        return query.getResultList();
    }
}