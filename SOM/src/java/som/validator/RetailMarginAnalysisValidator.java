/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import som.domain.RetailerProductsStock;

/**
 *
 * @author mithun
 */
public class RetailMarginAnalysisValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(RetailerProductsStock.class);
    }

    public void validate(Object command, Errors errors) {
        RetailerProductsStock rps = (RetailerProductsStock) command;
        if (null != rps) {
            checkRetailerIdAndProductId(rps, errors);
        }
    }

    public void checkRetailerIdAndProductId(RetailerProductsStock rps, Errors errors) {
        if (null != rps.getRetailProducts() && null != rps.getRetailer() && (rps.getRetailer().getRtId() == null && rps.getRetailProducts().getRtProdId() == null)) {
            errors.rejectValue("retailer.rtId", "error.retail.margin.analysis.required", null, "Either of Retailer name or Product Name is required.");
        }
    }
}
