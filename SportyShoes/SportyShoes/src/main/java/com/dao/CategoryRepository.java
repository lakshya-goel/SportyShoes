package com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bean.Category;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@Repository
public class CategoryRepository {

    @Autowired
    EntityManagerFactory emf;

    public Category save(Category category) {
        EntityManager manager = null;
        EntityTransaction tran = null;
        try {
            manager = emf.createEntityManager();
            tran = manager.getTransaction();
            tran.begin();
            if (category.getCategoryId() == 0) {
                manager.persist(category);
            } else {
                category = manager.merge(category);
            }
            tran.commit();
            return category;
        } catch (Exception e) {
            if (tran != null && tran.isActive()) tran.rollback();
            System.err.println(e);
            return null;
        } finally {
            if (manager != null) manager.close();
        }
    }

    public Category findById(int categoryId) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            return manager.find(Category.class, categoryId);
        } finally {
            if (manager != null) manager.close();
        }
    }

    public Category findByName(String name) {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT c FROM Category c WHERE c.name = :name");
            query.setParameter("name", name);
            return (Category) query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            if (manager != null) manager.close();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Category> findAll() {
        EntityManager manager = null;
        try {
            manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT c FROM Category c ORDER BY c.name");
            return query.getResultList();
        } finally {
            if (manager != null) manager.close();
        }
    }

    public void deleteById(int categoryId) {
        EntityManager manager = null;
        EntityTransaction tran = null;
        try {
            manager = emf.createEntityManager();
            tran = manager.getTransaction();
            tran.begin();
            Category category = manager.find(Category.class, categoryId);
            if (category != null) {
                manager.remove(category);
            }
            tran.commit();
        } catch (Exception e) {
            if (tran != null && tran.isActive()) tran.rollback();
            System.err.println(e);
        } finally {
            if (manager != null) manager.close();
        }
    }
}
