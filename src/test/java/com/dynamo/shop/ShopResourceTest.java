package com.dynamo.shop;

import com.dynamo.shop.model.Order;
import com.dynamo.shop.model.OrderItem;
import com.dynamo.shop.model.Product;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Dmytro Kryvenko
 * Date: 12/23/12
 */
public class ShopResourceTest {

    ShopResource shopResource;

    Map<String, Product> expectedProducts;
    JavaMailSender mockedMailSender;

    @Before
    public void before() {
        expectedProducts = new HashMap<String, Product>();
        mockedMailSender = Mockito.mock(JavaMailSender.class);
        shopResource = new ShopResource(expectedProducts, mockedMailSender);
    }

    @Test
    public void getStatus() {
        String version = shopResource.getStatus();
        Assert.assertEquals("running", version);
    }

    @Test
    public void getProducts() {
        Product p1 = new Product();
        p1.setCategory("c1");
        Product p2 = new Product();
        p2.setCategory("c2");
        expectedProducts.put("101", p1);
        expectedProducts.put("102", p2);

        List<Product> actualProducts = shopResource.getProducts(null);

        Assert.assertEquals(2, actualProducts.size());
        Assert.assertEquals(p1, actualProducts.get(1));
        Assert.assertEquals(p2, actualProducts.get(0));
    }

    @Test
    public void getProductsByCategory() {
        Product p1 = new Product();
        p1.setCategory("c1");
        Product p2 = new Product();
        p2.setCategory("c2");
        Product p3 = new Product();
        p3.setCategory("c2");
        expectedProducts.put("101", p1);
        expectedProducts.put("102", p2);
        expectedProducts.put("103", p3);

        List<Product> actualProducts = shopResource.getProducts("c2");

        Assert.assertEquals(2, actualProducts.size());
        Assert.assertEquals(p3, actualProducts.get(0));
        Assert.assertEquals(p2, actualProducts.get(1));
    }

    @Test
    public void getProduct() {
        Product p1 = new Product();
        Product p2 = new Product();
        expectedProducts.put("101", p1);
        expectedProducts.put("102", p2);

        Product actualProduct = shopResource.getProduct("101");

        Assert.assertEquals(p1, actualProduct);
    }

    @Test
    public void getOrder() {
        Product p1 = new Product();
        p1.setId("101");
        p1.setName("product 1");
        p1.setPrice(new BigDecimal("100.00"));
        Product p2 = new Product();
        p2.setId("102");
        p2.setName("product 2");
        p2.setPrice(new BigDecimal("150.00"));
        expectedProducts.put("101", p1);
        expectedProducts.put("102", p2);

        Order order = new Order();
        OrderItem i1 = new OrderItem();
        i1.setProductId("101");
        i1.setQuantity(1);
        OrderItem i2 = new OrderItem();
        i2.setProductId("102");
        i2.setQuantity(2);
        order.getOrderItems().add(i1);
        order.getOrderItems().add(i2);

        Order actualOrder = shopResource.calculateOrder(order);

        Assert.assertEquals(new BigDecimal("400.00"), actualOrder.getAmount());
        Assert.assertEquals(new BigDecimal("0"), actualOrder.getDiscount());
        Assert.assertEquals(new BigDecimal("400.00"), actualOrder.getTotal());
        Assert.assertEquals(2, actualOrder.getOrderItems().size());
        Assert.assertEquals("product 2", actualOrder.getOrderItems().get(1).getProductName());
        Assert.assertEquals(new BigDecimal("300.00"), actualOrder.getOrderItems().get(1).getAmount());
        Assert.assertEquals(new BigDecimal("150.00"), actualOrder.getOrderItems().get(1).getPrice());
        Assert.assertEquals("product 1", actualOrder.getOrderItems().get(0).getProductName());
        Assert.assertEquals(new BigDecimal("100.00"), actualOrder.getOrderItems().get(0).getAmount());
    }

    @Test
    public void getOrder_discount_for_2_pizza() {
        Product p1 = new Product();
        p1.setId("101");
        p1.setName("pizza 1");
        p1.setCategory(Product.PRODUCT_TYPE_PIZZA);
        p1.setPrice(BigDecimal.valueOf(100.0));
        Product p2 = new Product();
        p2.setId("102");
        p2.setName("pizza 2");
        p2.setCategory(Product.PRODUCT_TYPE_PIZZA);
        p2.setPrice(BigDecimal.valueOf(150.00));
        Product p3 = new Product();
        p3.setId("102");
        p3.setName("product 2");
        p3.setPrice(BigDecimal.valueOf(150.0));
        p3.setCategory(Product.PRODUCT_TYPE_BEVERAGE);
        expectedProducts.put("101", p1);
        expectedProducts.put("102", p2);
        expectedProducts.put("103", p3);

        Order order = new Order();
        OrderItem i1 = new OrderItem();
        i1.setProductId("101");
        i1.setQuantity(1);
        OrderItem i2 = new OrderItem();
        i2.setProductId("102");
        i2.setQuantity(1);
        OrderItem i3 = new OrderItem();
        i3.setProductId("103");
        i3.setQuantity(1);
        order.getOrderItems().add(i1);
        order.getOrderItems().add(i2);
        order.getOrderItems().add(i3);

        Order actualOrder = shopResource.calculateOrder(order);

        Assert.assertEquals(BigDecimal.valueOf(5.0), actualOrder.getDiscount());
        Assert.assertEquals(BigDecimal.valueOf(400.0), actualOrder.getAmount());
        Assert.assertEquals(BigDecimal.valueOf(380.0), actualOrder.getTotal());
    }

}
