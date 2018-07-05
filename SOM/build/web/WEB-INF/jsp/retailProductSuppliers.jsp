<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Retail Product Suppliers</title>
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
                <div class="formBorder">
                    <form:form commandName="RetailerProductsStock">
                        <form:errors path="*" cssClass="error"/>


                        <div>
                            <c:forEach items="${retailProductList}" var="prod">
                                <h2><a href="retailproducts.htm">
                                        <c:out value="${prod.rtProdName}"/>
                                    </a></h2>
                                <h3><c:out value="${prod.rtProdDesc}"/></h3>

                            </c:forEach>
                        </div>
                        <table id="customers">
                            <tr>
                                <th>Supplier Name</th>
                                <th>Product Price</th>
                                <th>Currency</th>
                                <th>Minimum Delivery Days</th>
                                <th>Maximum Delivery Days</th>
                                <th>Available Quantity</th>
                                <th>Buy</th>
                            </tr>


                            <c:forEach items="${retailProductsStocksList}" var="prod">
                                <tr>

                                    <td><c:out value="${prod.retailer.rtName}"/>  </td>
                                    <td><c:out value="${prod.rtProdSellPrice}"/>  </td>
                                    <td><c:out value="${prod.rtProdPriceCurrency}"/>  </td>
                                    <td><c:out value="${prod.rtMinDeliveryDays}"/>  </td>
                                    <td><c:out value="${prod.rtMaxDeliveryDays}"/>  </td>
                                    <td><c:out value="${prod.rtStockAvailableQnty}"/>  </td>
                                    <td><a href="placeretailorders.htm?productId=${prod.retailProducts.rtProdId}&rtId=${prod.retailer.rtId}"> <c:out value="Buy"/></a></td>
                                </tr>
                            </c:forEach>



                        </table>
                    </form:form>
                </div>
            </div>
        </div><!--end of main content-->
        <%--footer--%>
        <%@include file='footer.jsp'%>
        <script type="text/javascript">
            document.getElementById("retailProducts").className = 'current';
        </script>
    </body
</html>

