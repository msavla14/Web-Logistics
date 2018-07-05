package som.domain;
// Generated Aug 5, 2010 11:05:49 AM by Hibernate Tools 3.2.1.GA



/**
 * WholesalerProductsStockId generated by hbm2java
 */
public class WholesalerProductsStockId  implements java.io.Serializable {


     private long whId;
     private long whProdId;

    public WholesalerProductsStockId() {
    }

    public WholesalerProductsStockId(long whId, long whProdId) {
       this.whId = whId;
       this.whProdId = whProdId;
    }
   
    public long getWhId() {
        return this.whId;
    }
    
    public void setWhId(long whId) {
        this.whId = whId;
    }
    public long getWhProdId() {
        return this.whProdId;
    }
    
    public void setWhProdId(long whProdId) {
        this.whProdId = whProdId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof WholesalerProductsStockId) ) return false;
		 WholesalerProductsStockId castOther = ( WholesalerProductsStockId ) other; 
         
		 return (this.getWhId()==castOther.getWhId())
 && (this.getWhProdId()==castOther.getWhProdId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getWhId();
         result = 37 * result + (int) this.getWhProdId();
         return result;
   }   


}


