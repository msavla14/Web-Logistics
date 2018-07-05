/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import som.domain.Consumer;
import som.domain.RetailOrders;
import som.domain.RetailProducts;
import som.domain.RetailerProductsStock;
import som.service.CommonManager;

/**
 *
 * @author mithun
 */
public class ClassifyController implements Controller {

    private static final Long RETAILER_ID = 1L;
    private static final Long RETAILER_PRODUCT_ID = 1L;
    private static final Long CONSUMER_ID = 1L;
    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    public ModelAndView handleRequest(HttpServletRequest req, HttpServletResponse res) throws Exception {

        String action = req.getParameter("ACTION");
        if (null != action) {
            String now = (new Date()).toString();
            PlaceRetailOrdersController retailOrdersController = new PlaceRetailOrdersController();
            retailOrdersController.setCommonManager(commonManager);
            RetailOrders retailOrders = new RetailOrders();
            List<RetailerProductsStock> rpsList = commonManager.fetchRetailerProductsStocksByProdIdAndRtId(RETAILER_PRODUCT_ID.toString(), RETAILER_ID.toString());
            for (RetailerProductsStock rps : rpsList) {
                retailOrders.setRetailerProductsStock(rps);
            }

            Consumer consumer = new Consumer();
            consumer.setConsId(CONSUMER_ID);
            retailOrders.setConsumer(consumer);

            Calendar date = new GregorianCalendar(2010, 06, 06);

            if (action.equalsIgnoreCase("DEC")) {
                for (int i = 40; i > 0; --i) {
                    //###############################
                    int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);

                    retailOrders.setRtOrderDate(date.getTime());
                    retailOrders.setRtOrdQnty(i);
                    retailOrdersController.onSubmit(req, res, retailOrders, null);
                    System.out.println("############ i = " + i + "date = " + retailOrders.getRtOrderDate());
                    int nextDay = dayOfMonth + 1;
                    date.set(Calendar.DAY_OF_MONTH, nextDay);
                }
            } else if (action.equalsIgnoreCase("INC")) {

                for (int i = 1; i < 40; i++) {
                    //###############################
                    int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);

                    retailOrders.setRtOrderDate(date.getTime());
                    retailOrders.setRtOrdQnty(i);
                    retailOrdersController.onSubmit(req, res, retailOrders, null);
                    System.out.println("############ i = " + i + "date = " + retailOrders.getRtOrderDate());
                    int nextDay = dayOfMonth + 1;
                    date.set(Calendar.DAY_OF_MONTH, nextDay);
                }

            } else if (action.equalsIgnoreCase("RAND")) {
                Random randomGenerator = new Random();
                for (int i = 0; i < 40; i++) {
                    //###############################
                    int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
                    retailOrders.setRtOrderDate(date.getTime());
                    int randomInt = randomGenerator.nextInt(15);
                    retailOrders.setRtOrdQnty(randomInt);
                    retailOrdersController.onSubmit(req, res, retailOrders, null);
                    System.out.println("############ i = " + i + "date = " + retailOrders.getRtOrderDate());
                    int nextDay = dayOfMonth + 1;
                    date.set(Calendar.DAY_OF_MONTH, nextDay);
                }
            }
        }
        return new ModelAndView("classify");
    }
}
