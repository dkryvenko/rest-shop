package com.dynamo.shop.model;

/**
 * Author: Dmytro Kryvenko
 * Date: 12/23/12
 */
public class Product {

    public static final String PRODUCT_TYPE_PIZZA = "pizza";
    public static final String PRODUCT_TYPE_BEVERAGE = "beverage";
    public static final String PRODUCT_TYPE_FOOD = "food";

    private String id;
    private String category;
    private String name;
    private String description;
    private double weight;
    private double size;
    private double price;
    private String smallThumb;
    private String mediumThumb;
    private String largeThumb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSmallThumb() {
        return smallThumb;
    }

    public void setSmallThumb(String smallThumb) {
        this.smallThumb = smallThumb;
    }

    public String getMediumThumb() {
        return mediumThumb;
    }

    public void setMediumThumb(String mediumThumb) {
        this.mediumThumb = mediumThumb;
    }

    public String getLargeThumb() {
        return largeThumb;
    }

    public void setLargeThumb(String largeThumb) {
        this.largeThumb = largeThumb;
    }
}
