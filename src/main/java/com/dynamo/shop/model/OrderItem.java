package com.dynamo.shop.model;

/**
 * Author: Dmytro Kryvenko
 * Date: 1/9/13
 */
public class OrderItem {

    private String productName;
    private double price;
    private double quantity;

    public OrderItem() {
    }

    public OrderItem(String productName, double price, double quantity) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
