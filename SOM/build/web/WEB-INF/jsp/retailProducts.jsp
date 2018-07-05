<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Retail Products</title>
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
                    <h1>Who is the best Retailer?</h1>
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
                    <form:form commandName="retailProducts" action="wholesaleproducts">
                        <form:errors path="*" cssClass="error"/>
                        <div>

                            <div>
                                <br/>
                                <h2><c:out value="${retailProductListSize}" ></c:out> <fmt:message key="table.productsFound"/></h2>
                                <br/>
                            </div>
                            <table id="customers">
                                <tr>
                                    <th>Product Name</th>
                                    <th>Product Description</th>
                                    <th>Category</th>
                                    <th>Sub-Category</th>
                                </tr>
                                <c:forEach items="${retailProductList}" var="prod">
                                    <tr class="alt">
                                        <td><a href="retailproductsuppliers.htm?productId=${prod.rtProdId}"> <c:out value="${prod.rtProdName}"/></a></td>
                                        <td><c:out value="${prod.rtProdDesc}"/></td>
                                        <td><c:out value="${prod.rtProdCatg}"/></td>
                                        <td><c:out value="${prod.rtProdSubCatg}"/></td>
                                    </tr>
                                </c:forEach>

                            </table>

                        </form:form>
                    </div>
                </div>
            </div><!--end of main content-->
            <%--footer--%>
            <%@include file='footer.jsp'%>
        </div> <!--end of main container-->
        <script type="text/javascript">
            document.getElementById("retailProducts").className = 'current';
        </script>
    </body>
</html>

