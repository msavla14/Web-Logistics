/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.RetailAnalysisWrapper;
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class RetailSalesAnalysisController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        RetailAnalysisWrapper rap = (RetailAnalysisWrapper) super.formBackingObject(request);
        request.setAttribute("retailProductList", commonManager.getRetailProductsList(""));
        request.setAttribute("retailersList", commonManager.getRetailersList());
        return rap;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, null, new CustomDateEditor(dateFormat, false));

        //super.initBinder(request, binder);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        RetailAnalysisWrapper raw = (RetailAnalysisWrapper) command;
        // Create a simple Bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        raw.setFromDate(dateFormat.format(raw.getFromD()));
        raw.setToDate(dateFormat.format(raw.getToD()));
        SortedMap<String, Long> map = commonManager.fetchRetailSalesAnalysisData(raw);
        int a = raw.getFromD().getYear();
        int c = raw.getFromD().getYear() + 1900;
        System.out.println("year :" + c);
        System.out.println("month :" + raw.getFromD().getMonth());
        System.out.println("day :" + raw.getFromD().getDate());
        System.out.println("time :" + raw.getFromD());
        if (!map.isEmpty()) {
            JFreeChart chart;
            if (raw.getSupplierId() != null && raw.getProductId() == null) {
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();
                    //  System.out.println("###########Map values#######:" + e.getKey() + " " + e.getValue());
                    dataset.setValue((Number) e.getValue(), "No. of Units Sold", (Comparable) e.getKey());

                }
                //String prodName = list.get(0).getRetailProducts().getRtProdName();
                chart = ChartFactory.createBarChart("Sales of Products for Retailer Id" + raw.getSupplierId(),
                        "Product ID", "No. of Units Sold", dataset, PlotOrientation.VERTICAL,
                        false, true, false);
            } else if (raw.getSupplierId() == null && raw.getProductId() != null) {
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();
                    //   System.out.println("###########Map values#######:" + e.getKey() + " " + e.getValue());
                    dataset.setValue((Number) e.getValue(), "No. of Units Sold", (Comparable) e.getKey());

                }
                //String prodName = list.get(0).getRetailProducts().getRtProdName();
                chart = ChartFactory.createBarChart("Sales of Product for  Retailers",
                        "Retailer ID", "No. of Units Sold", dataset, PlotOrientation.VERTICAL,
                        false, true, false);
            } else {
                Iterator it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();
                    // System.out.println("###########Map values#######:" + e.getKey() + " " + e.getValue());
                    dataset.setValue((Number) e.getValue(), "No. of Units Sold", (Comparable) e.getKey());

                }
                //String prodName = list.get(0).getRetailProducts().getRtProdName();
                chart = ChartFactory.createBarChart("Sales of Retailer ID " + raw.getSupplierId() + " on Product ID " + raw.getProductId(),
                        "Product ID", "No. of Units Sold", dataset, PlotOrientation.VERTICAL,
                        false, true, false);
            }
            try {
                CategoryItemRenderer renderer = new BarRenderer();
                DecimalFormat decimalformat = new DecimalFormat("##,###");
                renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat));
                renderer.setItemLabelsVisible(true);
                renderer.setBaseItemLabelsVisible(true);
                chart.getCategoryPlot().setRenderer(renderer);

                ServletContext context = request.getSession().getServletContext();
                String realContextPath = context.getRealPath(request.getContextPath());
                System.out.println("*** getRealPath: " + realContextPath);
                String dirSubPart = "web\\jspchart\\";
                //String dirName = realContextPath.substring(0, realContextPath.lastIndexOf("build")) + dirSubPart + "RetailSalesAnalysis.jpg";
                request.setAttribute("imageName", "RetailSalesAnalysis.jpg");
                // System.out.println("*** Saving on: " + dirName);
                // ChartUtilities.saveChartAsJPEG(new File(dirName), chart, 800, 600);
                // for build
                String buildDir = realContextPath.substring(0, realContextPath.lastIndexOf("web")) + dirSubPart + "RetailSalesAnalysis.jpg";
                File file = new File(buildDir);
                if (file != null && file.exists()) {
                    file.delete();
                }
                //file = new File(buildDir);
                ChartUtilities.saveChartAsJPEG(file, chart, 800, 600);
                System.out.println("*** Saving on build: " + buildDir);
                // ChartUtilities.saveChartAsPNG(new File("C:\\Users\\mithun\\Documents\\NetBeansProjects\\SOM\\web\\jspchart\\barchart.jpg"), chart, 800, 500);
                } catch (IOException e) {
                System.err.println("Problem occurred creating chart.");
            }
            System.out.println("########## Chart displayed successfull########");
        }
        return super.onSubmit(request, response, command, errors);
    }
}
