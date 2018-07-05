<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Place Retail Order</title>
    </head>
    <body>
        <div id="main_container">
            <%--header--%>
            <%@include file='header.jsp'%>

            <div class="green_box">
                <div class="clock">
                    <img src="images/clock.png" alt="" title="" />
                </div>

                <div class="text_content">
                    <h1>Who is the best supplier?</h1>
                    <p class="green">

                    </p>
                    <div class="read_more"><a href="classify.htm">read more</a></div>
                </div>

                <div id="right_nav">
                    <ul>
                        <li><a href="retailproducts.htm" title="">Laptops</a></li>
                        <li><a href="retailproducts.htm" title="">Mouse</a></li>
                        <li><a <%--class="current"--%> href="retailproducts.htm" title="">Keyboards</a></li>
                        <li><a href="retailproducts.htm" title="">Intel Processors</a></li>
                        <li><a href="contact.htm" title="">Contact us for Other Products</a></li>
                    </ul>
                </div>
            </div><!--end of green box-->
            <div id="main_content">
                <form:form commandName="retailOrders" action="placeretailorders.htm?productId=${retailOrders.retailerProductsStock.retailProducts.rtProdId}&rtId=${retailOrders.retailerProductsStock.retailer.rtId}">
                    <div class="formBorder">
                        <form:errors path="*" cssClass="error"/>
                        <div>
                            <h2><a href="retailproductsuppliers.htm?productId=${retailOrders.retailerProductsStock.retailProducts.rtProdId}">${retailOrders.retailerProductsStock.retailProducts.rtProdName}</a></h2>
                            <h3>${retailOrders.retailerProductsStock.retailProducts.rtProdDesc}</h3>
                            <h3>${retailOrders.retailerProductsStock.retailer.rtName}</h3>
                            <h3></h3>
                        </div>
                        <table id="formTable">
                            <tr>
                                <td>Consumer Name (If you are registered)</td>
                                <td>
                                    <form:select path="consumer.consId">
                                        <form:option label="Select" value="0" />
                                        <c:forEach items="${consumerList}" var="consumers">
                                            <form:option label="${consumers.consName}" value="${consumers.consId}"></form:option>
                                        </c:forEach>
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td>Product Cost Per Unit</td>
                                <td>
                                    <label id="costPerUnit">${retailOrders.retailerProductsStock.rtProdSellPrice} ${retailOrders.retailerProductsStock.rtProdPriceCurrency} </label>
                                </td>
                            </tr>
                            <tr>
                                <td>No Of Units</td>
                                <td><form:input path="rtOrdQnty" id="prodQuantity" onblur="calculateTotalCost();"/></td>
                            </tr>
                            <tr>
                                <td>Total Cost</td>
                                <td>
                                    <label id="totalCost"></label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:hidden path="retailerProductsStock.retailProducts.rtProdId" id="prodId"></form:hidden>
                                    <form:hidden path="retailerProductsStock.retailer.rtId" id="retailerId" ></form:hidden>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="submit" value="Place Order"/>
                                </td>
                            </tr>
                        </table>
                    </div>
                </form:form>


            </div><!--end of main content-->
            <%--footer--%>
            <%@include file='footer.jsp'%>
        </div> <!--end of main container-->
        <script type="text/javascript">
            document.getElementById("retailProducts").className = 'current';

            function calculateTotalCost()
            {
                var costPerUnit=${retailOrders.retailerProductsStock.rtProdSellPrice};
                var noOfUnits = document.getElementById("prodQuantity").value;

                document.getElementById("totalCost").innerHTML=costPerUnit*noOfUnits+ " GBP";
            }
        </script>
    </body>
</html>

