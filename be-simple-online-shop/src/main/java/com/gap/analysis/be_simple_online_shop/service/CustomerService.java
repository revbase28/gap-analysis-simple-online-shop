package com.gap.analysis.be_simple_online_shop.service;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.model.customer.AddCustomerRequest;
import com.gap.analysis.be_simple_online_shop.model.customer.PatchCustomerRequest;
import com.gap.analysis.be_simple_online_shop.repository.CustomerRepository;
import com.gap.analysis.be_simple_online_shop.repository.OrderRepository;
import com.gap.analysis.be_simple_online_shop.tools.Patcher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MinioService minioService;

    @Autowired
    private Validator validator;

    @Transactional
    public void addNewCustomer(AddCustomerRequest request) throws Exception {
        Set<ConstraintViolation<AddCustomerRequest>>constraintViolations = validator.validate(request);

        if(constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Customer customer = new Customer();
        customer.setCustomerName(request.getCustomerName());
        customer.setCustomerAddress(request.getCustomerAddress());
        customer.setCustomerPhone(request.getCustomerPhone());

        // Upload file to MinIO and retrieve file name
        String fileName = minioService.uploadFile(request.getPic());

        customer.setPic(fileName);
        customer.setIsActive(true);

        customerRepository.save(customer);
    }

    public List<Customer> getAllActiveCustomer() {
        List<Customer> customers = customerRepository.findByIsActive(true);

        return customers.stream().peek(customer -> {
            String fileName = customer.getPic();
            String fileLink = null;
            try {
                fileLink = minioService.getPresignedUrl(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            customer.setPic(fileLink);
        }).collect(Collectors.toList());
    }

    public Page<Customer> getAllActiveCustomerWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.findByIsActiveTrue(pageable);

        return customers.map(customer -> {
            String fileName = customer.getPic();
            String fileLink = null;
            try {
                fileLink = minioService.getPresignedUrl(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            customer.setPic(fileLink);
            return customer;
        });
    }

    public Page<Customer> searchCustomer(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Customer> customers = customerRepository.searchActiveCustomers(keyword, pageable);

        return customers.map(customer -> {
            String fileName = customer.getPic();
            String fileLink = null;
            try {
                fileLink = minioService.getPresignedUrl(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            customer.setPic(fileLink);
            return customer;
        });
    }

    public Customer getCustomerById(long id){
        Optional<Customer> customer = customerRepository.findById(id);

        if(customer.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no customer with id " + id);
        }

        if(!customer.get().getIsActive()){
            throw new ResponseStatusException(HttpStatus.GONE, "Customer with id " + id + " is already deleted");
        }

        return customer.get();
    }

    public void updateCustomer(long id, PatchCustomerRequest request) throws Exception {
        Set<ConstraintViolation<PatchCustomerRequest>>constraintViolations = validator.validate(request);

        if(constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Optional<Customer> optCurrentCus = customerRepository.findById(id);

        if(optCurrentCus.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no customer with id " + id);
        }

        if(!optCurrentCus.get().getIsActive()){
            throw new ResponseStatusException(HttpStatus.GONE, "Customer with id " + id + " is already deleted");
        }

        Customer currentCus = optCurrentCus.get();
        Customer newCus = new Customer();
        newCus.setCustomerName(request.getCustomerName());
        newCus.setCustomerAddress(request.getCustomerAddress());
        newCus.setCustomerPhone(request.getCustomerPhone());

        // Upload file to MinIO and retrieve file name
        String fileName = null;
        if(request.getPic() != null){
            // If there is update on customer pic, remove the old one first
            // then upload the new one
            minioService.removeObject(currentCus.getPic());
            fileName = minioService.uploadFile(request.getPic());
        }

        newCus.setPic(fileName);
        newCus.setIsActive(request.getIsActive());

        //Check for null value in newCus field
        //If the field is not null, update value in currentCus field with value in newCus
        Patcher.customerPatcher(currentCus, newCus);

        customerRepository.save(currentCus);
    }

    public void softDelete(long id){
        Optional<Customer> optCurrentCus = customerRepository.findById(id);

        if(optCurrentCus.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no customer with id " + id);
        }

        if(!optCurrentCus.get().getIsActive()){
            throw new ResponseStatusException(HttpStatus.GONE, "Customer with id " + id + " is already deleted");
        }

        Customer currentCus = optCurrentCus.get();
        currentCus.setIsActive(false);

        orderRepository.deleteByCustomer(currentCus);
        customerRepository.save(currentCus);
    }
}
