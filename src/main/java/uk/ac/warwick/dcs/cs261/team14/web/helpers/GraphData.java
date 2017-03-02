package uk.ac.warwick.dcs.cs261.team14.web.helpers;

/**
 * Created by kwekmh on 02/03/17.
 */
public class GraphData {

    private String[] rollingX;
    private double[] rollingY;

    private String[] anomalousX;
    private double[] anomalousY;

    public GraphData(String[] rollingX, double[] rollingY, String[] anomalousX, double[] anomalousY) {
        this.rollingX = rollingX;
        this.rollingY = rollingY;
        this.anomalousX = anomalousX;
        this.anomalousY = anomalousY;
    }

    public String[] getRollingX() {
        return rollingX;
    }

    public double[] getRollingY() {
        return rollingY;
    }

    public String[] getAnomalousX() {
        return anomalousX;
    }

    public double[] getAnomalousY() {
        return anomalousY;
    }
}
