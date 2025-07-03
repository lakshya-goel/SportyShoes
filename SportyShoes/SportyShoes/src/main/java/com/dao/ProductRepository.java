package com.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bean.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;

@Repository
public class ProductRepository {

    @Autowired
    EntityManagerFactory emf;
    
    public int storeProduct(Product product) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            manager.persist(product);
            tran.commit();
            return 1;
        } catch (Exception e) {
            System.err.println(e);
            return 0;
        }
    }
    
    public Product update(Product product) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            product = manager.merge(product);
            tran.commit();
            return product;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public Product findById(int productId) {
        try {
            EntityManager manager = emf.createEntityManager();
            return manager.find(Product.class, productId);
        } catch (Exception e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Product> findAllProducts() {
        EntityManager manager = emf.createEntityManager();
        Query qry = manager.createQuery("select p from Product p ORDER BY p.createdDate DESC");
        return qry.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Product> findByCategory(int categoryId) {
        EntityManager manager = emf.createEntityManager();
        Query qry = manager.createQuery("select p from Product p WHERE p.category.categoryId = :categoryId");
        qry.setParameter("categoryId", categoryId);
        return qry.getResultList();
    }
    
    @SuppressWarnings("unchecked")
    public List<Product> searchProducts(String keyword) {
        EntityManager manager = emf.createEntityManager();
        Query qry = manager.createQuery("select p from Product p WHERE p.pname LIKE :keyword OR p.description LIKE :keyword");
        qry.setParameter("keyword", "%" + keyword + "%");
        return qry.getResultList();
    }
    
    public void updateStock(int productId, int quantity) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            Query query = manager.createQuery("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity WHERE p.pid = :productId");
            query.setParameter("quantity", quantity);
            query.setParameter("productId", productId);
            query.executeUpdate();
            tran.commit();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public long count() {
        EntityManager manager = emf.createEntityManager();
        Query query = manager.createQuery("SELECT COUNT(p) FROM Product p");
        return (Long) query.getSingleResult();
    }
    
    public void deleteById(int productId) {
        try {
            EntityManager manager = emf.createEntityManager();
            EntityTransaction tran = manager.getTransaction();
            tran.begin();
            Product product = manager.find(Product.class, productId);
            if (product != null) {
                manager.remove(product);
            }
            tran.commit();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}