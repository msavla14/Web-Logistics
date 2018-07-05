/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.RetailProducts;
import som.domain.RetailerProductsStock;
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class RetailProductSuppliersController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        String productId = request.getParameter("productId");
        Map map = new HashMap();
        List<RetailProducts> list = commonManager.getRetailProductsList(productId);
        map.put("retailProductList", list);
        List<RetailerProductsStock> retailProductsStocksList = commonManager.fetchRetailerProductsStocksByProductId(productId);
        map.put("retailProductsStocksList", retailProductsStocksList);
        return map;
    }
}
