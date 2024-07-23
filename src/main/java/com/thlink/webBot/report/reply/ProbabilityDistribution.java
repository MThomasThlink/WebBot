package com.thlink.webBot.report.reply;

import java.awt.Dimension;
import java.io.File;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.statistics.HistogramDataset;

public class ProbabilityDistribution extends ApplicationFrame
{
    
    public ProbabilityDistribution(String title) {
        super(title);
    }

    public static void main(String[] args) {
        double[] values = new double[1000];
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.random() * 100; // Example: random values between 0 and 100
        }

        int numberOfBins = 20;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double value : values) {
            if (value < min) min = value;
            if (value > max) max = value;
        }

        double binWidth = (max - min) / numberOfBins;
        double[] binEdges = new double[numberOfBins + 1];
        double[] binCounts = new double[numberOfBins];

        for (int i = 0; i <= numberOfBins; i++) {
            binEdges[i] = min + i * binWidth;
        }

        for (double value : values) {
            int binIndex = (int) ((value - min) / binWidth);
            if (binIndex >= numberOfBins) binIndex = numberOfBins - 1; // Handle the max value case
            binCounts[binIndex]++;
        }

        double[] probabilities = new double[numberOfBins];
        for (int i = 0; i < numberOfBins; i++) {
            probabilities[i] = binCounts[i] / values.length;
        }

        HistogramDataset dataset = new HistogramDataset();
        dataset.addSeries("Probability Distribution", values, numberOfBins);

        JFreeChart histogram = ChartFactory.createHistogram(
                "Probability Distribution",
                "Value",
                "Probability",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        XYPlot plot = (XYPlot) histogram.getPlot();
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setUpperMargin(0);
        plot.setForegroundAlpha(0.85f);

        ProbabilityDistribution demo = new ProbabilityDistribution("Probability Distribution");
        ChartPanel chartPanel = new ChartPanel(histogram);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        demo.setContentPane(chartPanel);
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);

        // Save as image
        try {
            File imageFile = new File("ProbabilityDistribution.png");
            ImageIO.write(histogram.createBufferedImage(800, 600), "png", imageFile);
            System.out.println("Chart saved as ProbabilityDistribution.png");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Print probabilities
        for (int i = 0; i < numberOfBins; i++) {
            System.out.printf("Interval: [%.2f, %.2f], Probability: %.4f%n", binEdges[i], binEdges[i + 1], probabilities[i]);
        }
    }
}
