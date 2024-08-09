package com.gap.analysis.be_simple_online_shop.service;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.entity.Item;
import com.gap.analysis.be_simple_online_shop.entity.Order;
import com.gap.analysis.be_simple_online_shop.model.order.AddOrderRequest;
import com.gap.analysis.be_simple_online_shop.model.order.PatchOrderRequest;
import com.gap.analysis.be_simple_online_shop.repository.CustomerRepository;
import com.gap.analysis.be_simple_online_shop.repository.ItemRepository;
import com.gap.analysis.be_simple_online_shop.repository.OrderRepository;
import com.gap.analysis.be_simple_online_shop.tools.Patcher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private Validator validator;

    @Transactional
    public void addNewOrder(AddOrderRequest request){
        Set<ConstraintViolation<AddOrderRequest>> constraintViolations = validator.validate(request);

        if(constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        // Validate customer and item is existed
        Optional<Customer> optCus = customerRepository.findById(request.getCustomerId());
        Optional<Item> optItem = itemRepository.findById(request.getItemId());

        if(optCus.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no customer with id " + request.getCustomerId());
        }

        if(optItem.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no item with id " + request.getItemId());
        }

        //Validate item stock readiness
        if(optItem.get().getStock() < request.getQuantity()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity Exceed item stock");
        }

        //Validate item if is available
        if(!optItem.get().getIsAvailable()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item is not available");
        }

        Order newOrder = new Order();
        newOrder.setOrderDate(new Date());
        newOrder.setTotalPrice(optItem.get().getPrice() * request.getQuantity());
        newOrder.setQuantity(request.getQuantity());
        newOrder.setCustomer(optCus.get());
        newOrder.setItem(optItem.get());

        orderRepository.save(newOrder);

        Item updatedItem = optItem.get();
        updatedItem.setStock(updatedItem.getStock() - request.getQuantity());
        updatedItem.setIsAvailable(updatedItem.getStock() > 0);
        itemRepository.save(updatedItem);

        Customer updatedCustomer = optCus.get();
        updatedCustomer.setLastOrderDate(new Date());
        customerRepository.save(updatedCustomer);
    }

    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id){
        Optional<Order> optOrder = orderRepository.findById(id);

        if(optOrder.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no order with id " + id);
        }

        return optOrder.get();
    }

    @Transactional
    public void updateOrder(Long id, PatchOrderRequest request) throws IllegalAccessException {
        Set<ConstraintViolation<PatchOrderRequest>> constraintViolations = validator.validate(request);

        if(constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Optional<Order> optOrder = orderRepository.findById(id);

        if(optOrder.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no order with id " + id);
        }

        // Cek existence and availability of an item
        Item updatedItem;
        Optional<Item> optItem;
        if(request.getItemId() != null){
            optItem = itemRepository.findById(request.getItemId());

            if(optItem.isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no item with id " + id);
            }

            // Check for availability if there is a change in item being ordered
            if(!optItem.get().getIsAvailable() && optItem.get().getItemId() != request.getItemId()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item is not available");
            }

        } else {
            optItem = itemRepository.findById(optOrder.get().getItem().getItemId());

            if(optItem.isEmpty()){
                throw new ResponseStatusException(HttpStatus.GONE, "Item with id " + id + " is probably already deleted");
            }
        }

        updatedItem = optItem.get();

        // Handle change in stock
        // Case : same item, diff quantity => item.stock - (newQuantity - oldQuantity)
        if(request.getItemId() == null || optOrder.get().getItem().getItemId() == request.getItemId()){
            Long oldQuantity = optOrder.get().getQuantity();
            Long newQuantity = request.getQuantity();

            updatedItem.setStock(updatedItem.getStock() - (newQuantity - oldQuantity));
            updatedItem.setIsAvailable(updatedItem.getStock() > 0);

            itemRepository.save(updatedItem);
        } else {
            // Case : diff item => oldItemStock + oldQuantity, newItemStock - newQuantity
            Item oldItem = optOrder.get().getItem();
            oldItem.setStock(oldItem.getStock() + optOrder.get().getQuantity());
            oldItem.setIsAvailable(oldItem.getStock() > 0);
            itemRepository.save(oldItem);

            updatedItem.setStock(updatedItem.getStock() - request.getQuantity());
            updatedItem.setIsAvailable(updatedItem.getStock() > 0);
            itemRepository.save(updatedItem);
        }

        Order newOrder = new Order();
        Order currOrder = optOrder.get();
        newOrder.setQuantity(request.getQuantity());
        newOrder.setItem(updatedItem);

        Patcher.orderPatcher(currOrder, newOrder);

        orderRepository.save(currOrder);
    }

    public void deleteOrder(Long id){
        Optional<Order> optOrder = orderRepository.findById(id);

        if(optOrder.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no order with id " + id);
        }

        // Return item stock
        Optional<Item> optItem = itemRepository.findById(optOrder.get().getItem().getItemId());

        if(optItem.isPresent()){
            Item item = optItem.get();
            item.setStock(item.getStock() + optOrder.get().getQuantity());
            item.setIsAvailable(item.getStock() > 0);
            itemRepository.save(item);
        }

        orderRepository.delete(optOrder.get());
    }
}
