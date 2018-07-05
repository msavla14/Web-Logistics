<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Retail Sales Analysis</title>
    </head>
    <body>
        <script type="text/javascript">
            <% Object imageName = request.getAttribute("imageName");%>

        </script>
        <div id="main_container">
            <%--header--%>
            <%@include file='header.jsp'%>
            <div id="main_content">
                <form:form commandName="RetailAnalysisWrapper" action="retailsalesanalysis.htm">

                    <div class="formBorder">
                        <form:errors path="*" cssClass="error"/>
                        <table id="formTable">
                            <tr>
                                <td>Retailer Name </td>
                                <td>
                                    <form:select path="supplierId">
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
                                    <form:select path="productId">
                                        <form:option label="Select" value="" />
                                        <c:forEach items="${retailProductList}" var="prod">
                                            <form:option label="${prod.rtProdName}" value="${prod.rtProdId}"></form:option>
                                        </c:forEach>
                                    </form:select>
                                </td>
                            </tr>
                            <tr>
                                <td>From Date</td>
                                <td>
                                    <form:input path="fromD"></form:input>[YYYY-MM-DD)
                                </td>
                            </tr>
                            <tr>
                                <td>To Date</td>
                                <td>
                                    <form:input path="toD"></form:input>(YYYY-MM-DD)
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <input type="submit" value="Plot Graph"/>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div>

                        <img SRC="jspchart/${imageName}" WIDTH="800" HEIGHT="400" BORDER="0" USEMAP="#chart"/>
                        <br/><br/><br/><br/>
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

