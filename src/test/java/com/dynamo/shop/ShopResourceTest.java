package com.dynamo.shop;

import com.dynamo.shop.model.Product;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;

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
    public void getVersion() {
        String version = shopResource.getVersion();
        Assert.assertEquals("1.2", version);
    }

    @Test
    public void getProducts() {
        Product p1 = new Product();
        Product p2 = new Product();
        expectedProducts.put("101", p1);
        expectedProducts.put("102", p2);

        List<Product> actualProducts = shopResource.getProduct();

        Assert.assertEquals(2, actualProducts.size());
        Assert.assertEquals(p1, actualProducts.get(1));
        Assert.assertEquals(p2, actualProducts.get(0));
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
    public void getProductsByCategory() {
        Product p1 = new Product();
        p1.setCategory("c1");
        Product p2 = new Product();
        p2.setCategory("c2");
        Product p3 = new Product();
        p3.setCategory("c3");
        Product p4 = new Product();
        p4.setCategory("c2");
        expectedProducts.put("101", p1);
        expectedProducts.put("102", p2);
        expectedProducts.put("103", p3);
        expectedProducts.put("104", p4);


        List<Product> actualProducts = shopResource.getProductsByCategory("c2");

        Assert.assertEquals(2, actualProducts.size());
        Assert.assertEquals(p4, actualProducts.get(0));
        Assert.assertEquals(p2, actualProducts.get(1));
    }


}
