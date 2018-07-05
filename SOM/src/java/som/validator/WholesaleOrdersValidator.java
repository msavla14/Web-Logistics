/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import som.domain.WholesaleOrders;

/**
 *
 * @author mithun
 */
public class WholesaleOrdersValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(WholesaleOrders.class);
    }

    public void validate(Object command, Errors errors) {
        WholesaleOrders wholesaleOrders = (WholesaleOrders) command;
        if (null != wholesaleOrders) {
            checkRetailerId(wholesaleOrders, errors);
            checkNoOfUnits(wholesaleOrders, errors);
        }
    }

    public void checkRetailerId(WholesaleOrders wholesaleOrders, Errors errors) {
        if (null != wholesaleOrders.getRetailer() && wholesaleOrders.getRetailer().getRtId() == 0) {
            errors.rejectValue("retailer.rtId", "error.retail.orders.required.rtId", null, "Cosumer name is required.");
        }
    }

    public void checkNoOfUnits(WholesaleOrders wholesaleOrders, Errors errors) {
        if (null == wholesaleOrders.getWhOrdQnty() || wholesaleOrders.getWhOrdQnty() == 0) {
            errors.rejectValue("whOrdQnty", "error.retail.orders.required.noOfUnits", null, "No of Units field is required.");
        } else if (!wholesaleOrders.getWhOrdQnty().toString().matches("^[0-9]+$")) {
            errors.rejectValue("whOrdQnty", "error.retail.orders.invalid.noOfUnits", null, "No of Units entered is Invalid.");
        } else if (wholesaleOrders.getWhOrdQnty() > 999) {
            errors.rejectValue("whOrdQnty", "error.retail.orders.limit.noOfUnits", null, "No of Units exceeds the Limit.");
        }

    }
}
