/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.domain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author mithun
 */
public class RetailAnalysisWrapper {

    Long supplierId;
    Long productId;
    String fromDate;
    String toDate;
    int noOfDays;
    Calendar calFromDate;
    Calendar calToDate;
    String DATE_FORMAT = "yyyy-MM-dd";
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    Date fromD;
    Date toD;

    public Date getFromD() {
        return fromD;
    }

    public void setFromD(Date fromD) {
        this.fromD = fromD;
    }

    public Date getToD() {
        return toD;
    }

    public void setToD(Date toD) {
        this.toD = toD;
    }

    public Calendar getCalFromDate() {
        return calFromDate;
    }

    public void setCalFromDate(Calendar calFromDate) {
        this.calFromDate = calFromDate;
    }

    public Calendar getCalToDate() {
        return calToDate;
    }

    public void setCalToDate(Calendar calToDate) {
        this.calToDate = calToDate;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
}
