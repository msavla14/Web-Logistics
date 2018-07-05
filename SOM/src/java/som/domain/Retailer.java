package som.domain;
// Generated Aug 5, 2010 11:05:49 AM by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * Retailer generated by hbm2java
 */
public class Retailer  implements java.io.Serializable {


     private Long rtId;
     private String rtName;
     private String rtAddress;
     private String rtCity;
     private String rtContactNo;
     private String rtEmail;
     private Set<RetailerProductsStock> retailerProductsStocks = new HashSet<RetailerProductsStock>(0);
     private Set<WholesaleOrders> wholesaleOrderses = new HashSet<WholesaleOrders>(0);

    public Retailer() {
    }

    public Retailer(String rtName, String rtAddress, String rtCity, String rtContactNo, String rtEmail, Set<RetailerProductsStock> retailerProductsStocks, Set<WholesaleOrders> wholesaleOrderses) {
       this.rtName = rtName;
       this.rtAddress = rtAddress;
       this.rtCity = rtCity;
       this.rtContactNo = rtContactNo;
       this.rtEmail = rtEmail;
       this.retailerProductsStocks = retailerProductsStocks;
       this.wholesaleOrderses = wholesaleOrderses;
    }
   
    public Long getRtId() {
        return this.rtId;
    }
    
    public void setRtId(Long rtId) {
        this.rtId = rtId;
    }
    public String getRtName() {
        return this.rtName;
    }
    
    public void setRtName(String rtName) {
        this.rtName = rtName;
    }
    public String getRtAddress() {
        return this.rtAddress;
    }
    
    public void setRtAddress(String rtAddress) {
        this.rtAddress = rtAddress;
    }
    public String getRtCity() {
        return this.rtCity;
    }
    
    public void setRtCity(String rtCity) {
        this.rtCity = rtCity;
    }
    public String getRtContactNo() {
        return this.rtContactNo;
    }
    
    public void setRtContactNo(String rtContactNo) {
        this.rtContactNo = rtContactNo;
    }
    public String getRtEmail() {
        return this.rtEmail;
    }
    
    public void setRtEmail(String rtEmail) {
        this.rtEmail = rtEmail;
    }
    public Set<RetailerProductsStock> getRetailerProductsStocks() {
        return this.retailerProductsStocks;
    }
    
    public void setRetailerProductsStocks(Set<RetailerProductsStock> retailerProductsStocks) {
        this.retailerProductsStocks = retailerProductsStocks;
    }
    public Set<WholesaleOrders> getWholesaleOrderses() {
        return this.wholesaleOrderses;
    }
    
    public void setWholesaleOrderses(Set<WholesaleOrders> wholesaleOrderses) {
        this.wholesaleOrderses = wholesaleOrderses;
    }




}


