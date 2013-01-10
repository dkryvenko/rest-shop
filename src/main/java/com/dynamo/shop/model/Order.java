package com.dynamo.shop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Dmytro Kryvenko
 * Date: 1/9/13
 */
public class Order {

    private List<OrderItem> orderItems;

    private String name;
    private String address;
    private String phone;
    private String comments;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
