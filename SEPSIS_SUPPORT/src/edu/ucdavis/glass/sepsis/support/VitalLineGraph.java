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
		
		int xAxis = 0; // USE 0,1,2,3,... as X axis for now, might need to change.
		for(Vital i : p.vitals)
		{
			TempSeries.add(xAxis, Integer.parseInt(i.temperature));
			RRSeries.add(xAxis, Integer.parseInt(i.respiratoryRate));
			WBCSeries.add(xAxis, Integer.parseInt(i.WBC));
			SBPSeries.add(xAxis, Integer.parseInt(i.SBP));
			MAPSeries.add(xAxis, Integer.parseInt(i.MAP));
			xAxis++;
		}
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(TempSeries);
		dataset.addSeries(RRSeries);
		dataset.addSeries(WBCSeries);
		dataset.addSeries(SBPSeries);
		dataset.addSeries(MAPSeries);
		
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); // Holds a collection of XYSeriesRenderer and customizes the graph
		XYSeriesRenderer TempRenderer = new XYSeriesRenderer(); // This will be used to customize series 1
		XYSeriesRenderer RRrenderer = new XYSeriesRenderer(); // This will be used to customize series 2
		XYSeriesRenderer WBCrenderer = new XYSeriesRenderer(); // This will be used to customize series 3
		XYSeriesRenderer SBPrenderer = new XYSeriesRenderer(); // This will be used to customize series 4
		XYSeriesRenderer MAPrenderer = new XYSeriesRenderer(); // This will be used to customize series 5
		mRenderer.addSeriesRenderer(TempRenderer);
		mRenderer.addSeriesRenderer(RRrenderer);
		mRenderer.addSeriesRenderer(WBCrenderer);
		mRenderer.addSeriesRenderer(SBPrenderer);
		mRenderer.addSeriesRenderer(MAPrenderer);
		mRenderer.setXAxisMax(7.5); //can set scale here
		
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
