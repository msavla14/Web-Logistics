<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Profit Margin Analysis</title>
    </head>
    <body>
        <script type="text/javascript">
            <% Object imageName = request.getAttribute("imageName");
            %>

        </script>
        <div id="main_container">
            <%--header--%>
            <%@include file='header.jsp'%>
            <div id="main_content">
                <form:form commandName="RetailerProductsStock" action="retailerprofitmargins.htm">

                    <div class="formBorder">
                        <form:errors path="*" cssClass="error"/>
                        <table id="formTable">
                            <tr>
                                <td>Retailer Name </td>
                                <td>
                                    <form:select path="retailer.rtId">
                                        <form:option label="Select" value="" />
                                        <c:forEach items="${retailersList}" var="retailers">
                                            <form:option label="${retailers.rtName}" value="${retailers.rtId}"></form:option>
                                        </c:forEach>
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td>Product Name</td>
                                <td>
                                    <form:select path="retailProducts.rtProdId">
                                        <form:option label="Select" value="" />
                                        <c:forEach items="${retailProductList}" var="prod">
                                            <form:option label="${prod.rtProdName}" value="${prod.rtProdId}"></form:option>
                                        </c:forEach>
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="submit" value="Plot Graph"/>
                                </td>
                            </tr>
                        </table>


                        <div>
                            <img SRC="jspchart/${imageName}" WIDTH="750" HEIGHT="400" BORDER="0" USEMAP="#chart"/>
                            <br/><br/><br/><br/>
                        </div>
                    </div>
                </form:form>


            </div><!--end of main content-->
            <%--footer--%>
            <%@include file='footer.jsp'%>
        </div> <!--end of main container-->
        <script type="text/javascript">
            document.getElementById("retailerAnalysis").className = 'current';
        </script>
    </body>
</html>

