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
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            if (category.getCategoryId() == 0) {
                manager.persist(category);
            } else {
                category = manager.merge(category);
            }
            tran.commit();
            return category;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public Category findById(int categoryId) {
        try {
            EntityManager manager = emf.createEntityManager();
            return manager.find(Category.class, categoryId);
        } catch (Exception e) {
            return null;
        }
    }
    
    public Category findByName(String name) {
        try {
            EntityManager manager = emf.createEntityManager();
            Query query = manager.createQuery("SELECT c FROM Category c WHERE c.name = :name");
            query.setParameter("name", name);
            return (Category) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Category> findAll() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT c FROM Category c ORDER BY c.name");
        return query.getResultList();
    }
    
    public void deleteById(int categoryId) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            Category category = manager.find(Category.class, categoryId);
            if (category != null) {
                manager.remove(category);
            }
            tran.commit();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}