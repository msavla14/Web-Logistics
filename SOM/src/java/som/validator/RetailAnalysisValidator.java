/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import som.domain.RetailAnalysisWrapper;

/**
 *
 * @author mithun
 */
public class RetailAnalysisValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(RetailAnalysisWrapper.class);
    }

    public void validate(Object command, Errors errors) {
        RetailAnalysisWrapper raw = (RetailAnalysisWrapper) command;
        if (null != raw) {
            checkRetailerIdAndProductId(raw, errors);
            checkFromDate(raw, errors);
            checkToDate(raw, errors);
        }
    }

    public void checkRetailerIdAndProductId(RetailAnalysisWrapper raw, Errors errors) {
        System.out.println("Validator : " + raw.getSupplierId() + " " + raw.getProductId());
        if ((null == raw.getSupplierId() && raw.getProductId() == null)) {
            errors.rejectValue("supplierId", "error.retail.analysis.required", null, "Either of Retailer name or Product Name is required.");
            //errors.rejectValue("error.retail.analysis.required", "Please select the Consumer Id.");
        }
    }

    public void checkFromDate(RetailAnalysisWrapper raw, Errors errors) {
     if (null != raw.getFromD() && null != raw.getToD() && raw.getFromD().after(raw.getToD())) {
            errors.rejectValue("fromD", "error.retail.analysis.fromD.incorrect", null, "From Date entered must me smaller than To Date.");
        }

    }

    public void checkToDate(RetailAnalysisWrapper raw, Errors errors) {
       if (null != raw.getToD() && null != raw.getFromD() && raw.getToD().before(raw.getFromD())) {
            errors.rejectValue("fromD", "error.retail.analysis.toD.incorrect", null, "To Date entered must be greater than From Date.");
        }

    }
}
