/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.RetailProducts;
import som.domain.Retailer;
import som.domain.RetailerProductsStock;
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class RetailerProfitMarginsController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        RetailerProductsStock rps = (RetailerProductsStock) super.formBackingObject(request);
        rps.setRetailProducts(new RetailProducts());
        rps.setRetailer(new Retailer());
        request.setAttribute("retailProductList", commonManager.getRetailProductsList(""));
        request.setAttribute("retailersList", commonManager.getRetailersList());
        return rps;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, org.springframework.validation.BindException errors) throws Exception {
        RetailerProductsStock rps = (RetailerProductsStock) command;
        // Create a simple Bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<RetailerProductsStock> list = commonManager.fetchProfitMarginAnalysisData(rps.getRetailProducts().getRtProdId(), rps.getRetailer().getRtId());
        if (!list.isEmpty()) {
            JFreeChart chart;
            if (rps.getRetailProducts().getRtProdId() != null && rps.getRetailer().getRtId() == null) {
                for (RetailerProductsStock rp : list) {
                    dataset.setValue(rp.getRtProdMarginPercentage(), "Profit Margin (in %)", rp.getRetailer().getRtName());
                }
                String prodName = list.get(0).getRetailProducts().getRtProdName();
                chart = ChartFactory.createBarChart("Profit Margins for " + prodName,
                        "Retailers", "Profit Margin (in %)", dataset, PlotOrientation.VERTICAL,
                        false, true, false);


            } else if (rps.getRetailProducts().getRtProdId() == null && rps.getRetailer().getRtId() != null) {
                for (RetailerProductsStock rp : list) {
                    dataset.setValue(rp.getRtProdMarginPercentage(), "Profit Margin (in %)", rp.getRetailProducts().getRtProdId());
                }
                String suppName = list.get(0).getRetailer().getRtName();
                chart = ChartFactory.createBarChart("Profit Margins for " + suppName,
                        "Product ID", "Profit Margin (in %)", dataset, PlotOrientation.VERTICAL,
                        false, true, false);
            } else {
                for (RetailerProductsStock rp : list) {
                    dataset.setValue(rp.getRtProdMarginPercentage(), "Profit Margin (in %)", rp.getRetailer().getRtName());
                }
                String prodName = list.get(0).getRetailProducts().getRtProdName();
                String suppName = list.get(0).getRetailer().getRtName();
                chart = ChartFactory.createBarChart("Profit Margins for " + suppName + " on " + prodName,
                        "Retailers", "Profit Margin (in %)", dataset, PlotOrientation.VERTICAL,
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
                request.setAttribute("imageName", "ProfitMarginAnalysis.jpg");
                // System.out.println("*** Saving on: " + dirName);
                // ChartUtilities.saveChartAsJPEG(new File(dirName), chart, 800, 600);
                // for build
                String buildDir = realContextPath.substring(0, realContextPath.lastIndexOf("web")) + dirSubPart + "ProfitMarginAnalysis.jpg";
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

            System.out.println("########## Retailer Id: " + rps.getRetailer().getRtId());
            System.out.println("########## Retailer Prod id: " + rps.getRetailProducts().getRtProdId());
            System.out.println("########## Chart displayed successfull########");
        }
        return super.onSubmit(request, response, command, errors);
    }
}
