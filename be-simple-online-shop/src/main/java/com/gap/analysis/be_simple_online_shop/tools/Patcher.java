package com.gap.analysis.be_simple_online_shop.tools;

import com.gap.analysis.be_simple_online_shop.entity.Customer;
import com.gap.analysis.be_simple_online_shop.entity.Item;
import com.gap.analysis.be_simple_online_shop.entity.Order;

import java.lang.reflect.Field;

public class Patcher {
    public static void customerPatcher(Customer existing, Customer incomplete) throws IllegalAccessException {
        //GET THE COMPILED VERSION OF THE CLASS
        Class<?> internClass= Customer.class;
        Field[] internFields=internClass.getDeclaredFields();
        System.out.println(internFields.length);
        for(Field field : internFields){
            if(field.getName().equals("customerId") || field.getName().equals("customerCode") ){
                continue;
            }

            System.out.println(field.getName());
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);

            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            Object value=field.get(incomplete);
            if(value!=null){
                field.set(existing,value);
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }
    }

    public static void itemPatcher(Item existing, Item incomplete) throws IllegalAccessException {
        //GET THE COMPILED VERSION OF THE CLASS
        Class<?> internClass= Item.class;
        Field[] internFields=internClass.getDeclaredFields();
        System.out.println(internFields.length);
        for(Field field : internFields){
            if(field.getName().equals("itemId") || field.getName().equals("itemCode") ){
                continue;
            }

            System.out.println(field.getName());
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);

            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            Object value=field.get(incomplete);
            if(value!=null){
                field.set(existing,value);
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }
    }

    public static void orderPatcher(Order existing, Order incomplete) throws IllegalAccessException {
        //GET THE COMPILED VERSION OF THE CLASS
        Class<?> internClass= Order.class;
        Field[] internFields=internClass.getDeclaredFields();
        System.out.println(internFields.length);
        for(Field field : internFields){
            if(field.getName().equals("orderId") || field.getName().equals("orderCode") ){
                continue;
            }

            System.out.println(field.getName());
            //CANT ACCESS IF THE FIELD IS PRIVATE
            field.setAccessible(true);

            //CHECK IF THE VALUE OF THE FIELD IS NOT NULL, IF NOT UPDATE EXISTING INTERN
            Object value=field.get(incomplete);
            if(value!=null){
                field.set(existing,value);
            }
            //MAKE THE FIELD PRIVATE AGAIN
            field.setAccessible(false);
        }
    }
}
