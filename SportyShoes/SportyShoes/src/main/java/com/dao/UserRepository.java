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
        try {
            EntityManager manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT u FROM User u WHERE u.username = :username");
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public User findByEmail(String email) {
        try {
            EntityManager manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT u FROM User u WHERE u.email = :email");
            query.setParameter("email", email);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public User findById(int userId) {
        try {
            EntityManager manager = emf.createEntityManager();
            return manager.find(User.class, userId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public User save(User user) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            if (user.getUserId() == 0) {
                manager.persist(user);
            } else {
                user = manager.merge(user);
            }
            tran.commit();
            return user;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT u FROM User u ORDER BY u.registrationDate DESC");
        return query.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<User> searchUsers(String keyword) {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT u FROM User u WHERE u.username LIKE :keyword OR u.email LIKE :keyword OR u.firstName LIKE :keyword OR u.lastName LIKE :keyword");
        query.setParameter("keyword", "%" + keyword + "%");
        return query.getResultList();
    }
    
    public long count() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT COUNT(u) FROM User u");
        return (Long) query.getSingleResult();
    }
}