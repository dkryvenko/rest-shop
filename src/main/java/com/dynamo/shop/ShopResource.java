package com.dynamo.shop;

import com.dynamo.shop.model.Order;
import com.dynamo.shop.model.OrderItem;
import com.dynamo.shop.model.Product;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Author: Dmytro Kryvenko
 * Date: 12/23/12
 */
@Path("/")
public class ShopResource {

    private static final Logger LOGGER = Logger.getLogger(ShopResource.class);

    private static final String STATUS_RUNNING = "running";

    private final JavaMailSender mailSender;
    private final Map<String, Product> products;

    public ShopResource(Map<String, Product> products, JavaMailSender mailSender) {
        this.products = products;
        this.mailSender = mailSender;
    }

    @GET
    @Path("/status")
    public String getStatus() {
        return STATUS_RUNNING;
    }

    @GET
    @Path("/product/{id}")
    @Produces("application/json;charset=utf8")
    public Product getProduct(@PathParam("id") String id) {
        return products.get(id);
    }

    @GET
    @Path("/product")
    @Produces("application/json;charset=utf8")
    public List<Product> getProducts(@QueryParam("category") String category) {
        if (category == null) {
            return new LinkedList<Product>(products.values());
        } else {
            Collection<Product> allProducts = products.values();
            List<Product> filteredProducts = new LinkedList<Product>();
            for(Product product: allProducts) {
                if (category.equals(product.getCategory())) {
                    filteredProducts.add(product);
                }
            }
            return filteredProducts;
        }
    }

    @POST
    @Path("/calculatedOrder")
    @Consumes("application/json;charset=utf8")
    @Produces("application/json;charset=utf8")
    public Order calculateOrder(Order order) {
        BigDecimal orderAmount =  BigDecimal.valueOf(0.0);
        BigDecimal discount = BigDecimal.valueOf(0.0);
        int pizzaItemCount = 0;

        for (OrderItem orderItem: order.getOrderItems()) {
            Product product = products.get(orderItem.getProductId());

            BigDecimal price = product.getPrice();
            String productName = product.getName();

            int quantity = orderItem.getQuantity();
            BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

            orderItem.setPrice(price);
            orderItem.setAmount(amount);
            orderItem.setProductName(productName);

            orderAmount = orderAmount.add(amount);

            if (Product.PRODUCT_TYPE_PIZZA.equals(product.getCategory())) {
                pizzaItemCount += quantity;
            }
        }

        if (pizzaItemCount == 2) {
            discount = BigDecimal.valueOf(5.0);
        } else if (pizzaItemCount == 3) {
            discount = BigDecimal.valueOf(7.0);
        } else if (pizzaItemCount == 4) {
            discount = BigDecimal.valueOf(10.0);
        } else if (pizzaItemCount >= 5) {
            discount = BigDecimal.valueOf(20.0);
        }

        order.setAmount(orderAmount);
        order.setDiscount(discount);
        BigDecimal orderDiscount = orderAmount.multiply(discount).divide(BigDecimal.valueOf(100.0));
        order.setTotal(orderAmount.subtract(orderDiscount));
        return order;
    }

    @POST
    @Path("/submittedOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    public void submitOrder(Order order) throws Exception {
        LOGGER.info("Order received.");
        LOGGER.info("Customer: " + order.getName());
        LOGGER.info("Address: " + order.getAddress());
        MimeMessage msg = mailSender.createMimeMessage();
        msg.setFrom(new InternetAddress("pizzavivo@gmail.com"));
        msg.setRecipient(Message.RecipientType.TO, new InternetAddress("pizzavivo@gmail.com"));
        msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("dmytro.kryvenko@gmail.com,tetyana.kryvenko@gmail.com"));
        msg.setSubject("PIZZA DELIVERY ORDER");
        StringBuilder body = new StringBuilder();
        body.append("CUSTOMER:").append("\n");
        body.append("Name: ").append(order.getName()).append("\n");
        body.append("Phone: ").append(order.getPhone()).append("\n");
        body.append("Address: ").append(order.getAddress()).append("\n");
        body.append("Comments: ").append(order.getComments()).append("\n");
        body.append("ORDER:").append("\n");
        for (OrderItem orderItem : order.getOrderItems()) {
            body.append(orderItem.getProductName()).append("\t").append(orderItem.getPrice()).append("\t").
                    append(orderItem.getQuantity()).append("\t").append(orderItem.getAmount()).append("\n");
        }
        body.append("SUM: ").append(order.getAmount()).append("\n");
        body.append("DISCOUNT: ").append(order.getDiscount().multiply(new BigDecimal("100.0"))).append("\n");
        body.append("TOTAL: ").append(order.getTotal()).append("\n");
        msg.setText(body.toString(), "UTF-8");
        mailSender.send(msg);
    }

}
