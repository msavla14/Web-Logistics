package som.domain;
// Generated Aug 5, 2010 11:05:49 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * RetailOrders generated by hbm2java
 */
public class RetailOrders  implements java.io.Serializable {


     private Long rtOrdNo;
     private RetailerProductsStock retailerProductsStock;
     private Consumer consumer;
     private RetailOrderStatus retailOrderStatus;
     private Integer rtOrdQnty;
     private Double rtOrdPrice;
     private Date rtOrderDate;
     private Set<RetailDelivery> retailDeliveries = new HashSet<RetailDelivery>(0);

    public RetailOrders() {
    }

	
    public RetailOrders(RetailerProductsStock retailerProductsStock, Consumer consumer, RetailOrderStatus retailOrderStatus) {
        this.retailerProductsStock = retailerProductsStock;
        this.consumer = consumer;
        this.retailOrderStatus = retailOrderStatus;
    }
    public RetailOrders(RetailerProductsStock retailerProductsStock, Consumer consumer, RetailOrderStatus retailOrderStatus, Integer rtOrdQnty, Double rtOrdPrice, Date rtOrderDate, Set<RetailDelivery> retailDeliveries) {
       this.retailerProductsStock = retailerProductsStock;
       this.consumer = consumer;
       this.retailOrderStatus = retailOrderStatus;
       this.rtOrdQnty = rtOrdQnty;
       this.rtOrdPrice = rtOrdPrice;
       this.rtOrderDate = rtOrderDate;
       this.retailDeliveries = retailDeliveries;
    }
   
    public Long getRtOrdNo() {
        return this.rtOrdNo;
    }
    
    public void setRtOrdNo(Long rtOrdNo) {
        this.rtOrdNo = rtOrdNo;
    }
    public RetailerProductsStock getRetailerProductsStock() {
        return this.retailerProductsStock;
    }
    
    public void setRetailerProductsStock(RetailerProductsStock retailerProductsStock) {
        this.retailerProductsStock = retailerProductsStock;
    }
    public Consumer getConsumer() {
        return this.consumer;
    }
    
    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }
    public RetailOrderStatus getRetailOrderStatus() {
        return this.retailOrderStatus;
    }
    
    public void setRetailOrderStatus(RetailOrderStatus retailOrderStatus) {
        this.retailOrderStatus = retailOrderStatus;
    }
    public Integer getRtOrdQnty() {
        return this.rtOrdQnty;
    }
    
    public void setRtOrdQnty(Integer rtOrdQnty) {
        this.rtOrdQnty = rtOrdQnty;
    }
    public Double getRtOrdPrice() {
        return this.rtOrdPrice;
    }
    
    public void setRtOrdPrice(Double rtOrdPrice) {
        this.rtOrdPrice = rtOrdPrice;
    }
    public Date getRtOrderDate() {
        return this.rtOrderDate;
    }
    
    public void setRtOrderDate(Date rtOrderDate) {
        this.rtOrderDate = rtOrderDate;
    }
    public Set<RetailDelivery> getRetailDeliveries() {
        return this.retailDeliveries;
    }
    
    public void setRetailDeliveries(Set<RetailDelivery> retailDeliveries) {
        this.retailDeliveries = retailDeliveries;
    }




}


