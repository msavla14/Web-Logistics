/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.Consumer;
import som.domain.RetailOrderStatus;
import som.domain.RetailOrders;
import som.domain.RetailProducts;
import som.domain.Retailer;
import som.domain.RetailerProductsStock;
import som.domain.WholesaleOrderStatus;
import som.domain.WholesaleOrders;
import som.domain.WholesaleProducts;
import som.domain.WholesalerProductsStock;
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class PlaceRetailOrdersController extends SimpleFormController {
    // The class performs and manages the ordering process for the retailers.

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    /* Prepares the object with initial data required. */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        RetailOrders ro = (RetailOrders) super.formBackingObject(request);
        String productId = request.getParameter("productId");
        String retailerId = request.getParameter("rtId");
        RetailerProductsStock rps = new RetailerProductsStock();
        rps.setRetailer(new Retailer());
        rps.setRetailProducts(new RetailProducts());
        ro.setRetailerProductsStock(rps);
        ro.setConsumer(new Consumer());
        if (null != productId && null != retailerId && !productId.isEmpty() && !retailerId.isEmpty()) {
            List<RetailerProductsStock> list = commonManager.fetchRetailerProductsStocksByProdIdAndRtId(productId, retailerId);
            for (RetailerProductsStock rpsObj : list) {
                ro.setRetailerProductsStock(rpsObj);
            }
            request.setAttribute("consumerList", commonManager.getConsumersList());
        }
        return ro;
    }

    /*
    Performs ordering on submit request by the user.
    Also an automated ordering process is triggered based on the stock available with the retailer.
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, org.springframework.validation.BindException errors) throws Exception {
        if (null != command && command.getClass().equals(RetailOrders.class)) {
            RetailOrders retailOrders = (RetailOrders) command;
            String rtId = retailOrders.getRetailerProductsStock().getRetailer().getRtId().toString();
            String rtProdId = retailOrders.getRetailerProductsStock().getRetailProducts().getRtProdId().toString();
            List<RetailerProductsStock> list = commonManager.fetchRetailerProductsStocksByProdIdAndRtId(rtProdId, rtId);
            for (RetailerProductsStock rps : list) {
                retailOrders.setRetailerProductsStock(rps);
            }
            RetailOrderStatus status = new RetailOrderStatus();
            status.setRtStatusId(1);
            retailOrders.setRtOrdPrice(retailOrders.getRetailerProductsStock().getRtProdSellPrice() * retailOrders.getRtOrdQnty());
            if (null == retailOrders.getRtOrderDate()) {
                retailOrders.setRtOrderDate(new Date());
            }
            retailOrders.setRetailOrderStatus(status);
            System.out.println(retailOrders.toString());
            commonManager.addRetailOrder(retailOrders);
            Integer remainingQuantity = retailOrders.getRetailerProductsStock().getRtStockAvailableQnty() - retailOrders.getRtOrdQnty();
            Integer lowThresholdQnty = retailOrders.getRetailerProductsStock().getRtStockLowThrQnty();
            if (remainingQuantity <= retailOrders.getRetailerProductsStock().getRtStockLowThrQnty()) {
                // forecast the sales
                Long forecastValue = forecastQuantityForAutomaticOrdering(retailOrders);
                Integer minOrderQnty = (lowThresholdQnty - remainingQuantity);
                if (forecastValue >= minOrderQnty && forecastValue <= retailOrders.getRetailerProductsStock().getRtStockUpThrQnty()) {
                    // automate ordering process function call
                    automateOrdering(retailOrders, forecastValue);
                } else {
                    automateOrdering(retailOrders, minOrderQnty.longValue());
                }
            }

            return new ModelAndView(getSuccessView());
        } else {
            throw new Exception("Invalid Command Class");
        }
    }

    /**
     * Automated ordering process for retailer based on the threshold levels.
     * Performs forecasting and best supplier selection and then decided the quantity to be ordered.
     * 
     * @param RetailOrders retailOrders
     * @param Long forecastValue
     */
    public void automateOrdering(RetailOrders retailOrders, Long forecastValue) {
        Long productId = retailOrders.getRetailerProductsStock().getRetailProducts().getRtProdId();
        // find the retailer product id mapped to wholesaler product id
        List<WholesaleProducts> wpList = commonManager.fetchWholesaleProductByRetailProductId(productId);
        Long whProductId = 0L;
        if (wpList != null && !wpList.isEmpty()) {
            for (WholesaleProducts wp : wpList) {
                whProductId = wp.getWhProdId();
                continue;
            }
        } else {
            // if retailer product is not mapped then ordering automation cannot take place
            return;
        }
        // find the wholesalers list based on the found wholesale product id


        List<WholesalerProductsStock> wpsList = commonManager.fetchWholesalerProductsStocksByProductId(whProductId.toString());

        /*
        The best supplier will be first and hence select the wholesaler for buying.
        If selected wholesaler for buying is null then select the first wholesaler.
        Place the order with the selected wholesaler.
         */

        Long pendingQuantity = forecastValue;
        for (WholesalerProductsStock wholesalerProductsStock : wpsList) {
            if (wholesalerProductsStock.getWhStockAvailableQnty() > 0 && pendingQuantity > 0) { // and pending quantity > 0

                if (wholesalerProductsStock.getWhStockAvailableQnty() >= forecastValue) {
                    WholesaleOrders wo = new WholesaleOrders();
                    wo.setWholesalerProductsStock(wholesalerProductsStock);
                    WholesaleOrderStatus status = new WholesaleOrderStatus();
                    status.setWhStatusId(1);
                    wo.setWhOrdQnty(forecastValue.intValue());
                    wo.setWhOrdPrice(wo.getWholesalerProductsStock().getWhProdSellPrice() * forecastValue);
                    wo.setWhOrderDate(retailOrders.getRtOrderDate());
                    wo.setWholesaleOrderStatus(status);
                    wo.setRetailer(retailOrders.getRetailerProductsStock().getRetailer());
                    commonManager.addWholesaleOrder(wo);
                    pendingQuantity = forecastValue - wo.getWhOrdQnty();
                    break;

                    // order all
                } else {
                    WholesaleOrders wo = new WholesaleOrders();
                    wo.setWholesalerProductsStock(wholesalerProductsStock);
                    WholesaleOrderStatus status = new WholesaleOrderStatus();
                    status.setWhStatusId(1);
                    wo.setWhOrdQnty(wholesalerProductsStock.getWhStockAvailableQnty());
                    wo.setWhOrdPrice(wo.getWholesalerProductsStock().getWhProdSellPrice() * wholesalerProductsStock.getWhStockAvailableQnty());
                    wo.setWhOrderDate(retailOrders.getRtOrderDate());
                    wo.setWholesaleOrderStatus(status);
                    wo.setRetailer(retailOrders.getRetailerProductsStock().getRetailer());
                    commonManager.addWholesaleOrder(wo);
                    pendingQuantity = forecastValue - wo.getWhOrdQnty();
                    //order available quantity
                    // pending quantity  = forecast- ordered quantity
                }


            }
            if (pendingQuantity == 0) {
                break;
            } else {
                forecastValue = pendingQuantity;
            }
            // if pending quantity == 0
            // break;
            // else
            // forecast value = pending value
        }
        if (pendingQuantity > 0) {

            WholesaleOrders wo = new WholesaleOrders();
            for (WholesalerProductsStock wps : wpsList) {
                wo.setWholesalerProductsStock(wps);
                break;
            }
            WholesaleOrderStatus status = new WholesaleOrderStatus();
            status.setWhStatusId(1);
            wo.setWhOrdQnty(forecastValue.intValue());
            wo.setWhOrdPrice(wo.getWholesalerProductsStock().getWhProdSellPrice() * forecastValue);
            wo.setWhOrderDate(retailOrders.getRtOrderDate());
            wo.setWholesaleOrderStatus(status);
            wo.setRetailer(retailOrders.getRetailerProductsStock().getRetailer());
            commonManager.addWholesaleOrder(wo);
        }

    }

    public Long forecastQuantityForAutomaticOrdering(RetailOrders retailOrders) {
        DataSet dataSet = new DataSet();
        Long productId = retailOrders.getRetailerProductsStock().getRetailProducts().getRtProdId();
        Long retailerId = retailOrders.getRetailerProductsStock().getRetailer().getRtId();
        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Calendar endDate = new GregorianCalendar();
        Calendar startDate = new GregorianCalendar();
        startDate.set(Calendar.MONTH, startDate.get(Calendar.MONTH) - 1);

        // fetch list of units and dates
        SortedMap<Date, Long> salesAndDateMap = commonManager.fetchAllRetailerSalesAndDate(retailerId, productId, sdf.format(startDate.getTime()), sdf.format(endDate.getTime()));
        int diffOfDates = daysBetween(startDate.getTime(), endDate.getTime());
        Calendar tempDate = new GregorianCalendar();
        tempDate.setTime(startDate.getTime());
        Long value = 0L;
        for (int i = 1; i <= diffOfDates; i++) {
            Iterator it = salesAndDateMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                Date d = (Date) e.getKey();
                if (sdf.format(tempDate.getTime()).equals(d.toString())) {
                    value = (Long) e.getValue();
                }
            }
            Observation obs = new Observation(value);
            obs.setIndependentValue("day", i);
            dataSet.add(obs);
            // increment day for temp date
            tempDate.set(Calendar.DAY_OF_MONTH, tempDate.get(Calendar.DAY_OF_MONTH) + 1);
        }
        // ForecastingModel model
        ForecastingModel model = Forecaster.getBestForecast(dataSet);
        model.init(dataSet);
        // obtain forecast for next 5 days
        DataSet fcDataSet = new DataSet();
        DataPoint fcDataPoint = new Observation(0.0);
        fcDataPoint.setIndependentValue("day", diffOfDates + 1);
        fcDataSet.add(fcDataPoint);
        //Forecasting model already created and initialized
        model.forecast(fcDataSet);

        // After calling model.forecast, our fcDataSet now contains forecast data points
        Long forecastValue = 0L;
        Iterator it = fcDataSet.iterator();
        while (it.hasNext()) {
            DataPoint dp = (DataPoint) it.next();
            forecastValue = Math.round(dp.getDependentValue());
        }
        return forecastValue;
    }

    /*
    Return the number of days between the two dates.
     */
    private int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
}

