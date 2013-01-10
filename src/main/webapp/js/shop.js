$(document).ready(function () {
    $("#order").hide();
    $("#submitOrder").click(function() {submitOrder();});
})

function submitOrder() {
    $.post('http://localhost:8080/shop/order');
}
