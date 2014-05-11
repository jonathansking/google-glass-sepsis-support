package edu.ucdavis.glass.sepsis.support;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;

public class VitalLineGraph{

	public GraphicalView getGraph(Context context) {
		
		// Temperature data
		int[] x = { 1, 2, 3, 4, 5, 6, 7}; // x values!
		int[] y =  { 40, 41, 37, 39, 40, 38, 40 }; // y values!
		TimeSeries series = new TimeSeries("Temp"); 
		for( int i = 0; i < x.length; i++)
		{
			series.add(x[i], y[i]);
		}
		
		// Respiratory rate
		int[] x2 = { 1, 2, 3, 4, 5, 6, 7}; // x values!
		int[] y2 =  { 21, 20, 3, 18, 3, 2, 19}; // y values!
		TimeSeries series2 = new TimeSeries("RR"); 
		for( int i = 0; i < x2.length; i++)
		{
			series2.add(x2[i], y2[i]);
		}
		
		// WBC rate
		int[] x3 = { 1, 2, 3, 4, 5, 6, 7}; // x values!
		int[] y3 =  { 19, 20, 23, 20, 22, 20, 21}; // y values!
		TimeSeries series3 = new TimeSeries("WBC"); 
		for( int i = 0; i < x3.length; i++)
		{
			series3.add(x3[i], y3[i]);
		}
		
		// SBP rate
		int[] x4 = { 1, 2, 3, 4, 5, 6, 7}; // x values!
		int[] y4 =  { 92, 90, 91, 93, 89, 91, 92}; // y values!
		TimeSeries series4 = new TimeSeries("SBP"); 
		for( int i = 0; i < x4.length; i++)
		{
			series4.add(x4[i], y4[i]);
		}
		
		// SBP rate
		int[] x5 = { 1, 2, 3, 4, 5, 6, 7}; // x values!
		int[] y5 =  { 72, 70, 71, 73, 69, 71, 72}; // y values!
		TimeSeries series5 = new TimeSeries("MAP"); 
		for( int i = 0; i < x5.length; i++)
		{
			series5.add(x5[i], y5[i]);
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(series);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		dataset.addSeries(series4);
		dataset.addSeries(series5);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
		XYSeriesRenderer renderer = new XYSeriesRenderer(); // This will be used to customize series 1
		XYSeriesRenderer renderer2 = new XYSeriesRenderer(); // This will be used to customize series 2
		XYSeriesRenderer renderer3 = new XYSeriesRenderer(); // This will be used to customize series 2
		XYSeriesRenderer renderer4 = new XYSeriesRenderer(); // This will be used to customize series 2
		XYSeriesRenderer renderer5 = new XYSeriesRenderer(); // This will be used to customize series 2
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.addSeriesRenderer(renderer2);
		mRenderer.addSeriesRenderer(renderer3);
		mRenderer.addSeriesRenderer(renderer4);
		mRenderer.addSeriesRenderer(renderer5);
		
		// Customization time for Temperature
		renderer.setColor(Color.CYAN);
		renderer.setFillPoints(true);
		// Customization time for Respiratory
		renderer2.setColor(Color.MAGENTA);
		renderer2.setFillPoints(true);
		// Customization time for WBC
		renderer2.setColor(Color.WHITE);
		renderer2.setFillPoints(true);
		// Customization time for SBP
		renderer2.setColor(Color.RED);
		renderer2.setFillPoints(true);
		// Customization time for MAP
		renderer2.setColor(Color.BLUE);
		renderer2.setFillPoints(true);
		
		GraphicalView graph = ChartFactory.getLineChartView(context, dataset, mRenderer);
		return graph;
		
	}

}
