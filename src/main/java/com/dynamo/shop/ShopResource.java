package com.dynamo.shop;

import com.dynamo.shop.model.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Dmytro Kryvenko
 * Date: 12/23/12
 */
@Path("/")
public class ShopResource {

    @GET
    @Path("/version")
    public String getVersion() {
        return "1.2";
    }

    @POST
    @Path("/order")
    @Consumes(MediaType.APPLICATION_JSON)
    public void createOrder(Order order) {
        System.out.printf("order received: %s", order);
    }

}
