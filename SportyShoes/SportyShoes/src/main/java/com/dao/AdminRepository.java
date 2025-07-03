package com.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bean.Admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@Repository
public class AdminRepository {

    @Autowired
    EntityManagerFactory emf;
    
    public Admin findByUsername(String username) {
        try {
            EntityManager manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT a FROM Admin a WHERE a.username = :username");
            query.setParameter("username", username);
            return (Admin) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    public Admin findById(int adminId) {
        try {
            EntityManager manager = emf.createEntityManager();
            return manager.find(Admin.class, adminId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public Admin save(Admin admin) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            if (admin.getAdminId() == 0) {
                manager.persist(admin);
            } else {
                admin = manager.merge(admin);
            }
            tran.commit();
            return admin;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public void updateLastLogin(int adminId, LocalDateTime lastLogin) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            Query query = manager.createQuery("UPDATE Admin a SET a.lastLogin = :lastLogin WHERE a.adminId = :adminId");
            query.setParameter("lastLogin", lastLogin);
            query.setParameter("adminId", adminId);
            query.executeUpdate();
            tran.commit();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public void updatePassword(int adminId, String newPassword) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            Query query = manager.createQuery("UPDATE Admin a SET a.password = :password WHERE a.adminId = :adminId");
            query.setParameter("password", newPassword);
            query.setParameter("adminId", adminId);
            query.executeUpdate();
            tran.commit();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Admin> findAll() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT a FROM Admin a");
        return query.getResultList();
    }
}
