<%@ page import="com.dynamo.shop.ShopResource" %>
<html>
<body>
<h2>Hello eShop!</h2>
<%=(new ShopResource()).getVersion()%>
</body>
</html>
