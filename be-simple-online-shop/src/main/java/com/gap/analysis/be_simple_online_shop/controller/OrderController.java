package com.gap.analysis.be_simple_online_shop.controller;

import com.gap.analysis.be_simple_online_shop.entity.Order;
import com.gap.analysis.be_simple_online_shop.model.WebResponse;
import com.gap.analysis.be_simple_online_shop.model.order.AddOrderRequest;
import com.gap.analysis.be_simple_online_shop.model.order.PatchOrderRequest;
import com.gap.analysis.be_simple_online_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/api/order")
    WebResponse<String> addNewOrder(@ModelAttribute AddOrderRequest request){
        orderService.addNewOrder(request);
        return WebResponse.<String>builder().data("Add order success").build();
    }

    @GetMapping("/api/order")
    WebResponse<List<Order>> getAllOrder(){
        List<Order> orders = orderService.getAllOrder();
        return WebResponse.<List<Order>>builder().data(orders).build();
    }

    @GetMapping("/api/order/{id}")
    WebResponse<Order> getAllOrder(@PathVariable Long id){
        Order order = orderService.getOrderById(id);
        return WebResponse.<Order>builder().data(order).build();
    }

    @PatchMapping("/api/order/{id}")
    WebResponse<String> updateOrder(@PathVariable Long id, @ModelAttribute PatchOrderRequest request) throws IllegalAccessException {
        orderService.updateOrder(id, request);
        return WebResponse.<String>builder().data("Update order success").build();
    }

    @DeleteMapping("/api/order/{id}")
    WebResponse<String> deleteOrder(@PathVariable Long id)  {
        orderService.deleteOrder(id);
        return WebResponse.<String>builder().data("Delete order success").build();
    }
}
