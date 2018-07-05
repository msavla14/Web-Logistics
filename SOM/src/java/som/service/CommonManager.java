/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import som.domain.Consumer;
import som.domain.RetailAnalysisWrapper;
import som.domain.RetailOrders;
import som.domain.RetailProducts;
import som.domain.Retailer;
import som.domain.RetailerProductsStock;
import som.domain.WholesaleOrders;
import som.domain.WholesaleProducts;
import som.domain.WholesalerProductsStock;

/**
 *
 * @author mithun
 */
public interface CommonManager extends Serializable {

    public List<Retailer> getRetailersList();

    public List<Consumer> getConsumersList();

    public List<WholesaleProducts> getWholesaleProductsList(String productId);

    public List<RetailProducts> getRetailProductsList(String productId);

    public List<String> getWholesalerProductSubCategoryList();

    public List<WholesalerProductsStock> fetchWholesalerProductsStocksByProductId(String productId);

    public List<RetailerProductsStock> fetchRetailerProductsStocksByProductId(String productId);

    public List<WholesalerProductsStock> fetchWholesalerProductsStocksByProdIdAndWhId(String productId, String wholesalerId);

    public List<RetailerProductsStock> fetchRetailerProductsStocksByProdIdAndRtId(String productId, String retailerId);

    public void addWholesaleOrder(WholesaleOrders wholesaleOrders);

    public void addRetailOrder(RetailOrders retailOrders);

    //############# Analysis method Calls#############
    public List<RetailerProductsStock> fetchProfitMarginAnalysisData(Long productId, Long retailerId);

    public SortedMap<String, Long> fetchRetailSalesAnalysisData(RetailAnalysisWrapper raw);

    public SortedMap<Date, Long> fetchRetailerSalesAndDate(RetailAnalysisWrapper raw);

    public SortedMap<Date, Long> fetchRetailerOrderQntyAndDate(RetailAnalysisWrapper raw);

    public SortedMap<Date, Integer> fetchRetailerAvailableStockHistoryByRetailerIdAndProdId(RetailAnalysisWrapper raw);

    public List<WholesaleProducts> fetchWholesaleProductByRetailProductId(Long retailProductId);

    public SortedMap<Date, Long> fetchAllRetailerSalesAndDate(Long retailerId, Long productId, String startDate, String endDate);
}
