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
import som.domain.WholesaleProducts;
import som.domain.WholesalerProductsStock;
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class WholesaleProductSuppliersController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        String productId = request.getParameter("productId");
        Map map = new HashMap();
        List<WholesaleProducts> list = commonManager.getWholesaleProductsList(productId);
        map.put("wholesaleProductList", list);
        List<WholesalerProductsStock> wholesalerProductsStocksList = commonManager.fetchWholesalerProductsStocksByProductId(productId);
        map.put("wholesalerProductsStocksList", wholesalerProductsStocksList);
        return map;
    }

    @Override
    protected void doSubmitAction(Object command) throws Exception {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
