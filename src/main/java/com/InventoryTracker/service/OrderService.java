package com.InventoryTracker.service;

import com.InventoryTracker.dto.OrderRequest;
import com.InventoryTracker.dto.OrderItemRequest;
import com.InventoryTracker.entity.*;
import com.InventoryTracker.repository.CustomerRepository;
import com.InventoryTracker.repository.OrderRepository;
import com.InventoryTracker.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order createOrder(OrderRequest orderRequest) {

        // Step 1: Customer dhundo
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Step 2: Naya Order object banao (abhi khali hai)
        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();

        // Step 3: Har item ke liye check karo aur stock kam karo
        for (OrderItemRequest itemRequest : orderRequest.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Stock kam karo
            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            // OrderItem banao
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtOrderTime(product.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);

        // Step 4: Order save karo (cascade se OrderItems bhi save ho jayenge)
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    public Double getRevenueBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findOrdersBetweenDates(startDate, endDate);

        Double totalRevenue = 0.0;
        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                totalRevenue += item.getPriceAtOrderTime() * item.getQuantity();
            }
        }
        return totalRevenue;
    }
}