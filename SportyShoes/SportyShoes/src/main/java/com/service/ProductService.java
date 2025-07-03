package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bean.Product;
import com.dao.ProductRepository;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;
    
    public String storeProduct(Product product) {
        if(productRepository.storeProduct(product) > 0) {
            return "Product stored successfully";
        } else {
            return "Product didn't store";
        }
    }
    
    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }
    
    public List<Product> findProductsByCategory(int categoryId) {
        return productRepository.findByCategory(categoryId);
    }
    
    public Product findById(int productId) {
        return productRepository.findById(productId);
    }
    
    public List<Product> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword);
    }
    
    public void updateProductStock(int productId, int quantity) {
        productRepository.updateStock(productId, quantity);
    }
    
    public long getTotalProductCount() {
        return productRepository.count();
    }
    
    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }
    
    public Product updateProduct(Product product) {
        return productRepository.update(product);
    }
}