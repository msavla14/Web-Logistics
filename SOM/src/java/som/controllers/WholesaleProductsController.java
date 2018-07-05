/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.net.BindException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.WholesaleProducts;
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class WholesaleProductsController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap();
        List<WholesaleProducts> list = commonManager.getWholesaleProductsList("");
        map.put("wholesaleProductList", list);
        map.put("wholesaleProductListSize", list.size());
        List<String> subCatgList = commonManager.getWholesalerProductSubCategoryList();
        map.put("subCatgList", subCatgList);
        return map;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return super.handleRequest(request, response);
    }
}
