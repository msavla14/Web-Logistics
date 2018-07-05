<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Retailer Analysis</title>
    </head>
    <body>
        <script type="text/javascript">
            <% Object imageName = request.getAttribute("imageName");%>

        </script>
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
                <form:form commandName="RetailerProductsStock" action="retaileranalysis.htm">
                    <form:errors path="*" />
                    <div class="formBorder">
                        <div id="left_content" style="width:90%">
                            <h3><a href="retailerprofitmargins.htm"> Profit Margin Analysis</a></h3>
                            <br/><br/>
                            <h3><a href="retailsalesanalysis.htm"> Retail Sales Analysis</a></h3>
                            <br/><br/>
                            <h3><a href="retailsalesforecastinganalysis.htm"> Retail Sales Forecasting Analysis</a></h3>
                            <br/><br/>
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

