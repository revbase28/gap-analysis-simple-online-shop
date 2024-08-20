package com.gap.analysis.be_simple_online_shop.controller;

import com.gap.analysis.be_simple_online_shop.entity.Item;
import com.gap.analysis.be_simple_online_shop.entity.Order;
import com.gap.analysis.be_simple_online_shop.model.WebResponse;
import com.gap.analysis.be_simple_online_shop.model.order.AddOrderRequest;
import com.gap.analysis.be_simple_online_shop.model.order.PatchOrderRequest;
import com.gap.analysis.be_simple_online_shop.service.OrderService;
import com.gap.analysis.be_simple_online_shop.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ReportService reportService;

    @PostMapping("/api/order")
    WebResponse<String> addNewOrder(@ModelAttribute AddOrderRequest request){
        orderService.addNewOrder(request);
        return WebResponse.<String>builder().data("Add order success").build();
    }

    @GetMapping("/api/order")
    WebResponse<Page<Order>> getAllOrder(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") int size){
        Page<Order> orders = orderService.getAllOrder(page, size);
        return WebResponse.<Page<Order>>builder().data(orders).build();
    }

    @GetMapping(path = "/api/order/search")
    public WebResponse<Page<Order>> searchItem(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Order> orders = orderService.searchOrder(keyword, page, size);
        return WebResponse.<Page<Order>>builder().data(orders).build();
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

    @GetMapping(value = "/api/order/report")
    ResponseEntity<byte[]> downloadOrderReport() throws Exception {
        try {
            byte[] pdfReport = reportService.generateOrderReport();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "order_report.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfReport);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}
