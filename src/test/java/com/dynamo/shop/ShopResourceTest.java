package com.dynamo.shop;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Author: Dmytro Kryvenko
 * Date: 12/23/12
 */
public class ShopResourceTest {

    ShopResource shopResource;

    @Before
    public void before() {
        shopResource = new ShopResource();
    }

    @Test
    public void getVersion() {
        String version = shopResource.getVersion();
        Assert.assertEquals("1.2", version);
    }

}
