$(document).ready(function () {
    $('#itemCarousel').carousel({
        interval: false
    });
    $('#headLineCarousel').carousel({
        interval: 5000
    });
    $("#submitOrder").click(function () {
        calculateAndShowOrder();
    });
    $("#order").hide();
    makeViewOrderUnavailble();

    showPizzas();
})

var order = {
    orderItems: [],
    name: "",
    address: "",
    phone: "",
    comments: "",
    amount: 0.0,
    discount: 0.0,
    total: 0.0
}

function showPizzas() {
    $.getJSON('/shop/rest/product?category=pizza', function(data) {
        $("#productsContainer").empty();
        var productContainerRow;
        $.each(data, function(i, product) {
            if (i % 3 == 0) {
                productContainerRow = $("#product-container-row").clone();
                productContainerRow.removeAttr("id");
                productContainerRow.removeClass("hide");
                productContainerRow.appendTo("#productsContainer")
            }

            var productContainer = $("#product-container-1").clone();
            productContainer.removeAttr("id");

            productContainer.find(".product-name").text(product.name);
            productContainer.find(".product-description").text(product.description);
            productContainer.find(".product-medium-thumb").attr("src", product.mediumThumb);
            productContainer.find(".product-medium-thumb").click(function() {
                showProductDetail(product.id);
            });
            productContainer.find(".product-size").text(product.size + " см");
            productContainer.find(".product-weight").text(product.weight + " грамм");
            productContainer.find(".product-price").text(product.price);
            productContainer.find(".product-price").formatCurrency({positiveFormat: '%n %s', symbol: ' грн'});
            productContainer.find(".btn-add-to-cart").click(function() {
                addItemToCart(product.id);
            });
            productContainer.attr("data-product-id", product.id);
            productContainer.removeClass("hide");

            productContainer.appendTo(productContainerRow);
        })
    })
}

function showSalads() {
    $.getJSON('/shop/rest/product?category=salad', function(data) {
        $("#productsContainer").empty();
        var productContainerRow;
        $.each(data, function(i, product) {
            if (i % 3 == 0) {
                productContainerRow = $("#product-container-row").clone();
                productContainerRow.removeAttr("id");
                productContainerRow.removeClass("hide");
                productContainerRow.appendTo("#productsContainer")
            }

            var productContainer = $("#product-container-2").clone();
            productContainer.removeAttr("id");

            productContainer.find(".product-name").text(product.name);
            productContainer.find(".product-description-2").text(product.description);
            productContainer.find(".product-medium-thumb").attr("src", product.mediumThumb);
            productContainer.find(".product-weight").text(product.weight + " грамм");
            productContainer.find(".product-price").text(product.price);
            productContainer.find(".product-price").formatCurrency({positiveFormat: '%n %s', symbol: ' грн'});
            productContainer.find(".btn-add-to-cart").click(function() {
                addItemToCart(product.id, product.name, product.price);
            });
            productContainer.attr("data-product-id", product.id);
            productContainer.removeClass("hide");

            productContainer.appendTo(productContainerRow);
        })
    })
}

function showBeverages() {
    $.getJSON('/shop/rest/product?category=beverage', function(data) {
        $("#productsContainer").empty();
        var productContainerRow;
        $.each(data, function(i, product) {
            if (i % 3 == 0) {
                productContainerRow = $("#product-container-row").clone();
                productContainerRow.removeAttr("id");
                productContainerRow.removeClass("hide");
                productContainerRow.appendTo("#productsContainer")
            }

            var productContainer = $("#product-container-3").clone();
            productContainer.removeAttr("id");

            productContainer.find(".product-name").text(product.name);
            productContainer.find(".product-medium-thumb").attr("src", product.mediumThumb);
            productContainer.find(".product-size").text(product.size + ' л');
            productContainer.find(".product-price").text(product.price);
            productContainer.find(".product-price").formatCurrency({positiveFormat: '%n %s', symbol: ' грн'});
            productContainer.find(".btn-add-to-cart").click(function() {
                addItemToCart(product.id, product.name, product.price);
            });
            productContainer.attr("data-product-id", product.id);
            productContainer.removeClass("hide");

            productContainer.appendTo(productContainerRow);
        })
    })
}

function showProductDetail(id) {
    $.getJSON('/shop/rest/product/' + id, function(product) {
        var productDetail = $("#product-detail");
        productDetail.find(".product-detail-name").text(product.name);
        productDetail.find(".product-detail-thumb").attr("src", product.largeThumb);
        productDetail.find(".product-detail-thumb").mouseover()
        productDetail.find(".product-detail-description").text(product.description);
        productDetail.find(".product-detail-size").text(product.size);
        productDetail.find(".product-detail-weight").text(product.weight);
        productDetail.find(".product-detail-price").text(product.price);
        productDetail.find(".product-detail-btn-add-to-cart").click(function() {
            addItemToCart(product.id, product.name, product.price);
            closeProductDetail();
        });
        productDetail.find(".product-detail-btn-close").click(function() {
            closeProductDetail();
        })
        productDetail.modal("show");
    });
}

function closeProductDetail() {
    $("#product-detail").modal("hide");
}

function makeViewOrderAvailable() {
    $("#submitOrder").show();
}

function makeViewOrderUnavailble() {
    $("#submitOrder").hide();
}

function addItemToCart(productId) {
    var itemCount = order.orderItems.length;
    var orderItem = {
        productId: productId,
        quantity: 1
    }
    order.orderItems[itemCount] = orderItem;
    makeViewOrderAvailable();
}

function removeItemFromCart(productId) {
    var itemCount = order.orderItems.length;
    for (var i = 0; i < itemCount; i++) {
        var orderItem = order.orderItems[i];
        if (orderItem.productId == productId) {
            order.orderItems.splice(i, 1);
        }
    }
}

function updateItemQuantity(productId, quantity) {
    var itemCount = order.orderItems.length;
    for (var i = 0; i < itemCount; i++) {
        var orderItem = order.orderItems[i];
        if (orderItem.productId == productId) {
            orderItem.quantity = quantity;
        }
    }
}

function calculateAndShowOrder() {
    $.ajax({
        url: "/shop/rest/calculatedOrder",
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(order),
        success: function(calculatedOrder) {
            $("#orderItems").empty();
            $.each(calculatedOrder.orderItems, function(i, item) {
                var orderItem = $("#orderItem").clone();
                orderItem.removeAttr("id");
                orderItem.removeAttr("style");
                orderItem.find(".order-item-product-name").text(item.productName);
                orderItem.find(".order-item-price").text(item.price + " грн/шт");
                orderItem.find(".order-item-quantity").val(item.quantity);
                orderItem.find(".order-item-quantity").change(function() {
                    updateItemQuantity(item.productId, orderItem.find(".order-item-quantity").val());
                    calculateAndShowOrder();
                });
                orderItem.find(".order-item-amount").text(item.amount + " грн");
                orderItem.appendTo("#orderItems");
            });
            $("#orderAmount").html(calculatedOrder.amount);
            $("#orderDiscount").html(calculatedOrder.discount);
            $("#orderTotal").html(calculatedOrder.total);

            $("#orderAmount").formatCurrency({symbol: ''});
            $("#orderDiscount").formatCurrency({symbol: '', roundToDecimalPlace: 0});
            $("#orderTotal").formatCurrency({symbol: ''});

            $("#order").modal("show");
        }
    });
}

function clearOrder() {
    order.orderItems = [];
    $("#itemCount").text("");
}

function submitOrder() {
    if ($("#name").val() == '') {
        $("#validationWarning").modal();
        $("#name").focus();
        return;
    }

    if ($("#address").val() == '') {
        $("#validationWarning").modal();
        $("#address").focus();
        return;
    }

    if ($("#phone").val() == '') {
        $("#validationWarning").modal();
        $("#phone").focus();
        return;
    }

    order.name = $("#name").val();
    order.address = $("#address").val();
    order.phone = $("#phone").val();
    order.comments = $("#comments").val();

    jQuery.ajax ({
        url: "/shop/rest/submittedOrder",
        type: "POST",
        data: JSON.stringify(order),
        dataType: "json",
        contentType: "application/json; charset=utf-8"
    });
    clearOrder();
    makeViewOrderUnavailble();
    $("#order").modal('hide');
    $("#orderConfirmation").modal();
}

function changeQuantityHandler(event) {
    var itemId = event.target.attributes["item_id"].value;
    var quantity = event.target.value;
    updateItemQuantity(itemId, quantity);

}

function removeItemHandler(event) {
    var itemId = event.target.attributes["item_id"].value;
    removeItemFromCart(itemId);
}

function onMouseOverHandler(event) {
    event.target.style.opacity = 0.8;
}

function onMouseOutHandler(event) {
    event.target.style.opacity = 1;
}

function navigate(category) {
    $("[id^=nav-]").removeClass("active");
    $("#nav-" + category).addClass("active");
    if (category == 'pizza') {
        showPizzas();
    } else if (category == 'salad') {
        showSalads();
    } else if (category == 'beverage') {
        showBeverages();
    }
}