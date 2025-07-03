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
        EntityManager manager = null;
        EntityTransaction tran = null;
        try {
            manager = emf.createEntityManager();
            tran = manager.getTransaction();
            tran.begin();
            if (order.getOrderId() == 0) {
                manager.persist(order);
            } else {
                order = manager.merge(order);
            }
            tran.commit();
            return order;
        } catch (Exception e) {
            if (tran != null && tran.isActive()) {
                tran.rollback();
            }
            throw e;
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    public Order findById(int orderId) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            return manager.find(Order.class, orderId);
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> findAll() {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("""
                SELECT DISTINCT o FROM Order o
                JOIN FETCH o.user
                LEFT JOIN FETCH o.orderItems oi
                LEFT JOIN FETCH oi.product p
                LEFT JOIN FETCH p.category
                ORDER BY o.orderDate DESC
            """);
            return query.getResultList();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> findByUserId(int userId) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("""
                SELECT DISTINCT o FROM Order o
                JOIN FETCH o.user
                LEFT JOIN FETCH o.orderItems oi
                LEFT JOIN FETCH oi.product p
                LEFT JOIN FETCH p.category
                WHERE o.user.userId = :userId
                ORDER BY o.orderDate DESC
            """);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    public long count() {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT COUNT(o) FROM Order o");
            return (Long) query.getSingleResult();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    public float getTotalRevenue() {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("""
                SELECT SUM(o.totalAmount) FROM Order o WHERE o.status <> 'CANCELLED'
            """);
            Object result = query.getSingleResult();
            return result != null ? ((Number) result).floatValue() : 0f;
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> findRecentOrders(int limit) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("""
                SELECT DISTINCT o FROM Order o
                JOIN FETCH o.user
                LEFT JOIN FETCH o.orderItems oi
                LEFT JOIN FETCH oi.product p
                LEFT JOIN FETCH p.category
                ORDER BY o.orderDate DESC
            """);
            query.setMaxResults(limit);
            return query.getResultList();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> findOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("""
                SELECT DISTINCT o FROM Order o
                JOIN FETCH o.user
                LEFT JOIN FETCH o.orderItems oi
                LEFT JOIN FETCH oi.product p
                LEFT JOIN FETCH p.category
                WHERE o.orderDate BETWEEN :startDate AND :endDate
                ORDER BY o.orderDate DESC
            """);
            query.setParameter("startDate", startDate.atStartOfDay());
            query.setParameter("endDate", endDate.atTime(23, 59, 59));
            return query.getResultList();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> findOrdersByCategory(int categoryId) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("""
                SELECT DISTINCT o FROM Order o
                JOIN o.orderItems oi
                JOIN oi.product p
                JOIN FETCH o.user
                LEFT JOIN FETCH o.orderItems oij
                LEFT JOIN FETCH oij.product pij
                LEFT JOIN FETCH pij.category
                WHERE p.category.categoryId = :categoryId
                ORDER BY o.orderDate DESC
            """);
            query.setParameter("categoryId", categoryId);
            return query.getResultList();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Order> findOrdersByDateRangeAndCategory(LocalDate startDate, LocalDate endDate, int categoryId) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("""
                SELECT DISTINCT o FROM Order o
                JOIN o.orderItems oi
                JOIN oi.product p
                JOIN FETCH o.user
                LEFT JOIN FETCH o.orderItems oij
                LEFT JOIN FETCH oij.product pij
                LEFT JOIN FETCH pij.category
                WHERE o.orderDate BETWEEN :startDate AND :endDate
                  AND p.category.categoryId = :categoryId
                ORDER BY o.orderDate DESC
            """);
            query.setParameter("startDate", startDate.atStartOfDay());
            query.setParameter("endDate", endDate.atTime(23, 59, 59));
            query.setParameter("categoryId", categoryId);
            return query.getResultList();
        } finally {
            if (manager != null) {
                manager.close();
            }
        }
    }
}
