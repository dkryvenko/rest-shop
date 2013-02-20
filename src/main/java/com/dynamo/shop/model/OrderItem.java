package com.dynamo.shop.model;

/**
 * Author: Dmytro Kryvenko
 * Date: 1/9/13
 */
public class OrderItem {

    private long id;
    private String product;
    private double price;
    private double quantity;
    private double amount;

    public OrderItem() {
    }

    public OrderItem(String product, double price, double quantity) {
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String productName) {
        this.product = productName;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
