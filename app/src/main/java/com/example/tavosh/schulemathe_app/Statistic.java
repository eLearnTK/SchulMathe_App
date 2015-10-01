package com.example.tavosh.schulemathe_app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

/* Carlos Trujillo   29/09/2015                                           */
/* This class is used to create the statistic shown at the end of the app */
public class Statistic extends ActionBarActivity {
    public String nextAufgabe;
    int indx_graph = 0;
    int sizeY;
    /* This variables stores the Qualification data to create the chart */
    int[] x_Q;
    int[] y_Q;
    double x_temp_Q;
    double y_temp_Q;

    String[] sTests = new String[10];
    int indx_sTests = 0;

    LinearLayout layout;
    GraphicalView gView;
    String j;
    XYSeriesRenderer renderer_Q = new XYSeriesRenderer();
    //XYSeriesRenderer renderer2 = new XYSeriesRenderer();
    //XYSeriesRenderer renderer3 = new XYSeriesRenderer();
    CategorySeries series_Q = new CategorySeries("Tests");
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    Button btnRew;
    Button btnFwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        nextAufgabe = MainActivity.vecTest.get(indx_graph).toString();

        /* displays the first values of the chart on the screen */
        layout = (LinearLayout) findViewById(R.id.BarGraph);
        gView = getViewFwd(this);
        layout.addView(gView);

        btnRew = (Button) findViewById(R.id.btnAktBack);
        btnFwd = (Button) findViewById(R.id.btnAktFwd);
        btnRew.setEnabled(false);
    }

    /* This function generates all the values and format for the chart */
    public GraphicalView getViewFwd(Context context) {
        /* evaluates how many task has the TEST*/
        sizeY = evalQtest(nextAufgabe);
        //sTests[indx_sTests] = nextAufgabe + "," + sizeY;
        sTests[indx_sTests] = nextAufgabe;

        y_Q = new int[sizeY];
        x_Q = new int[sizeY];
        //y2 = new int[sizeY];
        //x2 = new int[sizeY];
        //y3 = new int[sizeY];
        //x3 = new int[sizeY];
        //int i2 = indx_graph;
        //int i3 = indx_graph;

        while (indx_graph < sizeY) {
            j = MainActivity.vecQualifikation.get(indx_graph).toString();
            y_Q[indx_graph] = Integer.parseInt(j);
            x_Q[indx_graph] = indx_graph+1;

            indx_graph++;
        } // while

        /*while (i2 < sizeY) {
            j = vecFeeling.get(i2).toString();
            y2[i2] = Integer.parseInt(j);
            x2[i2] = i2+1;

            i2++;
        }*/

        /*while (i3 < sizeY) {
            j = vecFeeling2.get(i3).toString();
            y3[i3] = Integer.parseInt(j);
            x3[i3] = i3+1;

            i3++;
        }*/

        for (int w = 0; w < x_Q.length; w++) {
            x_temp_Q = (double) x_Q[w];
            y_temp_Q = (double) y_Q[w];
            series_Q.add("bar" + x_temp_Q, y_temp_Q);
        }

        /*for (int w = 0; w < x2.length; w++) {
            x2_temp = (double) x2[w];
            y2_temp = (double) y2[w];
            series2.add("bar" + x2_temp, y2_temp);
        }*/

        /*for (int w = 0; w < x3.length; w++) {
            x3_temp = (double) x3[w];
            y3_temp = (double) y3[w];
            series3.add("bar" + x3_temp, y3_temp);
        }*/

        dataset.addSeries(series_Q.toXYSeries());
        //dataset.addSeries(series2.toXYSeries());
        //dataset.addSeries(series3.toXYSeries());

        renderer_Q.setColor(Color.GREEN);
        renderer_Q.setPointStyle(PointStyle.SQUARE);
        renderer_Q.setFillPoints(true);

        //renderer2.setColor(Color.RED);
        //renderer2.setPointStyle(PointStyle.SQUARE);
        //renderer2.setFillPoints(true);
        //renderer2.setLineWidth(7);

        //renderer3.setColor(Color.RED);
        //renderer3.setPointStyle(PointStyle.SQUARE);
        //renderer3.setFillPoints(true);
        //renderer3.setLineWidth(7);

        mRenderer.addSeriesRenderer(renderer_Q);
        //mRenderer.addSeriesRenderer(renderer2);
        //mRenderer.addSeriesRenderer(renderer3);

        mRenderer.setChartTitle("Aufgabenergebnisse");
        mRenderer.setXTitle(nextAufgabe);
        mRenderer.setYTitle("Ergebniss");

        mRenderer.setPanEnabled(false, false);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.BLACK);
        mRenderer.setZoomEnabled(false, false);
        mRenderer.setBarSpacing(0.5);
        mRenderer.setYAxisMin(0);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        switch(metrics.densityDpi)
        {
            case DisplayMetrics.DENSITY_HIGH: //HDPI
                mRenderer.setChartTitleTextSize(40);
                mRenderer.setAxisTitleTextSize(30);
                mRenderer.setLabelsTextSize(40);
                System.out.println("HDPI");
                break;
            case DisplayMetrics.DENSITY_MEDIUM: //MDPI
                mRenderer.setChartTitleTextSize(40);
                mRenderer.setAxisTitleTextSize(30);
                mRenderer.setLabelsTextSize(40);
                System.out.println("MDPI");
                break;

            case DisplayMetrics.DENSITY_LOW:  //LDPI
                System.out.println("LDPI");
                break;
        }

        /* For single series */
        return ChartFactory.getBarChartView(context, dataset, mRenderer, BarChart.Type.DEFAULT);

        /* For multiple series */
        //String[] types = new String[]{BarChart.TYPE, LineChart.TYPE,LineChart.TYPE};
        //return ChartFactory.getCombinedXYChartView(context, dataset, mRenderer, types);
    }

    public static int evalQtest(String h) {
        Enumeration vEnumTest = MainActivity.vecTest.elements();
        String j = "";
        int count = 0;

        while(vEnumTest.hasMoreElements()) {
            j = vEnumTest.nextElement().toString();

            if (j.equals(h)){
                count++;
            }
        } // while

        return count;
    }

    /* This function generates the graph of the next Test */
    public void nextGraph(View view){
        /* Measures the quantitiy of tasks that the next Test has */
        nextAufgabe = MainActivity.vecTest.get(indx_graph).toString();
        sizeY = evalQtest(nextAufgabe);
        /* array to control which test is beeing displayed */
        indx_sTests++;
        sTests[indx_sTests] = nextAufgabe;

        int[] x_Qfwd = new int[sizeY];
        int[] y_Qfwd = new int[sizeY];
        int h = 0;
        int[] xw2;
        int[] yz2;
        int h2 = 0;

        yz2 = new int[sizeY];
        xw2 = new int[sizeY];

        //int i2 = indx_graph;

        while (h < sizeY) {
            j = MainActivity.vecQualifikation.get(indx_graph).toString();
            y_Qfwd[h] = Integer.parseInt(j);
            x_Qfwd[h] = h+1;

            indx_graph++;
            h++;
        } // while

        /*while (h2 < sizeY) {
            j = vecFeeling.get(i2).toString();
            yz2[h2] = Integer.parseInt(j);
            xw2[h2] = i2+1;

            i2++;
            h2++;
        } // while*/

        if (MainActivity.vecQualifikation.size() == indx_graph) {
            btnFwd.setEnabled(false);
        }
        if (indx_graph > 0) btnRew.setEnabled(true);

        series_Q.clear();
        //series2.clear();

        //_/_/TimeSeries series = new TimeSeries("Line1");
        //series = new CategorySeries("Tests");
        for (int w = 0; w < x_Qfwd.length; w++) {
            x_temp_Q = (double) x_Qfwd[w];
            y_temp_Q = (double) y_Qfwd[w];
            series_Q.add("bar" + x_temp_Q, y_temp_Q);
        }

        /*for (int w = 0; w < xw2.length; w++) {
            x_temp = (double) xw2[w];
            y_temp = (double) yz2[w];
            series2.add("bar" + x_temp, y_temp);
        }*/

        dataset.clear();
        dataset.addSeries(series_Q.toXYSeries());
        //dataset.addSeries(series2.toXYSeries());
        mRenderer.setXTitle(nextAufgabe);
        gView.repaint();
    }

    public void lastGraph(View view){
        int sizeY_prev = evalQtest(nextAufgabe);
        indx_sTests--;
        nextAufgabe = sTests[indx_sTests];
        sizeY = evalQtest(nextAufgabe);

        int[] x_Qrew = new int[sizeY];
        int[] y_Qrew = new int[sizeY];
        int h = 0;
        int[] xw2;
        int[] yz2;
        int h2 = 0;

        yz2 = new int[sizeY];
        xw2 = new int[sizeY];

        indx_graph = indx_graph - sizeY - sizeY_prev;
        //int i2 = indx_graph;

        if (indx_graph == 0) btnRew.setEnabled(false);

        while (h < sizeY) {
            j = MainActivity.vecQualifikation.get(indx_graph).toString();
            y_Qrew[h] = Integer.parseInt(j);
            x_Qrew[h] = h+1;

            indx_graph++;
            h++;
        } // while

        if (MainActivity.vecQualifikation.size() > indx_graph) {
            btnFwd.setEnabled(true);
        }

        /*while (h2 < sizeY) {
            j = vecFeeling.get(i2).toString();
            yz2[h2] = Integer.parseInt(j);
            xw2[h2] = i2+1;

            i2++;
            h2++;
        } // while*/

        series_Q.clear();
        //series2.clear();

        //_/_/TimeSeries series = new TimeSeries("Line1");
        //series = new CategorySeries("Tests");
        for (int w = 0; w < x_Qrew.length; w++) {
            x_temp_Q = (double) x_Qrew[w];
            y_temp_Q = (double) y_Qrew[w];
            series_Q.add("bar" + x_temp_Q, y_temp_Q);
        }

        /*for (int w = 0; w < xw2.length; w++) {
            x_temp = (double) xw2[w];
            y_temp = (double) yz2[w];
            series2.add("bar" + x_temp, y_temp);
        }*/

        dataset.clear();
        dataset.addSeries(series_Q.toXYSeries());
        //dataset.addSeries(series2.toXYSeries());
        mRenderer.setXTitle(nextAufgabe);
        gView.repaint();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
