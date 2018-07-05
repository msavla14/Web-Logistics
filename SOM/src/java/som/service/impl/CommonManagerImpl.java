/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.service.impl;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import som.dao.CommonDao;
import som.domain.Consumer;
import som.domain.RetailAnalysisWrapper;
import som.domain.RetailOrders;
import som.domain.RetailProducts;
import som.domain.Retailer;
import som.domain.RetailerProductsStock;
import som.domain.WholesaleOrders;
import som.domain.WholesaleProducts;
import som.domain.WholesalerProductsStock;
import som.service.CommonManager;

/**
 *
 * @author mithun
 */
public class CommonManagerImpl implements CommonManager {

    private CommonDao commonDao;

    public void setCommonDao(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    public List<Retailer> getRetailersList() {
        return commonDao.getRetailersList();
    }

    public List<Consumer> getConsumersList() {
        return commonDao.getConsumersList();
    }

    public List<WholesaleProducts> getWholesaleProductsList(String productId) {
        return commonDao.getWholesaleProductsList(productId);
    }

    public List<RetailProducts> getRetailProductsList(String productId) {
        return commonDao.getRetailProductsList(productId);
    }

    public List<String> getWholesalerProductSubCategoryList() {
        return commonDao.getWholesalerProductSubCategoryList();
    }

    public List<WholesalerProductsStock> fetchWholesalerProductsStocksByProductId(String productId) {
        return commonDao.fetchWholesalerProductsStocksByProductId(productId);
    }

    public List<RetailerProductsStock> fetchRetailerProductsStocksByProductId(String productId) {
        return commonDao.fetchRetailerProductsStocksByProductId(productId);
    }

    public List<WholesalerProductsStock> fetchWholesalerProductsStocksByProdIdAndWhId(String productId, String wholesalerId) {
        return commonDao.fetchWholesalerProductsStocksByProdIdAndWhId(productId, wholesalerId);
    }

    public List<RetailerProductsStock> fetchRetailerProductsStocksByProdIdAndRtId(String productId, String retailerId) {
        return commonDao.fetchRetailerProductsStocksByProdIdAndRtId(productId, retailerId);
    }

    public void addWholesaleOrder(WholesaleOrders wholesaleOrders) {
        commonDao.addWholesaleOrder(wholesaleOrders);
    }

    public void addRetailOrder(RetailOrders retailOrders) {
        commonDao.addRetailOrder(retailOrders);
    }

    //############# Analysis method Calls#############
    public List<RetailerProductsStock> fetchProfitMarginAnalysisData(Long productId, Long retailerId) {
        return commonDao.fetchProfitMarginAnalysisData(productId, retailerId);
    }

    public SortedMap<String, Long> fetchRetailSalesAnalysisData(RetailAnalysisWrapper raw) {
        return commonDao.fetchRetailSalesAnalysisData(raw);
    }

    public SortedMap<Date, Long> fetchRetailerSalesAndDate(RetailAnalysisWrapper raw) {
        return commonDao.fetchRetailerSalesAndDate(raw);
    }

    public SortedMap<Date, Long> fetchRetailerOrderQntyAndDate(RetailAnalysisWrapper raw) {
        return commonDao.fetchRetailerOrderQntyAndDate(raw);
    }

    public SortedMap<Date, Integer> fetchRetailerAvailableStockHistoryByRetailerIdAndProdId(RetailAnalysisWrapper raw) {
        return commonDao.fetchRetailerAvailableStockHistoryByRetailerIdAndProdId(raw);
    }

    public List<WholesaleProducts> fetchWholesaleProductByRetailProductId(Long retailProductId) {
        return commonDao.fetchWholesaleProductByRetailProductId(retailProductId);
    }

    public SortedMap<Date, Long> fetchAllRetailerSalesAndDate(Long retailerId, Long productId, String startDate, String endDate) {
        return commonDao.fetchAllRetailerSalesAndDate(retailerId, productId, startDate, endDate);
    }
}
