/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
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
import org.springframework.validation.Errors;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.WholesalerProductsStock;
import som.service.CommonManager;

/** 
 *
 * @author mithun
 */
public class WholesalerAnalysisController extends SimpleFormController {

    private CommonManager commonManager;

    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    @Override
    protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
//        final double[][] data = new double[][]{
//            {210, 300, 320, 265, 299},
//            {200, 304, 201, 201, 340}
//        };
//
//        final CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
//                "Team ", "", data);
//
//        JFreeChart chart = null;
//        BarRenderer renderer = null;
//        CategoryPlot plot = null;
//
//
//        final CategoryAxis categoryAxis = new CategoryAxis("Match");
//        final ValueAxis valueAxis = new NumberAxis("Run");
//        renderer = new BarRenderer();
//
//        plot = new CategoryPlot(dataset, categoryAxis, valueAxis,
//                renderer);
//
//        plot.setOrientation(PlotOrientation.VERTICAL);
//        chart = new JFreeChart("Score Board", JFreeChart.DEFAULT_TITLE_FONT,
//                plot, true);
//
//        chart.setBackgroundPaint(new Color(249, 231, 236));
//
//        Paint p1 = new GradientPaint(
//                0.0f, 0.0f, new Color(16, 89, 172), 0.0f, 0.0f, new Color(201, 201, 244));
//
//        renderer.setSeriesPaint(1, p1);
//
//        Paint p2 = new GradientPaint(
//                0.0f, 0.0f, new Color(255, 35, 35), 0.0f, 0.0f, new Color(255, 180, 180));
//
//        renderer.setSeriesPaint(2, p2);
//
//        plot.setRenderer(renderer);
//
//        try {
//            final ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
////            final File file1 = new File("barchart.png");
////            System.out.println("############ Absolute Path: "+ file1.getAbsolutePath());
////            System.out.println("############ parent: "+ file1.getParent());
//
//            ServletContext context = request.getSession().getServletContext();
//            String realContextPath = context.getRealPath(request.getContextPath());
//            System.out.println("*** getRealPath: " + realContextPath);
//            String dirName = realContextPath.substring(0, realContextPath.lastIndexOf("SOM")) + "../jspchart\\";
//
//            System.out.println("*** Saving on: " + dirName);
//
//            File file1 = new File("C:\\Users\\mithun\\Documents\\NetBeansProjects\\SOM\\web\\jspchart\\barchart.png");
//
//
//            ChartUtilities.saveChartAsPNG(file1, chart, 600, 400, info);
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
//        }





//        ForecastingChartDemo demo = new ForecastingChartDemo("Forecasting Demo: Time Series");
//        demo.pack();
//        demo.setVisible(true);


        return super.referenceData(request, command, errors);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, org.springframework.validation.BindException errors) throws Exception {

//        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//        List<WholesalerProductsStock> list = commonManager.fetchWholesalerProductsStocksByProductId("35");
//        String prodName = list.get(0).getWholesaleProducts().getWhProdName();
//        for (WholesalerProductsStock wps : list) {
//            dataset.setValue(wps.getWhProdMarginPercentage(), "Profit Margin (in %)", wps.getWholesaler().getWhName());
//        }
//        JFreeChart chart = ChartFactory.createBarChart("Profit Margins for Suppliers on " + "'" + prodName + "'",
//                "Suppliers", "Profit Margin (in %)", dataset, PlotOrientation.VERTICAL, true, true, true);
//        try {
//
//            ServletContext context = request.getSession().getServletContext();
//            String realContextPath = context.getRealPath(request.getContextPath());
//            System.out.println("*** getRealPath: " + realContextPath);
//            String dirSubPart = "web\\jspchart\\";
//            String dirName = realContextPath.substring(0, realContextPath.lastIndexOf("build")) + dirSubPart + "SuppliersProfitMargins1.jpg";
//            request.setAttribute("imageName", "SuppliersProfitMargins1.jpg");
//            System.out.println("*** Saving on: " + dirName);
//            ChartUtilities.saveChartAsJPEG(new File(dirName), chart, 800, 600);
//            // ChartUtilities.saveChartAsPNG(new File("C:\\Users\\mithun\\Documents\\NetBeansProjects\\SOM\\web\\jspchart\\barchart.jpg"), chart, 800, 500);
//        } catch (IOException e) {
//            System.err.println("Problem occurred creating chart.");
//        }


        // load the data

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<WholesalerProductsStock> list = commonManager.fetchWholesalerProductsStocksByProductId("35");
        String prodName = list.get(0).getWholesaleProducts().getWhProdName();
        for (WholesalerProductsStock wps : list) {
            dataset.setValue(wps.getWhProdMarginPercentage(), "Profit Margin (in %)", wps.getWholesaler().getWhName());
        }

        // create the chart

        final JFreeChart chart = ChartFactory.createBarChart("Profit Margins for Suppliers on " + "'" + prodName + "'",
                "Suppliers", "Profit Margin (in %)", dataset, PlotOrientation.VERTICAL, true, true, true);

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
            request.setAttribute("imageName", "WholesalerProfitMarginAnalysis.jpg");
            // System.out.println("*** Saving on: " + dirName);
            // ChartUtilities.saveChartAsJPEG(new File(dirName), chart, 800, 600);
            // for build
            String buildDir = realContextPath.substring(0, realContextPath.lastIndexOf("web")) + dirSubPart + "WholesalerProfitMarginAnalysis.jpg";
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
        return new ModelAndView(getSuccessView());
    }
}
