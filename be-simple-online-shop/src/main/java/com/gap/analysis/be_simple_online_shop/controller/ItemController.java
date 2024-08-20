package com.gap.analysis.be_simple_online_shop.controller;

import com.gap.analysis.be_simple_online_shop.entity.Item;
import com.gap.analysis.be_simple_online_shop.model.WebResponse;
import com.gap.analysis.be_simple_online_shop.model.item.AddItemRequest;
import com.gap.analysis.be_simple_online_shop.model.item.PatchItemRequest;
import com.gap.analysis.be_simple_online_shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping(path = "/api/item")
    public WebResponse<String> addNewItem(@ModelAttribute AddItemRequest request){
        itemService.addNewItem(request);
        return WebResponse.<String>builder().data("Add item Success").build();
    }

    @GetMapping(path = "/api/item-all")
    public WebResponse<List<Item>> getAllItem(){
        return WebResponse.<List<Item>>builder().data(itemService.getAllItem()).build();
    }

    @GetMapping(path = "/api/item")
    public WebResponse<Page<Item>> getAllItem(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size){
        Page<Item> items = itemService.getAllItemWithPagination(page, size);
        return WebResponse.<Page<Item>>builder().data(items).build();
    }

    @GetMapping(path = "/api/item/search")
    public WebResponse<Page<Item>> searchItem(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<Item> items = itemService.searchItem(keyword, page, size);
        return WebResponse.<Page<Item>>builder().data(items).build();
    }

    @GetMapping(path = "/api/item/{id}")
    public WebResponse<Item> getItemById(@PathVariable long id){
        Item items = itemService.getItemById(id);
        return WebResponse.<Item>builder().data(items).build();
    }

    @PatchMapping(path = "/api/item/{id}")
    public WebResponse<String> updateItemById(@PathVariable long id, @ModelAttribute PatchItemRequest request) throws IllegalAccessException {
        itemService.updateItem(id, request);
        return WebResponse.<String>builder().data("Update item Success").build();
    }

    @DeleteMapping(path = "/api/item/{id}")
    public WebResponse<String> deleteItemById(@PathVariable long id) {
        itemService.deleteItem(id);
        return WebResponse.<String>builder().data("Delete item Success").build();
    }
}
