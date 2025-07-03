package com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bean.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@Repository
public class UserRepository {

    @Autowired
    EntityManagerFactory emf;

    public User findByUsername(String username) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT u FROM User u WHERE u.username = :username");
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            if (manager != null) manager.close();
        }
    }

    public User findByEmail(String email) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT u FROM User u WHERE u.email = :email");
            query.setParameter("email", email);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            if (manager != null) manager.close();
        }
    }

    public User findById(int userId) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            return manager.find(User.class, userId);
        } finally {
            if (manager != null) manager.close();
        }
    }

    public User save(User user) {
        EntityManager manager = null;
        EntityTransaction tran = null;
        try {
            manager = emf.createEntityManager();
            tran = manager.getTransaction();
            tran.begin();
            if (user.getUserId() == 0) {
                manager.persist(user);
            } else {
                user = manager.merge(user);
            }
            tran.commit();
            return user;
        } catch (Exception e) {
            if (tran != null && tran.isActive()) tran.rollback();
            System.err.println(e);
            return null;
        } finally {
            if (manager != null) manager.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT u FROM User u ORDER BY u.registrationDate DESC");
            return query.getResultList();
        } finally {
            if (manager != null) manager.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<User> searchUsers(String keyword) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT u FROM User u WHERE u.username LIKE :keyword OR u.email LIKE :keyword OR u.firstName LIKE :keyword OR u.lastName LIKE :keyword");
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } finally {
            if (manager != null) manager.close();
        }
    }

    public long count() {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT COUNT(u) FROM User u");
            return (Long) query.getSingleResult();
        } finally {
            if (manager != null) manager.close();
        }
    }
}
