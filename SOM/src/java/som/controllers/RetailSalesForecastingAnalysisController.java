/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package som.controllers;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;
import net.sourceforge.openforecast.models.MovingAverageModel;
import net.sourceforge.openforecast.models.NaiveForecastingModel;
import net.sourceforge.openforecast.models.PolynomialRegressionModel;
import net.sourceforge.openforecast.models.RegressionModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import org.jfree.data.xy.XYDataset;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import som.domain.RetailAnalysisWrapper;
import som.domain.RetailerProductsStock;
import som.service.CommonManager;

/**
 *
 * @author mithun
 */
public class RetailSalesForecastingAnalysisController extends SimpleFormController {

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
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        RetailAnalysisWrapper raw = (RetailAnalysisWrapper) command;
        int fromYear = raw.getFromD().getYear() + 1900;
        int toYear = raw.getToD().getYear() + 1900;

        raw.setCalFromDate(new GregorianCalendar(fromYear, raw.getFromD().getMonth(), raw.getFromD().getDate()));
        raw.setCalToDate(new GregorianCalendar(toYear, raw.getToD().getMonth(), raw.getToD().getDate()));


        //System.out.println("Cal dates"+ raw.getCalFromDate().getTime()+" "+ raw.getCalToDate().getTime());
        final XYDataset dataset = createTimeSeriesDataset(request, raw);
        final JFreeChart chart = createChart(dataset);
        displayChart(request, chart);
        return super.onSubmit(request, response, command, errors);
    }

    private XYDataset createTimeSeriesDataset(HttpServletRequest request, RetailAnalysisWrapper raw) {
        int fromYear = raw.getFromD().getYear() + 1900;
        int toYear = raw.getToD().getYear() + 1900;

        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
// from Date
        Calendar fromDate = new GregorianCalendar(fromYear, raw.getFromD().getMonth(), raw.getFromD().getDate());
// to date
        Calendar toDate = new GregorianCalendar(toYear, raw.getToD().getMonth(), raw.getToD().getDate());
// current date
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.MONTH, currentDate.get(Calendar.MONTH));
//        raw.setFromDate(sdf.format(raw.getFromD()));
//        raw.setToDate(sdf.format(raw.getToD()));
        System.out.println("fr " + fromDate.getTime() + " to " + toDate.getTime() + " curr " + currentDate.getTime());

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries observations = new TimeSeries("Sold units", Day.class);
        TimeSeries fc = new TimeSeries("Forecast Unit Sales", Day.class);
        TimeSeries bestFc = new TimeSeries("Best Forecast Unit Sales", Day.class);
        TimeSeries uppThreshold = new TimeSeries("Stock Upper Threshold", Day.class);
        TimeSeries lowThreshold = new TimeSeries("Stock Lower Threshold", Day.class);
        TimeSeries orderQuantity = new TimeSeries("Ordered Stock", Day.class);
        TimeSeries availableStockHistorySeries = new TimeSeries("Available Stock", Day.class);
        DataSet initDataSet = new DataSet(); // data set of input history data that will be given as input to forecasting model
        Integer diffFrom_CurrentDate = daysBetween(fromDate.getTime(), currentDate.getTime());
        // System.out.println("differenceOfDays : " + diffFrom_CurrentDate);
        SortedMap<Date, Long> map = commonManager.fetchRetailerSalesAndDate(raw);
        List<RetailerProductsStock> rpsList = commonManager.fetchRetailerProductsStocksByProdIdAndRtId(raw.getProductId().toString(), raw.getSupplierId().toString());
        SortedMap<Date, Long> orderInfoMap = commonManager.fetchRetailerOrderQntyAndDate(raw);
        SortedMap<Date, Integer> availableStockHistoryMap = commonManager.fetchRetailerAvailableStockHistoryByRetailerIdAndProdId(raw);
        Integer upThreshValue = 0;
        Integer lowThreshValue = 0;
        Integer diffFromDate_ToDate = daysBetween(fromDate.getTime(), toDate.getTime());
        diffFromDate_ToDate = diffFromDate_ToDate - 1;
        Integer availableStockHistoryValue = 0;
        boolean availabeStartFlag = Boolean.FALSE;
        for (RetailerProductsStock rps : rpsList) {
            upThreshValue = rps.getRtStockUpThrQnty();
            lowThreshValue = rps.getRtStockLowThrQnty();
        }

        for (int i = 0; i <= diffFrom_CurrentDate; i++) {
            int dayOfMonth = fromDate.get(Calendar.DAY_OF_MONTH);
            int year = fromDate.get(Calendar.YEAR);
            int month = fromDate.get(Calendar.MONTH);
            int displayMonth = month + 1;
            Long value = 0L;

            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                //System.out.println("###########Map values#######:" + e.getKey() + " " + e.getValue());
                Date d = (Date) e.getKey();
                //System.out.println("From Date" + sdf.format(fromDate.getTime()) + "  key sting " + d.toString());
                Calendar tempFromDate = new GregorianCalendar();
                tempFromDate.setTime(fromDate.getTime());
                //tempFromDate.set(Calendar.MONTH, tempFromDate.get(Calendar.MONTH));
                if (sdf.format(tempFromDate.getTime()).equals(d.toString())) {
                    //System.out.println("###########Map values matches  for date#######:" + fromDate.getTime());
                    value = (Long) e.getValue();
                    break;
                }
            }
            Long orderedQuantityValue = 0L;
            Iterator orderInfoItr = orderInfoMap.entrySet().iterator();
            while (orderInfoItr.hasNext()) {
                Map.Entry e = (Map.Entry) orderInfoItr.next();
                Date d = (Date) e.getKey();
                Calendar tempFromDate = new GregorianCalendar();
                tempFromDate.setTime(fromDate.getTime());
                if (sdf.format(tempFromDate.getTime()).equals(d.toString())) {
                    orderedQuantityValue = (Long) e.getValue();
                    break;
                }
            }


            Iterator availableStockHistoryItr = availableStockHistoryMap.entrySet().iterator();
            while (availableStockHistoryItr.hasNext()) {
                Map.Entry e = (Map.Entry) availableStockHistoryItr.next();
                Date d = (Date) e.getKey();
                Calendar tempFromDate = new GregorianCalendar();
                tempFromDate.setTime(fromDate.getTime());
                if (sdf.format(tempFromDate.getTime()).equals(d.toString())) {
                    availableStockHistoryValue = (Integer) e.getValue();
                    availabeStartFlag = Boolean.TRUE;
                }
            }

            //ForecastingModel model = Forecaster.getBestForecast((DataSet) dataset);
            //model.init((DataSet) dataset);

            observations.add(new Day(dayOfMonth, displayMonth, year), value);
            fc.addOrUpdate(new Day(dayOfMonth, displayMonth, year), 0L);
            bestFc.add(new Day(dayOfMonth, displayMonth, year), 0L);
            uppThreshold.add(new Day(dayOfMonth, displayMonth, year), upThreshValue);
            lowThreshold.add(new Day(dayOfMonth, displayMonth, year), lowThreshValue);
            orderQuantity.add(new Day(dayOfMonth, displayMonth, year), orderedQuantityValue);
            if (availabeStartFlag) {
                availableStockHistorySeries.add(new Day(dayOfMonth, displayMonth, year), availableStockHistoryValue);
            }
            System.out.println(">>>>>>>>>>>>>>>>>>>>> first" + dayOfMonth + "-" + displayMonth + "-" + year);
            fromDate.set(Calendar.DAY_OF_MONTH, fromDate.get(Calendar.DAY_OF_MONTH) + 1);
        }

        Integer diffCurrent_ToDate = daysBetween(currentDate.getTime(), toDate.getTime());
        Calendar tempCurrentDate = new GregorianCalendar();
        tempCurrentDate.setTime(currentDate.getTime());
        tempCurrentDate.set(Calendar.DAY_OF_MONTH, tempCurrentDate.get(Calendar.DAY_OF_MONTH) + 1);
        System.out.println(">>>>>>>>>>>>>>>>>>>>> second diffCurrent_ToDate" + diffCurrent_ToDate);
        for (int i = 0; i <= diffCurrent_ToDate; i++) {
            int dayOfMonth = tempCurrentDate.get(Calendar.DAY_OF_MONTH);
            int year = tempCurrentDate.get(Calendar.YEAR);
            int month = tempCurrentDate.get(Calendar.MONTH);
            int displayMonth = month + 1;
            fc.addOrUpdate(new Day(dayOfMonth, displayMonth, year), 0L);
            bestFc.addOrUpdate(new Day(dayOfMonth, displayMonth, year), 0L);
            uppThreshold.addOrUpdate(new Day(dayOfMonth, displayMonth, year), upThreshValue);
            lowThreshold.addOrUpdate(new Day(dayOfMonth, displayMonth, year), lowThreshValue);
            tempCurrentDate.set(Calendar.DAY_OF_MONTH, tempCurrentDate.get(Calendar.DAY_OF_MONTH) + 1);
            System.out.println(">>>>>>>>>>>>>>>>>>>>> second" + dayOfMonth + "-" + displayMonth + "-" + year);
        }

        initDataSet = getDataSet(observations, 0, diffFrom_CurrentDate);


        TimeSeries naiveSeries = getForecastTimeSeries(new NaiveForecastingModel(),
                initDataSet,
                0, diffFromDate_ToDate,
                "Naive forecast", fc);


        TimeSeries ma4Series = getForecastTimeSeries(new MovingAverageModel(4),
                initDataSet,
                0, diffFromDate_ToDate,
                "4 Period Moving Average", fc);
        TimeSeries ma8Series = getForecastTimeSeries(new MovingAverageModel(8),
                initDataSet,
                0, diffFromDate_ToDate,
                "8 Period Moving Average", fc);
        TimeSeries regressionSeries = getForecastTimeSeries(new RegressionModel("t"),
                initDataSet,
                0, diffFromDate_ToDate,
                "Linear regression", fc);
        TimeSeries polyRegressSeries = getForecastTimeSeries(new PolynomialRegressionModel("t", 4),
                initDataSet,
                0, diffFromDate_ToDate,
                "4th order polynomial regression", fc);

        System.out.println("Model used by bestForecast is:");
        ForecastingModel model = Forecaster.getBestForecast(initDataSet);
        TimeSeries bestForecastSeries = getForecastTimeSeries(model,
                initDataSet,
                0, diffFromDate_ToDate,
                "Best forecast", bestFc);
        //1 blue dark
        // 2 red
        // 3 light blue
        // 4 green
        // 5 yellow
        // 6 pink
        // 7 flourosscent blue
        //8 cant see light grey
        // 9 dark grey

        dataset.addSeries(availableStockHistorySeries);// 1 dark blue
        dataset.addSeries(observations); // 2 red
        dataset.addSeries(uppThreshold);//3 light blue
        dataset.addSeries(lowThreshold);//4 light green
        dataset.addSeries(bestForecastSeries); // 5 yellow
        dataset.addSeries(orderQuantity); // 2 red

        dataset.addSeries(naiveSeries);
        dataset.addSeries(ma4Series);
        dataset.addSeries(ma8Series);
        dataset.addSeries(regressionSeries);
        dataset.addSeries(polyRegressSeries);


        return dataset;
    }

    private DataSet getDataSet(TimeSeries series,
            int startIndex, int endIndex) {
        DataSet dataSet = new DataSet();
//        if (endIndex > series.getItemCount()) {
//            endIndex = series.getItemCount();
//        }

        for (int i = startIndex; i < endIndex; i++) {
            TimeSeriesDataItem dataPair = series.getDataItem(i);
            DataPoint dp = new Observation(dataPair.getValue().doubleValue());
            dp.setIndependentValue("t", i);
            dataSet.add(dp);
        }

        return dataSet;
    }

    private TimeSeries getForecastTimeSeries(ForecastingModel model,
            DataSet initDataSet,
            int startIndex,
            int endIndex,
            String title, TimeSeries fc) {
        model.init(initDataSet);
        // Get range of data required for forecast
        DataSet fcDataSet = getDataSet(fc, startIndex, endIndex);
        model.forecast(fcDataSet);
        System.out.println(">>>>>>>>>>>> Forecast Type : " + model.getForecastType());
        System.out.println(">>>>>>>>>>>> MSE : " + model.getMSE());
        System.out.println(">>>>>>>>>>>> MAD : " + model.getMAD());
        System.out.println(">>>>>>>>>>>> SAE : " + model.getSAE());
        System.out.println(">>>>>>>>>>>> MAPE : " + model.getMAPE());
        TimeSeries series = new TimeSeries(title, fc.getTimePeriodClass());
        // Iterator through the forecast results, adding to the series
        Iterator it = fcDataSet.iterator();
        while (it.hasNext()) {
            DataPoint dp = (DataPoint) it.next();
            int index = (int) dp.getIndependentValue("t");
            series.add(fc.getTimePeriod(index), dp.getDependentValue());
        }
        return series;
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * Creates a chart.
     *
     * @param dataset  a dataset.
     *
     * @return A chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Forecast of Retail Sales",
                "Date",
                "No. Of Units",
                dataset,
                true,
                true,
                false);

        XYPlot plot = chart.getXYPlot();

        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 0, 255));
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yy"));
        //CategoryPlot p = chart.getCategoryPlot(); // Get the Plot object for a bar graph
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.WHITE);
        return chart;
    }

    public void displayChart(HttpServletRequest request, JFreeChart chart) {
        try {
            ServletContext context = request.getSession().getServletContext();
            String realContextPath = context.getRealPath(request.getContextPath());
            //System.out.println("*** getRealPath: " + realContextPath);
            String dirSubPart = "web\\jspchart\\";
            //String dirName = realContextPath.substring(0, realContextPath.lastIndexOf("build")) + dirSubPart + "RetailSalesAnalysis.jpg";
            request.setAttribute("imageName", "RetailSalesForecastAnalysis.jpg");
            // System.out.println("*** Saving on: " + dirName);
            // ChartUtilities.saveChartAsJPEG(new File(dirName), chart, 800, 600);
            // for build
            String buildDir = realContextPath.substring(0, realContextPath.lastIndexOf("web")) + dirSubPart + "RetailSalesForecastAnalysis.jpg";
            File file = new File(buildDir);
            if (file != null && file.exists()) {
                file.delete();
            }
            //file = new File(buildDir);
            ChartUtilities.saveChartAsJPEG(file, chart, 1280, 1024);
            System.out.println("*** Saving on build: " + buildDir);
            // ChartUtilities.saveChartAsPNG(new File("C:\\Users\\mithun\\Documents\\NetBeansProjects\\SOM\\web\\jspchart\\barchart.jpg"), chart, 800, 500);
        } catch (IOException e) {
            System.err.println("Problem occurred creating chart.");
        }
    }
}
