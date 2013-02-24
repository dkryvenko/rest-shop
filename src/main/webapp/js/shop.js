$(document).ready(function () {
    $('#itemCarousel').carousel({
        interval: false
    });
    $('#headLineCarousel').carousel({
        interval: 5000
    });
    $("#submitOrder").click(function () {
        viewOrder();
    });
    $("#order").hide();
    makeViewOrderUnavailble();

    getProducts();
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

function getProducts() {
    $.getJSON('/shop/rest/product', function(data) {
        var productContainerRow;
        $.each(data, function(i, product) {
            if (i % 3 == 0) {
                productContainerRow = $("#product-container-row").clone();
                productContainerRow.removeAttr("id");
                productContainerRow.removeClass("hide");
                productContainerRow.appendTo("#productsContainer")
            }

            var productContainer = $("#product-container").clone();
            productContainer.removeAttr("id");

            productContainer.find(".product-name").text(product.name);
            productContainer.find(".product-description").text(product.description);
            productContainer.find(".product-medium-thumb").attr("src", product.mediumThumb);
            productContainer.find(".product-medium-thumb").click(function() {
                showProductDetail(product.id);
            });
            productContainer.find(".product-size").text(product.size);
            productContainer.find(".product-weight").text(product.weight);
            productContainer.find(".product-price").text(product.price);
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

function showItemDetails(name) {
    $("#" + name + " img").attr("src", "img/" + name + "_big.png");
    $("#" + name).modal();
}

function closeItemDetails(name) {
    $("#" + name).modal('hide');
}

function addItemToCart(id, product, price) {
    var itemCount = order.orderItems.length;
    for (var i = 0; i < itemCount; i++) {
        var orderItem = order.orderItems[i];
        if (orderItem.id == id) {
            orderItem.quantity++;
            $("#itemCount").html(getQuantity());
            calculateOrderTotal();
            return;
        }
    }

    var orderItem = {
        id: id,
        product: product,
        price: price,
        quantity: 1,
        amount: price
    }

    order.orderItems[itemCount] = orderItem;
    $("#itemCount").text(getQuantity());
    $("#box").attr("src", "img/box.png");
    calculateOrderTotal();
    makeViewOrderAvailable();
}

function removeItemFromCart(itemId) {
    var itemCount = order.orderItems.length;
    for (var i = 0; i < itemCount; i++) {
        var orderItem = order.orderItems[i];
        if (orderItem.id == itemId) {
            order.orderItems.splice(i, 1);
        }
    }
    calculateOrderTotal();

    if (getQuantity() == 0) {
        $("#box").attr("src", "img/box_empty.png");
        $("#itemCount").text("");
        makeViewOrderUnavailble();
    } else {
        $("#itemCount").text(getQuantity());
    }

    viewOrder();
}

function getQuantity() {
    var q = new Number(0);
    for (var i = 0; i < order.orderItems.length; i++) {
        q += new Number(order.orderItems[i].quantity);
    }
    return q;
}

function updateItemQuantity(itemId, quantity) {
    var itemCount = order.orderItems.length;
    for (var i = 0; i < itemCount; i++) {
        var orderItem = order.orderItems[i];
        if (orderItem.id == itemId) {
            orderItem.quantity = quantity;
        }
    }
    $("#itemCount").html(getQuantity());
    calculateOrderTotal();
    viewOrder();
}

function calculateOrderTotal() {
    var orderAmount = 0.0;
    var itemCount = order.orderItems.length;
    for (var i = 0; i < itemCount; i++) {
        var price = order.orderItems[i].price;
        var quantity = order.orderItems[i].quantity;
        var amount = price * quantity;
        order.orderItems[i].amount = amount;
        orderAmount += amount;
    }

    var orderQuantity = getQuantity();
    if (orderQuantity == 2) {
        order.discount = 0.05;
    } else if (orderQuantity == 3) {
        order.discount = 0.07;
    } else if (orderQuantity == 4) {
        order.discount = 0.10;
    } else if (orderQuantity == 5) {
        order.discount = 0.15;
    } else if (orderQuantity > 5) {
        order.discount = 0.20;
    } else {
        order.discount = 0.0;
    }

    order.amount = orderAmount;
    order.total = orderAmount - orderAmount * order.discount;
}

function viewOrder() {
    $("#orderItems").empty();
    var itemCount = order.orderItems.length;
    for (var i = 0; i < itemCount; i++) {
        var itemId = order.orderItems[i].id;
        var product = order.orderItems[i].product;
        var price = order.orderItems[i].price;
        var quantity = order.orderItems[i].quantity;
        var amount = order.orderItems[i].amount;

        var orderItem = $("#orderItem").clone();

        orderItem.removeAttr("style");
        orderItem.find("#product").text(product);
        orderItem.find("#price").text(price + " грн/шт");
        orderItem.find("#quantity").attr("item_id", itemId);
        orderItem.find("#quantity[item_id=" + itemId + "]").val(quantity);
        orderItem.find("#amount").text(amount + " грн")
        orderItem.find("#removeItem").attr("item_id", itemId);

        orderItem.appendTo("#orderItems");
    }
    $("#orderAmount").html(order.amount);
    $("#orderDiscount").html(order.discount * 100);
    $("#orderTotal").html(order.total);

    $("#orderAmount").formatCurrency({symbol: ''});
    $("#orderDiscount").formatCurrency({symbol: '', roundToDecimalPlace: 0});
    $("#orderTotal").formatCurrency({symbol: ''});

    $("#order").modal('show');
}

function clearOrder() {
    order.orderItems = [];
    $("#itemCount").text("");
    calculateOrderTotal();
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
        url: "/shop/rest/order",
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