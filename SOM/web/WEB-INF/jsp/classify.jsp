<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>HOME</title>   
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
                        <li><a href="wholesaleproducts.htm" title="Wholesale Products">Wholesale Products</a></li>
                        <li><a href="retailproducts.htm" title="Retail Products">Retail Products</a></li>
                        <li><a class="current" href="wholesaleranalysis.htm" title="Wholesale Analysis">Wholesale Analysis</a></li>
                        <li><a href="retaileranalysis.htm" title="Retail Analysis">Retail Analysis</a></li>
                        <li><a href="contact.htm" title="Contact us">Contact us for Other Products</a></li>
                    </ul>
                </div>
            </div><!--end of green box-->
            <div id="main_content">
                <form:form action="classify.htm" >
                    <div class="formBorder">
                        <%--<div id="left_content">

                        </div>--%>
                        <!--end of left content-->

                        <div id="left_content">
                            <div class="contact_information">
                                <h4>Free Customers Support</h4>
                                <p>
                                    <img src="images/phone_icon.gif" alt="" title="" class="box_img" />
                                    0700 679 122 489<br />
                                    0700 679 122 489
                                </p>
                                <br /><br />
                                <p>
                                    <img src="images/contact_icon.gif" alt="" title="" class="box_img" />
                                    info@company.com<br />
                                    contact@company.com
                                </p>

                            </div>

                        </div><!--end of right content-->
                        <input type="submit" value="INC" name="ACTION"/>
                        <input type="submit" value="DEC" name="ACTION"/>
                        <input type="submit" value="RAND" name="ACTION"/>
                        
                    </div>
                </form:form>
            </div><!--end of main content-->
            <%--footer--%>
            <%@include file='footer.jsp'%>
        </div> <!--end of main container-->
        <script type="text/javascript">
            document.getElementById("home").className = 'current';
        </script>
    </body>
</html>

