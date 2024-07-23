package com.thlink.webBot.report.reply;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ui.UIUtils;
import org.jfree.data.statistics.HistogramDataset;

public class ProbProcessor {
    
    private final String title;
    private double[] values;
    private int numberOfBins = 20;
    private XYPlot plot;
    private HistogramDataset dataset;
    private JFreeChart histogram;
  //private double[] probabilities;
    private double[] binEdges;
    private double min, max;
    
    public ProbProcessor (String title) {
        this.title = title;
    }
    
    public boolean go ()
    {
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;

        for (double value : values) 
        {
            if (value < min) min = value;
            if (value > max) max = value;
        }
        
        double binWidth = (max - min) / numberOfBins;
        binEdges = new double[numberOfBins + 1];
        double[] binCounts = new double[numberOfBins];
        
        for (int i = 0; i <= numberOfBins; i++) {
            binEdges[i] = min + i * binWidth;
        }

        for (double value : values) {
            int binIndex = (int) ((value - min) / binWidth);
            if (binIndex >= numberOfBins) binIndex = numberOfBins - 1; // Handle the max value case
            binCounts[binIndex]++;
        }

        /*probabilities = new double[numberOfBins];
        for (int i = 0; i < numberOfBins; i++) {
            probabilities[i] = binCounts[i] / values.length; //Mostrar o percentual de ocorrência (não a quantidade) na vertical
        }*/
        
        dataset = new HistogramDataset();
        dataset.addSeries("Probability Distribution", values, numberOfBins);

        histogram = ChartFactory.createHistogram(
                title,
                "Tempo [min]",
                "Quantidade",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        plot = (XYPlot) histogram.getPlot();
      //XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

        plot.getDomainAxis().setLowerMargin(0);
        plot.getDomainAxis().setUpperMargin(0);
        plot.setForegroundAlpha(0.85f);
        
        return true;
    }
    
    public void showGraph ()
    {
        ProbabilityDistribution demo = new ProbabilityDistribution("Probability Distribution");
        ChartPanel chartPanel = new ChartPanel(histogram);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        demo.setContentPane(chartPanel);
        demo.pack();
        UIUtils.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
    
    public void savePicture (String pFileName, String pFormat)
    {
        // Save as image
        try {
            File imageFile = new File(pFileName.concat("\\").concat(pFormat));
            ImageIO.write(histogram.createBufferedImage(800, 600), pFormat, imageFile);
            System.out.println("Chart saved as " + pFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void saveBufferedImage (BufferedImage pImg, String pFileName, String pFormat)
    {
        // Save as image
        try {
            File imageFile = new File(pFileName.concat(".").concat(pFormat));
            ImageIO.write(pImg, pFormat, imageFile);
            System.out.println("Chart saved as " + pFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public BufferedImage getBufferedImage ()
    {
        return getBufferedImage(800, 600);
    }
    public BufferedImage getBufferedImage (int pX, int pY)
    {
        return histogram.createBufferedImage(pX, pY);
    }
    
    public BufferedImage getBufferedImageAvgMean (double average, double mean) 
    {
        int width = 800;
        int height = 600;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Fill the background with white
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Draw the axes
        g2d.setColor(Color.BLACK);
        int padding = 50;
        int graphWidth = width - 2 * padding;
        int graphHeight = height - 2 * padding;

        // Draw x and y axis
        g2d.drawLine(padding, height - padding, padding, padding);
        g2d.drawLine(padding, height - padding, width - padding, height - padding);

        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;

        for (double value : values) 
        {
            if (value < minValue) minValue = value;
            if (value > maxValue) maxValue = value;
        }
        
        // Calculate scale
        double xScale = (double) graphWidth / (values.length - 1);
        double yMax = maxValue;//10; // Adjust based on your data
        double yScale = (double) graphHeight / yMax;

        // Draw data points
        g2d.setColor(Color.BLUE);
        for (int i = 0; i < values.length; i++) {
            int x = padding + (int) (i * xScale);
            int y = height - padding - (int) (values[i] * yScale);
            g2d.fillOval(x - 3, y - 3, 6, 6); // Draw each data point as a small circle
        }

        // Draw average line
        g2d.setColor(Color.RED);
        int avgY = height - padding - (int) (average * yScale);
        g2d.drawLine(padding, avgY, width - padding, avgY);
        g2d.drawString("Média: " + average, width - padding + 10, avgY);

        // Draw mean line
        /*g2d.setColor(Color.GREEN);
        int meanY = height - padding - (int) (mean * yScale);
        g2d.drawLine(padding, meanY, width - padding, meanY);
        g2d.drawString("Mean: " + mean, width - padding + 10, meanY);*/
        int midY = height / 1;
      //addAxisLabels(g2d, width, height);
        addAxisLabelsAndValues(g2d, width, height, midY);
        // Dispose the graphics context
        g2d.dispose();

        return image;
    }

    private void addAxisLabels (Graphics2D g2d, int width, int height) 
    {
        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 14);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);

        // Horizontal axis label
        String xAxisLabel = "X Axis";
        int xLabelWidth = metrics.stringWidth(xAxisLabel);
        g2d.drawString(xAxisLabel, (width - xLabelWidth) / 2, height - 10);

        // Vertical axis label
        String yAxisLabel = "Y Axis";
        int yLabelWidth = metrics.stringWidth(yAxisLabel);
        int xPosition = 10;
        int yPosition = (height + yLabelWidth) / 2;

        // Rotate the label for the Y-axis
        g2d.rotate(-Math.PI / 2);
        g2d.drawString(yAxisLabel, -yPosition, xPosition);
        g2d.rotate(Math.PI / 2);
    }
    private void addAxisLabelsAndValues (Graphics2D g2d, int width, int height, int midY) 
    {
        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 14);
        g2d.setFont(font);
        FontMetrics metrics = g2d.getFontMetrics(font);

        // Axis labels
        String xAxisLabel = "Respostas";
        String yAxisLabel = "Tempo [min]";

        // Draw X-axis label
        int xLabelWidth = metrics.stringWidth(xAxisLabel);
        g2d.drawString(xAxisLabel, (width - xLabelWidth) / 2, height - 20);

        // Draw Y-axis label (rotated)
        int yLabelWidth = metrics.stringWidth(yAxisLabel);
        int xPosition = 10;
        int yPosition = (height + yLabelWidth) / 2;
        g2d.rotate(-Math.PI / 2);
        g2d.drawString(yAxisLabel, -yPosition, xPosition+5);
        g2d.rotate(Math.PI / 2);

        // Draw values along the X-axis
        /*int xAxisYPosition = height - 50;
        for (int i = 50; i < width - 50; i += 50) {
            String value = Integer.toString(i - 50);
            int valueWidth = metrics.stringWidth(value);
            g2d.drawString(value, i - valueWidth / 2, xAxisYPosition + 20);
            g2d.drawLine(i, xAxisYPosition - 5, i, xAxisYPosition + 5);
        }*/
        
        // Set dashed line pattern
        float[] dashPattern = {5, 5}; // Dash pattern: 5 pixels on, 5 pixels off
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0);

        int labelCount = 10;
        int padding = 50;
        min = 0;
        double labelInterval = (max - min) / labelCount;
        for (int i = 0; i <= labelCount; i++) 
        {
            yPosition = height - padding - (i * (height - 2 * padding) / labelCount); 
            double labelValue = min + i * labelInterval;
            
            // Draw label text
            String labelText = String.format("%d", (int)labelValue);
            int labelWidth = metrics.stringWidth(labelText);
            g2d.drawString(labelText, padding - labelWidth - 10, yPosition + metrics.getHeight() / 2);
            
            // Draw tick marks
            g2d.drawLine(padding - 5, yPosition, padding, yPosition);
            
            // Draw dashed horizontal line
            g2d.setStroke(dashed);
            g2d.drawLine(padding, yPosition, width - padding, yPosition);
            g2d.setStroke(new BasicStroke()); // Reset to solid stroke
        }
    }
    
    public double calculateAverage(double[] dataPoints) {
        double sum = 0.0;
        for (double point : dataPoints) {
            sum += point;
        }
        return sum / dataPoints.length;
    }

    public double calculateMean(double[] dataPoints) {
        // Mean is same as average in this context
        return calculateAverage(dataPoints);
    }
    
    /*public void printResults ()
    {
        // Print probabilities
        for (int i = 0; i < numberOfBins; i++) {
            System.out.printf("Interval: [%.2f, %.2f], Probability: %.4f%n", binEdges[i], binEdges[i + 1], probabilities[i]);
        }
    }*/
    public void setValues(double[] values) {
        this.values = values;
    }

    public void setNumberOfBins(int numberOfBins) {
        this.numberOfBins = numberOfBins;
    }
 
    
}
