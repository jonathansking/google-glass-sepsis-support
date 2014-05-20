package edu.ucdavis.glass.sepsis.support;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import edu.ucdavis.glass.sepsis.support.Patient.Vital;
import android.content.Context;
import android.graphics.Color;

public class VitalLineGraph{

	public GraphicalView getGraph(Context context) {
		
		Patient p = Global.recentPatients.peek();
		// create series for each graph
		TimeSeries TempSeries = new TimeSeries("Temp");
		TimeSeries RRSeries = new TimeSeries("RR");
		TimeSeries WBCSeries = new TimeSeries("WBC");
		TimeSeries SBPSeries = new TimeSeries("SBP"); 
		TimeSeries MAPSeries = new TimeSeries("MAP");
		
		double xAxis = 11.0/10.0; 
		for(Vital i : p.vitals)
		{
			TempSeries.add(xAxis, Double.parseDouble(i.temperature));
			RRSeries.add(xAxis, Double.parseDouble(i.respiratoryRate));
			WBCSeries.add(xAxis, Double.parseDouble(i.WBC));
			SBPSeries.add(xAxis, Double.parseDouble(i.SBP));
			MAPSeries.add(xAxis, Double.parseDouble(i.MAP));
			xAxis +=11.0/10.0 ;
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(TempSeries);
		dataset.addSeries(RRSeries);
		dataset.addSeries(WBCSeries);
		dataset.addSeries(SBPSeries);
		dataset.addSeries(MAPSeries);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
		XYSeriesRenderer TempRenderer = new XYSeriesRenderer();
		XYSeriesRenderer RRrenderer = new XYSeriesRenderer();
		XYSeriesRenderer WBCrenderer = new XYSeriesRenderer();
		XYSeriesRenderer SBPrenderer = new XYSeriesRenderer();
		XYSeriesRenderer MAPrenderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(TempRenderer);
		mRenderer.addSeriesRenderer(RRrenderer);
		mRenderer.addSeriesRenderer(WBCrenderer);
		mRenderer.addSeriesRenderer(SBPrenderer);
		mRenderer.addSeriesRenderer(MAPrenderer);
		mRenderer.setXAxisMax(10.0); //can set scale here
		
		// Customization time for Temperature
		TempRenderer.setColor(Color.CYAN);
		TempRenderer.setFillPoints(true);
		// Customization time for Respiratory
		RRrenderer.setColor(Color.rgb(255, 0, 255));
		RRrenderer.setFillPoints(true);
		// Customization time for WBC
		WBCrenderer.setColor(Color.WHITE);
		WBCrenderer.setFillPoints(true);
		// Customization time for SBP
		SBPrenderer.setColor(Color.RED);
		SBPrenderer.setFillPoints(true);
		// Customization time for MAP
		MAPrenderer.setColor(Color.BLUE);
		MAPrenderer.setFillPoints(true);
		
		GraphicalView graph = ChartFactory.getLineChartView(context, dataset, mRenderer);
		return graph;
		
	}

}
