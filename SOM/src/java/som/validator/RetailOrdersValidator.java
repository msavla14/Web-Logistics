/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import som.domain.RetailOrders;

/**
 *
 * @author mithun
 */
public class RetailOrdersValidator implements Validator {

    public boolean supports(Class clazz) {
        return clazz.equals(RetailOrders.class);
    }

    public void validate(Object command, Errors errors) {
        RetailOrders retailOrders = (RetailOrders) command;
        if (null != retailOrders) {
            checkConsumerId(retailOrders, errors);
            checkNoOfUnits(retailOrders, errors);
        }


    }

    public void checkConsumerId(RetailOrders retailOrders, Errors errors) {
        if (null != retailOrders.getConsumer() && retailOrders.getConsumer().getConsId() == 0) {
            errors.rejectValue("consumer.consId", "error.retail.orders.required.consumerId", null, "Cosumer name is required.");
        }
    }

    public void checkNoOfUnits(RetailOrders retailOrders, Errors errors) {
        if (null == retailOrders.getRtOrdQnty() || retailOrders.getRtOrdQnty() == 0) {
            errors.rejectValue("rtOrdQnty", "error.retail.orders.required.noOfUnits", null, "No of Units field is required.");
        } else if (!retailOrders.getRtOrdQnty().toString().matches("^[0-9]+$")) {
            errors.rejectValue("rtOrdQnty", "error.retail.orders.invalid.noOfUnits", null, "No of Units entered is Invalid.");
        } else if (retailOrders.getRtOrdQnty() > 999) {
            errors.rejectValue("rtOrdQnty", "error.retail.orders.limit.noOfUnits", null, "No of Units exceeds the Limit.");
        }

    }
}
