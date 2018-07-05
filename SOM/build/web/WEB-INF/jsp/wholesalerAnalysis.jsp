<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ page import="java.util.*" %>

<%--<%@ page import="import org.jfree.chart.JFreeChart" %>--%>
<%@ page import="java.awt.image.BufferedImage" %>
<%--<%@ page import="java.io.PrintWriter" %>--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Wholesler Analysis</title>
    </head>
    <body>
        <script type="text/javascript">
           <% Object imageName = request.getAttribute("imageName");
            %>

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
                        <li><a href="wholesaleproducts.htm" title="">Laptops</a></li>
                        <li><a href="wholesaleproducts.htm" title="">Mouse</a></li>
                        <li><a <%--class="current"--%> href="wholesaleproducts.htm" title="">Keyboards</a></li>
                        <li><a href="wholesaleproducts.htm" title="">Intel Processors</a></li>
                        <li><a href="contact.htm" title="">Contact us for Other Products</a></li>
                    </ul>
                </div>
            </div><!--end of green box-->
            <div id="main_content">
                <form:form commandName="WholesalerProductsStock" action="wholesaleranalysis.htm">
                    <form:errors path="*" />
                    <div>
                        <img SRC="jspchart/${imageName}" alt="" WIDTH="750" HEIGHT="400" BORDER="0" USEMAP="#chart"/>
                        <br/><br/><br/><br/>
                    </div>
                    <%--<input type="submit" value="Plot"></input>--%>
                    <h2>This Page is under development and will have features similar to Retailer Analysis Page</h2>
                </form:form>


            </div><!--end of main content-->
            <%--footer--%>
            <%@include file='footer.jsp'%>
        </div> <!--end of main container-->
        <script type="text/javascript">
            document.getElementById("wholesalerAnalysis").className = 'current';
        </script>
    </body>
</html>

