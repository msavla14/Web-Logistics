/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.RetailerProductsStock;
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class RetailerAnalysisController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, org.springframework.validation.BindException errors) throws Exception {
        // Create a simple Bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<RetailerProductsStock> list = commonManager.fetchRetailerProductsStocksByProductId("1");
        for (RetailerProductsStock rp : list) {
            dataset.setValue(rp.getRtProdMarginPercentage(), "Profit Margin (in %)", rp.getRetailer().getRtName());
        }
        JFreeChart chart = ChartFactory.createBarChart("Profit Margins for Suppliers",
                "Suppliers", "Profit Margin (in %)", dataset, PlotOrientation.VERTICAL,
                false, true, false);
        try {
            ServletContext context = request.getSession().getServletContext();
            String realContextPath = context.getRealPath(request.getContextPath());
            System.out.println("*** getRealPath: " + realContextPath);
            String dirSubPart = "web\\jspchart\\";
            String dirName = realContextPath.substring(0, realContextPath.lastIndexOf("build")) + dirSubPart + "SuppliersProfitMargins.jpg";
            request.setAttribute("imageName", "SuppliersProfitMargins.jpg");
            System.out.println("*** Saving on: " + dirName);
            ChartUtilities.saveChartAsJPEG(new File(dirName), chart, 800, 600);
            
            // ChartUtilities.saveChartAsPNG(new File("C:\\Users\\mithun\\Documents\\NetBeansProjects\\SOM\\web\\jspchart\\barchart.jpg"), chart, 800, 500);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        }

        System.out.println("########## Chart displayed successfull########");
        return new ModelAndView(getSuccessView());
    }
}
