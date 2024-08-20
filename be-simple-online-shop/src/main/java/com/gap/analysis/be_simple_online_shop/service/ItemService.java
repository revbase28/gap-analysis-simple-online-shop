package com.gap.analysis.be_simple_online_shop.service;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.entity.Item;
import com.gap.analysis.be_simple_online_shop.model.item.AddItemRequest;
import com.gap.analysis.be_simple_online_shop.model.item.PatchItemRequest;
import com.gap.analysis.be_simple_online_shop.repository.ItemRepository;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private Validator validator;

    @Transactional
    public void addNewItem(AddItemRequest request){
        Set<ConstraintViolation<AddItemRequest>> constraintViolations = validator.validate(request);

        if(constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Item item = new Item();
        item.setItemName(request.getItemName());
        item.setStock(request.getStock());
        item.setPrice(request.getPrice());
        item.setIsAvailable(request.getIsAvailable());
        item.setLastReStock(request.getLastReStock());

        itemRepository.save(item);
    }

    public List<Item> getAllItem(){
        return itemRepository.findAll();
    }

    public Page<Item> getAllItemWithPagination(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAll(pageable);
    }

    public Page<Item> searchItem(String keyword, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.searchItem(keyword, pageable);
    }

    public Item getItemById(long id){
        Optional<Item> item = itemRepository.findById(id);

        if(item.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no item with id " + id);
        }

        return item.get();
    }

    public void updateItem(long id, PatchItemRequest request) throws IllegalAccessException {
        Set<ConstraintViolation<PatchItemRequest>> constraintViolations = validator.validate(request);

        if(constraintViolations.size() != 0){
            throw new ConstraintViolationException(constraintViolations);
        }

        Optional<Item> optItem = itemRepository.findById(id);

        if(optItem.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no item with id " + id);
        }

        Item currentItem = optItem.get();
        Item updateItem = new Item();

        updateItem.setItemName(request.getItemName());
        updateItem.setStock(request.getStock());
        updateItem.setPrice(request.getPrice());
        updateItem.setIsAvailable(request.getIsAvailable());

        if(updateItem.getStock() != null && currentItem.getStock() < updateItem.getStock()){
            updateItem.setLastReStock(new Date());
        }

        Patcher.itemPatcher(currentItem, updateItem);

        itemRepository.save(currentItem);
    }

    public void deleteItem(long id) {
        Optional<Item> optItem = itemRepository.findById(id);

        if(optItem.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no item with id " + id);
        }


        orderRepository.deleteByItem(optItem.get());
        itemRepository.delete(optItem.get());
    }
}
