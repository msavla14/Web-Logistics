/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.Retailer;
import som.domain.WholesaleOrderStatus;
import som.service.CommonManager;
import som.domain.WholesaleOrders;
import som.domain.WholesaleProducts;
import som.domain.Wholesaler;
import som.domain.WholesalerProductsStock;

/** 
 *
 * @author mithun
 */
public class PlaceWholesaleOrdersController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        WholesaleOrders wo = (WholesaleOrders) super.formBackingObject(request);
        String productId = request.getParameter("productId");
        String wholesalerId = request.getParameter("whId");
        wo.setRetailer(new Retailer());
        WholesalerProductsStock wps = new WholesalerProductsStock();
        wps.setWholesaler(new Wholesaler());
        wps.setWholesaleProducts(new WholesaleProducts());
        wo.setWholesalerProductsStock(wps);
        if (null != productId && null != wholesalerId && !productId.isEmpty() && !wholesalerId.isEmpty()) {
            List<WholesalerProductsStock> list = commonManager.fetchWholesalerProductsStocksByProdIdAndWhId(productId, wholesalerId);
            for (WholesalerProductsStock wpsObj : list) {
                wo.setWholesalerProductsStock(wpsObj);
            }
            request.setAttribute("retailerList", commonManager.getRetailersList());
        }
        return wo;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        if (null != command && command.getClass().equals(WholesaleOrders.class)) {
            WholesaleOrders wholesaleOrders = (WholesaleOrders) command;
            String whId = wholesaleOrders.getWholesalerProductsStock().getWholesaler().getWhId().toString();
            String whProdId = wholesaleOrders.getWholesalerProductsStock().getWholesaleProducts().getWhProdId().toString();
            List<WholesalerProductsStock> list = commonManager.fetchWholesalerProductsStocksByProdIdAndWhId(whProdId, whId);
            for (WholesalerProductsStock wps : list) {
                wholesaleOrders.setWholesalerProductsStock(wps);
            }
            WholesaleOrderStatus status = new WholesaleOrderStatus();
            status.setWhStatusId(1);
            wholesaleOrders.setWhOrdPrice(wholesaleOrders.getWholesalerProductsStock().getWhProdSellPrice() * wholesaleOrders.getWhOrdQnty());
            wholesaleOrders.setWhOrderDate(new Date());
            wholesaleOrders.setWholesaleOrderStatus(status);
            System.out.println(wholesaleOrders.toString());
            commonManager.addWholesaleOrder(wholesaleOrders);
            return new ModelAndView(getSuccessView());
        } else {
            throw new Exception("Invalid Command Class");
        }

    }
}
