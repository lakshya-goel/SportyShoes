package com.controller;

import com.bean.Order;
import com.bean.OrderItem;
import com.bean.OrderStatus;
import com.bean.Product;
import com.bean.User;
import com.service.OrderService;
import com.service.ProductService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @PostMapping("/checkout")
    public String checkout(@RequestParam int productId,
                           @RequestParam int quantity,
                           HttpSession session,
                           Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        Product product = productService.findById(productId);
        if (product == null || quantity > product.getStockQuantity()) {
            model.addAttribute("error", "Product not available or insufficient stock");
            return "redirect:/products/" + productId;
        }

        float totalAmount = product.getPrice() * quantity;

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CONFIRMED);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice());

        order.setOrderItems(Collections.singletonList(item));

        orderService.saveOrder(order);

        productService.updateProductStock(productId, quantity);

        model.addAttribute("success", "Order placed successfully!");
        return "redirect:/user/orders";
    }
}
