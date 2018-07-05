<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Wholesale Products</title>
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
                <div class="formBorder">
                    <form:form commandName="WholesaleProducts" action="wholesaleproducts">
                        <form:errors path="*" cssClass="error"/>
                   <%--     <h2>Looking For</h2>
                        <form:select id="" path="whSubCatg">
                            <form:option label="Select" value="0" />
                            <c:forEach items="${subCatgList}" var="prod">
                                <form:option label="${prod}" value="${prod}"></form:option>
                            </c:forEach>
                        </form:select>
                        <input type="submit" value="Go"/>--%>

                        <div>
                            <div>
                                <br/>
                                <h2><c:out value="${wholesaleProductListSize}" ></c:out> <fmt:message key="table.productsFound"/></h2>
                                <br/>
                            </div>
                            <table id="customers">
                                <tr>
                                    <th>Product Name</th>
                                    <th>Product Description</th>
                                    <th>Category</th>
                                    <th>Sub-Category</th>
                                </tr>
                                <%-- <c:out value="${wholesaleProductList.WholesaleProducts}"/>--%>
                                <c:forEach items="${wholesaleProductList}" var="prod">
                                    <tr class="alt">
                                        <td><a href="wholesaleproductsuppliers.htm?productId=${prod.whProdId}"> <c:out value="${prod.whProdName}"/></a></td>
                                        <td><c:out value="${prod.whProdDesc}"/></td>
                                        <td><c:out value="${prod.whProdCatg}"/></td>
                                        <td><c:out value="${prod.whSubCatg}"/></td>                                        
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
            document.getElementById("wholesaleProducts").className = 'current';
        </script>
    </body>
</html>

