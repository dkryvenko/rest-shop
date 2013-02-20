package com.dynamo.shop;

import com.dynamo.shop.model.Order;
import com.dynamo.shop.model.OrderItem;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Author: Dmytro Kryvenko
 * Date: 12/23/12
 */
@Path("/")
@Component
public class ShopResource {

    private static final Logger LOGGER = Logger.getLogger(ShopResource.class);

    @Autowired
    private JavaMailSender mailSender;

    @GET
    @Path("/version")
    public String getVersion() {
        return "1.2";
    }

    @POST
    @Path("/order")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createOrder(Order order) throws Exception {
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
            body.append(orderItem.getProduct()).append("\t").append(orderItem.getPrice()).append("\t").
                    append(orderItem.getQuantity()).append("\t").append(orderItem.getAmount()).append("\n");
        }
        body.append("SUM: ").append(order.getAmount()).append("\n");
        body.append("DISCOUNT: ").append(order.getDiscount() * 100).append("\n");;
        body.append("TOTAL: ").append(order.getTotal()).append("\n");
        msg.setText(body.toString(), "UTF-8");
        mailSender.send(msg);
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

}
