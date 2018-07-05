<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Place Wholesale Order</title>
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
                        <li><a href="wholesaleproducts.htm" title="">Laptops</a></li>
                        <li><a href="wholesaleproducts.htm" title="">Mouse</a></li>
                        <li><a <%--class="current"--%> href="wholesaleproducts.htm" title="">Keyboards</a></li>
                        <li><a href="wholesaleproducts.htm" title="">Intel Processors</a></li>
                        <li><a href="contact.htm" title="">Contact us for Other Products</a></li>
                    </ul>
                </div>
            </div><!--end of green box-->
            <div id="main_content">
                <form:form commandName="wholesaleOrders" action="placewholesaleorders.htm?productId=${wholesaleOrders.wholesalerProductsStock.wholesaleProducts.whProdId}&whId=${wholesaleOrders.wholesalerProductsStock.wholesaler.whId}">
                    
                    <div class="formBorder">
                        <form:errors path="*" cssClass="error"/>
                        <div>
                            <h2><a href="wholesaleproductsuppliers.htm?productId=${wholesaleOrders.wholesalerProductsStock.wholesaleProducts.whProdId}">${wholesaleOrders.wholesalerProductsStock.wholesaleProducts.whProdName}</a></h2>
                            <h3>${wholesaleOrders.wholesalerProductsStock.wholesaleProducts.whProdDesc}</h3>
                            <h3>${wholesaleOrders.wholesalerProductsStock.wholesaler.whName}</h3>
                            <h3></h3>
                        </div>
                        <table id="formTable">
                            <tr>
                                <td>Retailer Name (If you are registered)</td>
                                <td>
                                    <form:select path="retailer.rtId">
                                        <form:option label="Select" value="0" />
                                        <c:forEach items="${retailerList}" var="retailers">
                                            <form:option label="${retailers.rtName}" value="${retailers.rtId}"></form:option>
                                        </c:forEach>
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td>Product Cost Per Unit</td>
                                <td>
                                    <label id="costPerUnit">${wholesaleOrders.wholesalerProductsStock.whProdSellPrice} ${wholesaleOrders.wholesalerProductsStock.whProdPriceCurrency} </label>
                                </td>
                            </tr>
                            <tr>
                                <td>No Of Units</td>
                                <td><form:input path="whOrdQnty" id="prodQuantity" onblur="calculateTotalCost();"/></td>
                            </tr>
                            <tr>
                                <td>Total Cost</td>
                                <td>
                                    <label id="totalCost"></label>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <form:hidden path="wholesalerProductsStock.wholesaleProducts.whProdId" id="prodId"></form:hidden>
                                    <form:hidden path="wholesalerProductsStock.wholesaler.whId" id="wholesalerId" ></form:hidden>
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
            document.getElementById("wholesaleProducts").className = 'current';

            function calculateTotalCost()
            {
                var costPerUnit=${wholesaleOrders.wholesalerProductsStock.whProdSellPrice};
                var noOfUnits = document.getElementById("prodQuantity").value;

                document.getElementById("totalCost").innerHTML=costPerUnit*noOfUnits+ " GBP";
            }
        </script>
    </body>
</html>

