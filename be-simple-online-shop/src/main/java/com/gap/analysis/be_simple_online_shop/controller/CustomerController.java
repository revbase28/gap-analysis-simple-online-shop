package com.gap.analysis.be_simple_online_shop.controller;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.model.customer.AddCustomerRequest;
import com.gap.analysis.be_simple_online_shop.model.customer.PatchCustomerRequest;
import com.gap.analysis.be_simple_online_shop.model.WebResponse;
import com.gap.analysis.be_simple_online_shop.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(path = "/api/customer")
    public WebResponse<String> addNewCustomer(@ModelAttribute AddCustomerRequest request) throws Exception {
        customerService.addNewCustomer(request);
        return WebResponse.<String>builder().data("Add Customer Success").build();
    }

    @GetMapping(path = "/api/customer-all")
    public WebResponse<List<Customer>> getAllActiveCustomer(){
        List<Customer> customers = customerService.getAllActiveCustomer();
        return WebResponse.<List<Customer>>builder().data(customers).build();
    }

    @GetMapping(path = "/api/customer")
    public WebResponse<Page<Customer>> getAllActiveCustomer(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Customer> customers = customerService.getAllActiveCustomerWithPagination(page, size);
        return WebResponse.<Page<Customer>>builder().data(customers).build();
    }

    @GetMapping(path = "/api/customer/search")
    public WebResponse<Page<Customer>> searchCustomer(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Customer> customers = customerService.searchCustomer(keyword, page, size);
        return WebResponse.<Page<Customer>>builder().data(customers).build();
    }

    @GetMapping(path = "/api/customer/{id}")
    public WebResponse<Customer> getCustomerById(@PathVariable long id) {
        Customer customer = customerService.getCustomerById(id);
        return WebResponse.<Customer>builder().data(customer).build();
    }

    @PatchMapping(path = "/api/customer/{id}")
    public WebResponse<String> updateCustomerById(@PathVariable long id, @ModelAttribute PatchCustomerRequest request) throws Exception {
        customerService.updateCustomer(id, request);
        return WebResponse.<String>builder().data("Update Customer Success").build();
    }

    @DeleteMapping(path = "/api/customer/{id}")
    public WebResponse<String> deleteCustomer(@PathVariable long id) {
        customerService.softDelete(id);
        return WebResponse.<String>builder().data("Delete Customer Success").build();
    }
}
