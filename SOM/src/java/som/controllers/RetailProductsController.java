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
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class RetailProductsController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap();
        List<RetailProducts> list = commonManager.getRetailProductsList("");
        map.put("retailProductList", list);
        map.put("retailProductListSize", list.size());
        return map;
    }
}
