/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.management.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import som.dao.CommonDao;
import som.domain.Consumer;
import som.domain.RetailAnalysisWrapper;
import som.domain.RetailOrders;
import som.domain.RetailProducts;
import som.domain.Retailer;
import som.domain.RetailerAvailableStockHistory;
import som.domain.RetailerProductsStock;
import som.domain.WholesaleOrders;
import som.domain.WholesaleProducts;
import som.domain.WholesalerProductsStock;

/**
 *
 * @author mithun
 */
public class CommonDaoImpl extends HibernateDaoSupport implements CommonDao {

    public List<Retailer> getRetailersList() {
        List<Retailer> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("from Retailer"));
        return list;
    }

    public List<Consumer> getConsumersList() {
        List<Consumer> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("from Consumer"));
        return list;
    }

    public List<WholesaleProducts> getWholesaleProductsList(String productId) {
        List<WholesaleProducts> list = new ArrayList();
        if (null != productId && !productId.isEmpty()) {
            list.addAll(getHibernateTemplate().find("from WholesaleProducts wp where wp.whProdId=" + productId));
        } else {
            list.addAll(getHibernateTemplate().find("from WholesaleProducts"));
        }
        return list;
    }

    public List<RetailProducts> getRetailProductsList(String productId) {
        List<RetailProducts> list = new ArrayList();
        if (null != productId && !productId.isEmpty()) {
            list.addAll(getHibernateTemplate().find("from RetailProducts rp where rp.rtProdId=" + productId + "order by rp.rtProdId"));
        } else {
            list.addAll(getHibernateTemplate().find("from RetailProducts"));
        }


        return list;
    }

    public List<String> getWholesalerProductSubCategoryList() {
        List<String> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("Select distinct whSubCatg from WholesaleProducts"));
        return list;
    }

    public List<WholesalerProductsStock> fetchWholesalerProductsStocksByProductId(String productId) {
        List<WholesalerProductsStock> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("from WholesalerProductsStock whprodst where whprodst.wholesaleProducts.whProdId =" + productId + "order by whprodst.whProdSellPrice, whprodst.whMaxDeliveryDays"));
        return list;
    }

    public List<RetailerProductsStock> fetchRetailerProductsStocksByProductId(String productId) {
        List<RetailerProductsStock> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("from RetailerProductsStock rtprodst where rtprodst.retailProducts.rtProdId =" + productId + "order by rtprodst.rtProdSellPrice, rtprodst.rtMaxDeliveryDays"));
        return list;
    }

    public List<WholesalerProductsStock> fetchWholesalerProductsStocksByProdIdAndWhId(String productId, String wholesalerId) {
        List<WholesalerProductsStock> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("from WholesalerProductsStock whprodst where whprodst.wholesaleProducts.whProdId =" + productId + "AND whprodst.wholesaler.whId=" + wholesalerId));
        return list;
    }

    public List<RetailerProductsStock> fetchRetailerProductsStocksByProdIdAndRtId(String productId, String retailerId) {
        List<RetailerProductsStock> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("from RetailerProductsStock rtprodst where rtprodst.retailProducts.rtProdId =" + productId + "AND rtprodst.retailer.rtId=" + retailerId));
        return list;
    }

    public void addWholesaleOrder(WholesaleOrders wholesaleOrders) {
        getHibernateTemplate().save(wholesaleOrders);
        List<WholesalerProductsStock> list = fetchWholesalerProductsStocksByProdIdAndWhId(wholesaleOrders.getWholesalerProductsStock().getWholesaleProducts().getWhProdId().toString(), wholesaleOrders.getWholesalerProductsStock().getWholesaler().getWhId().toString());
        int avQnty = 0;
        Long wholesalerProdId = 0L;
        Long wholesalerId = 0L;
        Long retailerProdId = 0L;
        Integer orderQnty = wholesaleOrders.getWhOrdQnty();
        for (WholesalerProductsStock wps : list) {
            avQnty = wps.getWhStockAvailableQnty();
            int whProdQntySold = wps.getWhProdQntySold();
            wholesalerProdId = wps.getWholesaleProducts().getWhProdId();
            wholesalerId = wps.getWholesaler().getWhId();
            retailerProdId = wps.getWholesaleProducts().getRtProdId();


            wps.setWhStockAvailableQnty(avQnty - orderQnty);
            wps.setWhProdQntySold(whProdQntySold + orderQnty);

            getHibernateTemplate().update(wps);

            System.out.println("##### WholesalerProductsStock Quantity Decreased to:" + wps.getWhStockAvailableQnty());
            System.out.println("##### Wholesaler total ordered quantity Increased to:" + wps.getWhProdQntySold());

        }
        // decrease the quantity of dependent product if any
        List<Long> dependentProdIdList = fetchDependentWholesaleProductId(wholesalerProdId);
        for (Long dpl : dependentProdIdList) {
            List<WholesalerProductsStock> wpsDependent = fetchWholesalerProductsStocksByProdIdAndWhId(dpl.toString(), wholesalerId.toString());
            for (WholesalerProductsStock wpsd : wpsDependent) {
                wpsd.setWhStockAvailableQnty(wpsd.getWhStockAvailableQnty() - orderQnty);
                wpsd.setWhProdQntySold(wpsd.getWhProdQntySold() + orderQnty);
                getHibernateTemplate().update(wpsd);
                System.out.println("##### Dependent WholesalerProductsStock Quantity Decreased to:" + wpsd.getWhStockAvailableQnty());
                System.out.println("##### Dependent WholesalerProductsStock total ordered quantity Increased to:" + wpsd.getWhProdQntySold());
            }
        }

        // update the available quantity to retail products table
        List<RetailerProductsStock> retailerProductStockList = fetchRetailerProductsStocksByProdIdAndRtId(retailerProdId.toString(), wholesaleOrders.getRetailer().getRtId().toString());
        for (RetailerProductsStock rps : retailerProductStockList) {
            rps.setRtStockAvailableQnty(rps.getRtStockAvailableQnty() + orderQnty);
            getHibernateTemplate().update(rps);
            System.out.println("##### Retailer available Quantity Increased to:" + rps.getRtStockAvailableQnty());
            createRetailerAvailableStockHistory(rps.getRetailer().getRtId(), rps.getRetailProducts().getRtProdId(), rps.getRtStockAvailableQnty(), wholesaleOrders.getWhOrderDate());
        }
    }

    public List<Long> fetchDependentWholesaleProductId(Long productId) {
        List<Long> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("select wp.whDependProdId from WholesaleProducts wp where wp.whProdId =" + productId));
        return list;
    }

    public List<Long> fetchDependentRetailProductId(Long productId) {
        List<Long> list = new ArrayList();
        list.addAll(getHibernateTemplate().find("select rpd.rtDependProdId from RetailProducts rpd where rpd.rtProdId =" + productId));
        return list;
    }

    public void addRetailOrder(RetailOrders retailOrders) {
        getHibernateTemplate().save(retailOrders);
        List<RetailerProductsStock> list = fetchRetailerProductsStocksByProdIdAndRtId(retailOrders.getRetailerProductsStock().getRetailProducts().getRtProdId().toString(), retailOrders.getRetailerProductsStock().getRetailer().getRtId().toString());
        Long retailProdId = 0L;
        Long retailerId = 0L;
        for (RetailerProductsStock rps : list) {
            int avQnty = rps.getRtStockAvailableQnty();
            int rtProdQntySold = rps.getRtProdQntySold();
            retailProdId = rps.getRetailProducts().getRtProdId();
            retailerId = rps.getRetailer().getRtId();

            rps.setRtStockAvailableQnty(avQnty - retailOrders.getRtOrdQnty());
            rps.setRtProdQntySold(rtProdQntySold + retailOrders.getRtOrdQnty());
            getHibernateTemplate().update(rps);
            System.out.println("##### RetailerProductsStock available Quantity Decreased to:" + rps.getRtStockAvailableQnty());
            System.out.println("##### RetailerProductsStock total ordered quantity Increased to:" + rps.getRtProdQntySold());
            createRetailerAvailableStockHistory(retailerId, retailProdId, rps.getRtStockAvailableQnty(), retailOrders.getRtOrderDate());

        }
        // decrease the quantity of dependent product if any
        List<Long> dependentProdIdList = fetchDependentRetailProductId(retailProdId);
        for (Long dpl : dependentProdIdList) {
            List<RetailerProductsStock> rpsDependent = fetchRetailerProductsStocksByProdIdAndRtId(dpl.toString(), retailerId.toString());
            for (RetailerProductsStock rpsd : rpsDependent) {
                rpsd.setRtStockAvailableQnty(rpsd.getRtStockAvailableQnty() - retailOrders.getRtOrdQnty());
                rpsd.setRtProdQntySold(rpsd.getRtProdQntySold() + retailOrders.getRtOrdQnty());
                getHibernateTemplate().update(rpsd);
                System.out.println("##### Dependent RetailerProductsStock Quantity Decreased to:" + rpsd.getRtStockAvailableQnty());
                System.out.println("##### Dependent RetailerProductsStock total ordered quantity Increased to:" + rpsd.getRtProdQntySold());
                createRetailerAvailableStockHistory(retailerId, rpsd.getRetailProducts().getRtProdId(), rpsd.getRtStockAvailableQnty(), retailOrders.getRtOrderDate());

            }
        }
    }

    public void createRetailerAvailableStockHistory(Long retailerId, Long productId, int availableStockQnty, Date date) {
        RetailerAvailableStockHistory rash = new RetailerAvailableStockHistory();
        rash.setRtId(retailerId);
        rash.setRtProdId(productId);
        rash.setRtStockAvailableQnty(availableStockQnty);
        rash.setRtHistoryDate(date);
        getHibernateTemplate().save(rash);
    }
    //############# Analysis method Calls#############

    public List<RetailerProductsStock> fetchProfitMarginAnalysisData(Long productId, Long retailerId) {
        List<RetailerProductsStock> list = new ArrayList();
        if (productId == null && null != retailerId) {
            System.out.println("########  fetchProfitMarginAnalysisData called with retailer id####");
            list.addAll(getHibernateTemplate().find("from RetailerProductsStock rtprodst where rtprodst.retailer.rtId=" + retailerId));
        } else if (productId != null && retailerId == null) {
            System.out.println("########  fetchProfitMarginAnalysisData called with product id####");
            list.addAll(getHibernateTemplate().find("from RetailerProductsStock rtprodst where rtprodst.retailProducts.rtProdId =" + productId));
        } else {
            System.out.println("########  fetchProfitMarginAnalysisData called with retailer id and product id ####");
            list.addAll(getHibernateTemplate().find("from RetailerProductsStock rtprodst where rtprodst.retailProducts.rtProdId =" + productId + "AND rtprodst.retailer.rtId=" + retailerId));
        }
        return list;
    }

    public SortedMap<String, Long> fetchRetailSalesAnalysisData(RetailAnalysisWrapper raw) {
        SortedMap<String, Long> map = new TreeMap<String, Long>();
        System.out.println("###### date format was : " + raw.getFromDate() + " " + raw.getToDate());
        if (raw.getSupplierId() != null && raw.getProductId() == null) {
            List<RetailProducts> retailProductList = getRetailProductsList("");
            for (RetailProducts rp : retailProductList) {
                List<Long> list = new ArrayList();
                list.addAll(getHibernateTemplate().find("SELECT SUM(ro.rtOrdQnty) FROM RetailOrders ro where ro.retailerProductsStock.retailer.rtId = " + raw.getSupplierId() + " And ro.retailerProductsStock.retailProducts.rtProdId = " + rp.getRtProdId() + " AND ro.rtOrderDate BETWEEN '" + raw.getFromDate() + "' AND '" + raw.getToDate() + "'"));
                for (Long total : list) {
                    map.put(rp.getRtProdId().toString(), total);
                }
            }
        } else if (raw.getSupplierId() == null && raw.getProductId() != null) {
            List<Retailer> retailerList = getRetailersList();
            for (Retailer retailer : retailerList) {
                List<Long> list = new ArrayList();
                list.addAll(getHibernateTemplate().find("SELECT SUM(ro.rtOrdQnty) FROM RetailOrders ro where ro.retailerProductsStock.retailer.rtId = " + retailer.getRtId() + " And ro.retailerProductsStock.retailProducts.rtProdId = " + raw.getProductId() + " AND ro.rtOrderDate BETWEEN '" + raw.getFromDate() + "' AND '" + raw.getToDate() + "'"));
                for (Long total : list) {
                    map.put(retailer.getRtId().toString(), total);
                }
            }
        } else {
            List<Long> list = new ArrayList();
            list.addAll(getHibernateTemplate().find("SELECT SUM(ro.rtOrdQnty) FROM RetailOrders ro where ro.retailerProductsStock.retailer.rtId = " + raw.getSupplierId() + " And ro.retailerProductsStock.retailProducts.rtProdId = " + raw.getProductId() + " AND ro.rtOrderDate BETWEEN '" + raw.getFromDate() + "' AND '" + raw.getToDate() + "'"));
            for (Long count : list) {
                map.put(raw.getProductId().toString(), count);
            }
        }
        return map;
    }

    public SortedMap<Date, Long> fetchRetailerSalesAndDate(RetailAnalysisWrapper raw) {
        SortedMap<Date, Long> map = new TreeMap<Date, Long>();
        List<Date> datesList = getHibernateTemplate().find("Select distinct ro.rtOrderDate from RetailOrders ro where ro.retailerProductsStock.retailer.rtId = " + raw.getSupplierId() + " And ro.retailerProductsStock.retailProducts.rtProdId = " + raw.getProductId());
        for (Date uniDate : datesList) {
            List<Long> noOfUnitsSold = getHibernateTemplate().find("select SUM(ro.rtOrdQnty) from RetailOrders ro where ro.retailerProductsStock.retailer.rtId = " + raw.getSupplierId() + " And ro.retailerProductsStock.retailProducts.rtProdId = " + raw.getProductId() + " AND ro.rtOrderDate = '" + uniDate + "'");
            for (Long qnty : noOfUnitsSold) {
                map.put(uniDate, qnty);
            }
        }
        return map;
    }

    public SortedMap<Date, Long> fetchRetailerOrderQntyAndDate(RetailAnalysisWrapper raw) {
        SortedMap<Date, Long> map = new TreeMap<Date, Long>();


        List orderDateList = getHibernateTemplate().find("Select Distinct wo.whOrderDate From WholesaleOrders wo where wo.retailer.rtId = " + raw.getSupplierId() + " AND wo.wholesalerProductsStock.wholesaleProducts.whProdId = " + raw.getProductId());

        if (null != orderDateList && !orderDateList.isEmpty()) {
            for (Object date : orderDateList) {
                List orderQntyList = getHibernateTemplate().find("Select SUM(wo.whOrdQnty)From WholesaleOrders wo where wo.retailer.rtId = " + raw.getSupplierId() + " AND wo.wholesalerProductsStock.wholesaleProducts.whProdId = " + raw.getProductId() + " And wo.whOrderDate = '" + date + "'");
                if (orderDateList != null && !orderDateList.isEmpty()) {
                    for (Object orderQnty : orderQntyList) {
                        map.put((Date) date, (Long) orderQnty);
                    }
                }
            }

        }
        return map;
    }

    public SortedMap<Date, Integer> fetchRetailerAvailableStockHistoryByRetailerIdAndProdId(RetailAnalysisWrapper raw) {
        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        SortedMap<Date, Integer> map = new TreeMap<Date, Integer>();
        List historyDateList = getHibernateTemplate().find("Select Distinct rtHistoryDate From RetailerAvailableStockHistory where rtId=" + raw.getSupplierId() + " And rtProdId = " + raw.getProductId() + " And rtHistoryDate between'" + sdf.format(raw.getFromD()) + "' AND '" + sdf.format(raw.getToD()) + "'");
        if (null != historyDateList && !historyDateList.isEmpty()) {
            for (Object date : historyDateList) {
                List avQntyHistoryList =
                        getHibernateTemplate().find("select rtStockAvailableQnty From RetailerAvailableStockHistory where rtId=" + raw.getSupplierId() + " And rtProdId = " + raw.getProductId() + " And rtHistoryDate= '" + date + "' order by rtAvStockHistoryId DESC");


                if (avQntyHistoryList != null && !avQntyHistoryList.isEmpty()) {
                    for (Object avQntyHistory : avQntyHistoryList) {
                        map.put((Date) date, (Integer) avQntyHistory);
                        break; // only get the highest id recored
                    }
                }
            }
        }
        return map;
    }

    public List<WholesaleProducts> fetchWholesaleProductByRetailProductId(Long retailProductId) {
        List<WholesaleProducts> wpList = new ArrayList<WholesaleProducts>();
        wpList.addAll(getHibernateTemplate().find(" from WholesaleProducts where rtProdId =" + retailProductId));
        return wpList;
    }

    public SortedMap<Date, Long> fetchAllRetailerSalesAndDate(Long retailerId, Long productId, String startDate, String endDate) {
        SortedMap<Date, Long> map = new TreeMap<Date, Long>();
        List<Date> datesList = getHibernateTemplate().find("Select distinct ro.rtOrderDate from RetailOrders ro where ro.retailerProductsStock.retailer.rtId = " + retailerId + " And ro.retailerProductsStock.retailProducts.rtProdId = " + productId + " AND ro.rtOrderDate between '" + startDate + "' And '" + endDate + "'");
        for (Date uniDate : datesList) {
            List<Long> noOfUnitsSold = getHibernateTemplate().find("select SUM(ro.rtOrdQnty) from RetailOrders ro where ro.retailerProductsStock.retailer.rtId = " + retailerId + " And ro.retailerProductsStock.retailProducts.rtProdId = " + productId + " AND ro.rtOrderDate = '" + uniDate + "'");
            for (Long qnty : noOfUnitsSold) {
                map.put(uniDate, qnty);
            }
        }
        return map;
    }
}
