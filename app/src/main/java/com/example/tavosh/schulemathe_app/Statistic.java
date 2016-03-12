package com.example.tavosh.schulemathe_app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

import java.util.Enumeration;
import java.util.Locale;

import android.view.View.OnTouchListener;
import android.widget.Toast;

import javax.xml.transform.Result;

/* Carlos Trujillo   29/09/2015                                           */
/* This class is used to create the statistic shown at the end of the app */
public class Statistic extends ActionBarActivity implements OnTouchListener {
    public String nextAufgabe;
    int indx_graph = 0;
    int sizeY;
    /* These variables stores the Qualification data to create the chart */
    int[] x_Q;
    int[] y_Q;
    double x_temp_Q;
    double y_temp_Q;
    // These variables stores the Empfindung data to create the chart
    int[] x_E;
    int[] y_E;
    double x_temp_E;
    double y_temp_E;
    String[] time_temp = new String[10];
    String language;

    String[] sTests = new String[10];
    int indx_sTests = 0;

    LinearLayout layout;
    GraphicalView gView;
    String j;
    XYSeriesRenderer renderer_Q = new XYSeriesRenderer();
    XYSeriesRenderer renderer_E = new XYSeriesRenderer();
    //XYSeriesRenderer renderer3 = new XYSeriesRenderer();
    CategorySeries series_Q = new CategorySeries("Tests");
    CategorySeries series_E; // Trujillo 07_03_2016
    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
    XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    Button btnRew;
    Button btnFwd;
    public final static String EXTRA_MESSAGE = "";

    Boolean timeGraph_boolean = false;
    Boolean ResultGraph_boolean = false;
    Boolean feelingGraph_boolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        try{
            nextAufgabe = MainActivity.vecTest.get(indx_graph).toString();
            series_E = new CategorySeries(getString(R.string.chartLineLegend)); // Trujillo 07_03_2016

            MainActivity.statisticData = true;

            /* displays the first values of the chart on the screen */
            layout = (LinearLayout) findViewById(R.id.BarGraph);
            gView = getViewFwd(this);
            layout.addView(gView);

            btnRew = (Button) findViewById(R.id.btnAktBack);
            btnFwd = (Button) findViewById(R.id.btnAktFwd);
            btnRew.setEnabled(false);

            LinearLayout lnrLayout = (LinearLayout) findViewById(R.id.BarGraph);

            lnrLayout.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    //Toast.makeText(getApplicationContext(),"OnTouchListener", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            gView.setOnTouchListener(this);
        } catch (Exception e){
            System.out.println("Error: Statistic.onCreate " + e);
        }
    } //onCreate

    /* This function generates all the values and format for the chart */
    public GraphicalView getViewFwd(Context context) {
        /* evaluates how many task has the TEST*/
        sizeY = evalQtest(nextAufgabe);

        if (MainActivity.vecTest.size() == sizeY) {
            btnFwd = (Button) findViewById(R.id.btnAktFwd);
            btnFwd.setEnabled(false);
        }

        //sTests[indx_sTests] = nextAufgabe + "," + sizeY;
        sTests[indx_sTests] = nextAufgabe;

        y_Q = new int[sizeY];
        x_Q = new int[sizeY];
        y_E = new int[sizeY];
        x_E = new int[sizeY];
        //y3 = new int[sizeY];
        //x3 = new int[sizeY];
        int i2 = indx_graph;
        //int i3 = indx_graph;

        while (indx_graph < sizeY) {
            j = MainActivity.vecQualifikation.get(indx_graph).toString();
            //time_temp[indx_graph] = MainActivity.vecTime.get(indx_graph).toString();
            y_Q[indx_graph] = Integer.parseInt(j);
            x_Q[indx_graph] = indx_graph+1;

            indx_graph++;
        } // while

        while (i2 < sizeY) {
            //j = MainActivity.vecEmpfindung.get(i2).toString();
            j = MainActivity.vecAvgUc.get(i2).toString();
            y_E[i2] = Integer.parseInt(j);
            x_E[i2] = i2+1;

            i2++;
        }

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

        for (int w = 0; w < x_E.length; w++) {
            x_temp_E = (double) x_E[w];
            y_temp_E = (double) y_E[w];
            series_E.add("line" + x_temp_E, y_temp_E);
        }

        /*for (int w = 0; w < x3.length; w++) {
            x3_temp = (double) x3[w];
            y3_temp = (double) y3[w];
            series3.add("bar" + x3_temp, y3_temp);
        }*/

        dataset.addSeries(series_Q.toXYSeries());
        dataset.addSeries(series_E.toXYSeries());
        //dataset.addSeries(series3.toXYSeries());

        renderer_Q.setColor(Color.GREEN);
        renderer_Q.setPointStyle(PointStyle.SQUARE);
        renderer_Q.setFillPoints(true);

        renderer_E.setColor(Color.YELLOW);
        renderer_E.setPointStyle(PointStyle.SQUARE);
        renderer_E.setFillPoints(true);

        //renderer3.setColor(Color.RED);
        //renderer3.setPointStyle(PointStyle.SQUARE);
        //renderer3.setFillPoints(true);
        //renderer3.setLineWidth(7);

        mRenderer.addSeriesRenderer(renderer_Q);
        mRenderer.addSeriesRenderer(renderer_E);
        //mRenderer.addSeriesRenderer(renderer3);
        mRenderer.setXTitle(nextAufgabe);

        // Sets the tittle fo the chart depending on the language
        /*language = Locale.getDefault().getLanguage().toString();
        switch (language) {
            case "en":
                mRenderer.setChartTitle("Results for " + nextAufgabe);
                mRenderer.setYTitle("Result");
                break;
            case "de":
                break;
        }*/
        mRenderer.setChartTitle(getString(R.string.chartResultTitle) + " " + nextAufgabe);
        mRenderer.setYTitle(getString(R.string.yResultTitle));

        mRenderer.setPanEnabled(false, false);
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.BLACK);
        mRenderer.setZoomEnabled(false, false);
        mRenderer.setBarSpacing(0.5);
        mRenderer.setYAxisMin(0);

        // Sets fonts size according to the definition of the device
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        switch(metrics.densityDpi)
        {
            case 640:
                mRenderer.setChartTitleTextSize(50);
                mRenderer.setAxisTitleTextSize(50);
                mRenderer.setLabelsTextSize(50);
                mRenderer.setMargins(new int[]{80, 80, 80, 80});
                mRenderer.setLegendTextSize(50); // Trujillo 07_03_2016
                renderer_E.setLineWidth(8);
                break;
            case 480:
                mRenderer.setChartTitleTextSize(40);
                mRenderer.setAxisTitleTextSize(40);
                mRenderer.setLabelsTextSize(40);
                mRenderer.setMargins(new int[]{65, 60, 60, 60});
                mRenderer.setLegendTextSize(40); // Trujillo 07_03_2016
                renderer_E.setLineWidth(7);
                break;
            case 320:
                mRenderer.setChartTitleTextSize(30);
                mRenderer.setAxisTitleTextSize(30);
                mRenderer.setLabelsTextSize(30);
                mRenderer.setMargins(new int[]{50, 50, 50, 50});
                mRenderer.setLegendTextSize(30); // Trujillo 07_03_2016
                renderer_E.setLineWidth(6);
                break;
            case 240:
                mRenderer.setChartTitleTextSize(20);
                mRenderer.setAxisTitleTextSize(20);
                mRenderer.setLabelsTextSize(20);
                mRenderer.setMargins(new int[]{40, 40, 40, 40});
                mRenderer.setLegendTextSize(20); // Trujillo 07_03_2016
                renderer_E.setLineWidth(5);
                break;
            case 213:
                mRenderer.setChartTitleTextSize(10);
                mRenderer.setAxisTitleTextSize(10);
                mRenderer.setLabelsTextSize(10);
                mRenderer.setMargins(new int[]{30, 30, 30, 30});
                mRenderer.setLegendTextSize(10); // Trujillo 07_03_2016
                renderer_E.setLineWidth(4);
                break;
            case 160: //MDPI
                mRenderer.setChartTitleTextSize(10);
                mRenderer.setAxisTitleTextSize(10);
                mRenderer.setLabelsTextSize(10);
                mRenderer.setMargins(new int[]{20, 20, 20, 20});
                mRenderer.setLegendTextSize(10); // Trujillo 07_03_2016
                renderer_E.setLineWidth(3);
                break;
            case 120:  //LDPI
                mRenderer.setMargins(new int[]{ 20, 10, 20, 10 });
                mRenderer.setLegendTextSize(10); // Trujillo 07_03_2016
                renderer_E.setLineWidth(2);
                break;
        }

        /* For single series */
        //return ChartFactory.getBarChartView(context, dataset, mRenderer, BarChart.Type.DEFAULT);

        timeGraph_boolean = true;
        ResultGraph_boolean = false;
        feelingGraph_boolean = false;

        /* For multiple series */
        String[] types = new String[]{BarChart.TYPE, LineChart.TYPE};
        return ChartFactory.getCombinedXYChartView(context, dataset, mRenderer, types);
    } //getViewFwd

    /* Returns the quantity of task that a Test has */
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
        System.out.println("--> nextGraph");
        /* Measures the quantitiy of tasks that the next Test has */
        nextAufgabe = MainActivity.vecTest.get(indx_graph).toString();
        sizeY = evalQtest(nextAufgabe);
        /* array to control which test is beeing displayed */
        indx_sTests++;
        sTests[indx_sTests] = nextAufgabe;

        int[] x_Qfwd = new int[sizeY];
        int[] y_Qfwd = new int[sizeY];
        int h = 0;
        int[] x_Efwd = new int[sizeY];
        int[] y_Efwd = new int[sizeY];
        int h2 = 0;

        int i2 = indx_graph;
        time_temp = new String[10];

        while (h < sizeY) {
            j = MainActivity.vecQualifikation.get(indx_graph).toString();
            //time_temp[h] = MainActivity.vecTime.get(indx_graph).toString();
            y_Qfwd[h] = Integer.parseInt(j);
            x_Qfwd[h] = h+1;

            indx_graph++;
            h++;
        } // while

        while (h2 < sizeY) {
            //j = MainActivity.vecEmpfindung.get(i2).toString();
            j = MainActivity.vecAvgUc.get(i2).toString();
            y_Efwd[h2] = Integer.parseInt(j);
            x_Efwd[h2] = i2+1;

            i2++;
            h2++;
        } // while

        if (MainActivity.vecQualifikation.size() == indx_graph) {
            btnFwd.setEnabled(false);
        }
        if (indx_graph > 0) btnRew.setEnabled(true);

        series_Q.clear();
        series_E.clear();

        //_/_/TimeSeries series = new TimeSeries("Line1");
        //series = new CategorySeries("Tests");
        for (int w = 0; w < x_Qfwd.length; w++) {
            x_temp_Q = (double) x_Qfwd[w];
            y_temp_Q = (double) y_Qfwd[w];
            series_Q.add("bar" + x_temp_Q, y_temp_Q);
        }

        for (int w = 0; w < x_Efwd.length; w++) {
            x_temp_E = (double) x_Efwd[w];
            y_temp_E = (double) y_Efwd[w];
            series_E.add("line" + x_temp_E, y_temp_E);
        }

        dataset.clear();
        dataset.addSeries(series_Q.toXYSeries());
        dataset.addSeries(series_E.toXYSeries());
        mRenderer.setXTitle(nextAufgabe);

        // Set the name or the graph at the top of it
       /* switch (language) {
            case "en":
                mRenderer.setChartTitle("Results for " + nextAufgabe);
                break;
            case "de":
                mRenderer.setChartTitle("Ergebniss für " + nextAufgabe);
                break;
        }*/
        mRenderer.setChartTitle(getString(R.string.chartTitle) + " " + nextAufgabe);

        timeGraph_boolean = true;
        ResultGraph_boolean = false;
        feelingGraph_boolean = false;

        gView.repaint();
    } //nextGraph

    /* This function generates the graph of the previous Test */
    public void lastGraph(View view){
        System.out.println("-->lastGraph");

        try {
            int sizeY_prev = evalQtest(nextAufgabe);
            indx_sTests--;
            nextAufgabe = sTests[indx_sTests];
            sizeY = evalQtest(nextAufgabe);

            int[] x_Qrew = new int[sizeY];
            int[] y_Qrew = new int[sizeY];
            int h = 0;
            int[] x_Erew = new int[sizeY];
            int[] y_Erew = new int[sizeY];
            int h2 = 0;

            indx_graph = indx_graph - sizeY - sizeY_prev;
            int i2 = indx_graph;
            //time_temp = new String[10];

            if (indx_graph == 0) btnRew.setEnabled(false);

            while (h < sizeY) {
                j = MainActivity.vecQualifikation.get(indx_graph).toString();
                //time_temp[h] = MainActivity.vecTime.get(indx_graph).toString();
                y_Qrew[h] = Integer.parseInt(j);
                x_Qrew[h] = h + 1;

                indx_graph++;
                h++;
            } // while

            if (MainActivity.vecQualifikation.size() > indx_graph) {
                btnFwd.setEnabled(true);
            }

            while (h2 < sizeY) {
                //j = MainActivity.vecEmpfindung.get(i2).toString();
                j = MainActivity.vecAvgUc.get(i2).toString();
                y_Erew[h2] = Integer.parseInt(j);
                x_Erew[h2] = i2 + 1;

                i2++;
                h2++;
            } // while

            series_Q.clear();
            series_E.clear();

            //_/_/TimeSeries series = new TimeSeries("Line1");
            //series = new CategorySeries("Tests");
            for (int w = 0; w < x_Qrew.length; w++) {
                x_temp_Q = (double) x_Qrew[w];
                y_temp_Q = (double) y_Qrew[w];
                series_Q.add("bar" + x_temp_Q, y_temp_Q);
            }

            for (int w = 0; w < x_Erew.length; w++) {
                x_temp_E = (double) x_Erew[w];
                y_temp_E = (double) y_Erew[w];
                series_E.add("line" + x_temp_E, y_temp_E);
            }

            dataset.clear();
            dataset.addSeries(series_Q.toXYSeries());
            dataset.addSeries(series_E.toXYSeries());
            mRenderer.setXTitle(nextAufgabe);

            // Set the name or the graph at the top of it
            mRenderer.setChartTitle(getString(R.string.chartTitle) + " " + nextAufgabe);

            gView.repaint();
        } catch (Exception e) {
            System.out.println("Error: TestAufgabe.lastgraph " + e);
        }

        timeGraph_boolean = true;
        ResultGraph_boolean = false;
        feelingGraph_boolean = false;
    } //lastGraph

    public void correctnessGraph() {
        System.out.println("--> correctnessGraph");
        try {
            int sizeY_temp = evalQtest(nextAufgabe);

            float[] x_Corr = new float[sizeY_temp];
            float[] y_Corr = new float[sizeY_temp];
            int h = 0;
            float[] x_Lim  = new float[sizeY_temp];
            float[] y_Lim = new float[sizeY_temp];
            int h2 = 0;
            int minutes;
            int seconds;
            String sSeconds;
            String sMinutes;
            int posMin;

            int indx_temp = indx_graph - sizeY_temp;
            int i2 = indx_temp;

            while (h < sizeY_temp) {
                j = MainActivity.vecQualifikation.get(indx_temp).toString();
                y_Corr[h] = Integer.parseInt(j);
                x_Corr[h] = h + 1;

                indx_temp++;
                h++;
            } // while

            while (h2 < sizeY) {
                j = MainActivity.vecAvgUc.get(i2).toString();
                y_Lim[h2] = Integer.parseInt(j);
                x_Lim[h2] = h2 + 1;

                i2++;
                h2++;
            } // while

            series_Q.clear();
            series_E.clear();

            for (int w = 0; w < x_Corr.length; w++) {
                x_temp_Q = (double) x_Corr[w];
                y_temp_Q = (double) y_Corr[w];
                series_Q.add("bar" + x_temp_Q, y_temp_Q);
            }

            for (int w = 0; w < x_Lim.length; w++) {
                x_temp_E = (double) x_Lim[w];
                y_temp_E = (double) y_Lim[w];
                series_E.add("line" + x_temp_E, y_temp_E);
            }

            dataset.clear();
            dataset.addSeries(series_Q.toXYSeries());
            dataset.addSeries(series_E.toXYSeries());

            mRenderer.setXTitle(nextAufgabe);
            mRenderer.setChartTitle(getString(R.string.chartTitle) + " " + nextAufgabe);
            mRenderer.setYTitle(getString(R.string.yResultTitle));

            gView.repaint();
        } catch (Exception e) {
            System.out.println("Error: Statistic.timeGraph " + e);
        }
        timeGraph_boolean = true;
        ResultGraph_boolean = false;
        feelingGraph_boolean = false;
    } // correctnessGraph

    public void timeGraph() {
        System.out.println("--> timeGraph");
        try {
            int sizeY_temp = evalQtest(nextAufgabe);
            //int indx_sTest_temp = indx_sTests -1;
            //nextAufgabe = sTests[indx_sTest_temp];
            //int sizeY_temp = evalQtest(nextAufgabe);

            float[] x_Time = new float[sizeY_temp];
            float[] y_Time = new float[sizeY_temp];
            int h = 0;
            float[] x_Lim = new float[sizeY_temp];
            float[] y_Lim = new float[sizeY_temp];
            int h2 = 0;
            int minutes;
            int seconds;
            String sSeconds;
            String sMinutes;
            int posMin;
            float y_Time_Temp;
            float j_temp;

            int indx_temp = indx_graph - sizeY_temp;
            int i2 = indx_temp;

            while (h < sizeY_temp) {
                j = MainActivity.vecTime.get(indx_temp).toString();
                j_temp = Float.parseFloat(MainActivity.vecTimeTask.get(indx_temp).toString()); // Trujillo 06_03_2016
                posMin = j.lastIndexOf(":");
                sMinutes = j.substring(0,posMin);
                //minutes = Integer.parseInt(j.substring(0,posMin));
                sSeconds = j.substring(j.lastIndexOf(":") + 1);
                seconds = ((Integer.parseInt(sSeconds))*100)/60;
                y_Time_Temp = Float.parseFloat(sMinutes + "." + seconds); // Trujillo 06_03_2016
                y_Time[h] = j_temp - y_Time_Temp; // Trujillo 06_03_2016
                x_Time[h] = h + 1;

                indx_temp++;
                h++;
            } // while

            while (h2 < sizeY) {
                j = MainActivity.vecTime4taskUt.get(i2).toString();
                j_temp = Float.parseFloat(MainActivity.vecTimeTask.get(indx_temp).toString()); // Trujillo 06_03_2016


                //Aufgabe j_Auf = MainActivity.aufgb2Eval.get(0);

                //j = j_Auf.getTime();
                //posMin = j.lastIndexOf(":");
                //minutes = Integer.parseInt(j.substring(0,posMin));
                //sSeconds = j.substring(j.lastIndexOf(":") + 1);
                //seconds = ((Integer.parseInt(sSeconds))*100)/60;

                if (j.equals("")){
                    j = "0.0";
                }
                y_Lim[h2] = j_temp - Float.parseFloat(j);
                x_Lim[h2] = h2 + 1;

                i2++;
                h2++;
            } // while

            series_Q.clear();
            series_E.clear();

            for (int w = 0; w < x_Time.length; w++) {
                x_temp_Q = (double) x_Time[w];
                y_temp_Q = (double) y_Time[w];
                series_Q.add("bar" + x_temp_Q, y_temp_Q);
            }

            for (int w = 0; w < x_Lim.length; w++) {
                x_temp_E = (double) x_Lim[w];
                y_temp_E = (double) y_Lim[w];
                series_E.add("line" + x_temp_E, y_temp_E);
            }

            dataset.clear();
            dataset.addSeries(series_Q.toXYSeries());
            dataset.addSeries(series_E.toXYSeries());

            mRenderer.setXTitle(getString(R.string.xTimeTitle) + " " + nextAufgabe);
            mRenderer.setChartTitle(getString(R.string.chartTimeTitle) + " " + nextAufgabe);
            mRenderer.setYTitle(getString(R.string.yTimeTitle));

            gView.repaint();
        } catch (Exception e) {
            System.out.println("Error: Statistic.timeGraph " + e);
        }
        timeGraph_boolean = false;
        ResultGraph_boolean = false;
        feelingGraph_boolean = true;
    } // timeGraph

    public void fellingGraph() {
        System.out.println("--> feelingGraph");
        try {
            int sizeY_temp = evalQtest(nextAufgabe);
            //int indx_sTest_temp = indx_sTests -1;
            //nextAufgabe = sTests[indx_sTest_temp];
            //int sizeY_temp = evalQtest(nextAufgabe);

            float[] x_Feel = new float[sizeY_temp];
            float[] y_Feel = new float[sizeY_temp];
            int h = 0;
            float[] x_Lim = new float[sizeY_temp];
            float[] y_Lim = new float[sizeY_temp];
            int h2 = 0;
            //int minutes;
            //int seconds;
            //String sSeconds;
            //String sMinutes;
            //int posMin;

            int indx_temp = indx_graph - sizeY_temp;
            int i2 = indx_temp;
            int y_Feel_temp;

            while (h < sizeY_temp) {
                j = MainActivity.vecEmpfindung.get(indx_temp).toString();
                //posMin = j.lastIndexOf(":");
                //sMinutes = j.substring(0,posMin);
                //minutes = Integer.parseInt(j.substring(0,posMin));
                //sSeconds = j.substring(j.lastIndexOf(":") + 1);
                //seconds = ((Integer.parseInt(sSeconds))*100)/60;
                y_Feel_temp = Integer.parseInt(j);
                y_Feel[h] = y_Feel_temp;
                x_Feel[h] = h + 1;

                if (y_Feel_temp < 0) {
                    if ((y_Feel_temp == -1) && (mRenderer.getYAxisMin() == 0)) {
                        mRenderer.setYAxisMin(-1);
                    } else if (y_Feel_temp == -2) {
                        mRenderer.setYAxisMin(-2);
                    }
                }

                indx_temp++;
                h++;
            } // while

            while (h2 < sizeY) {
                j = MainActivity.vecEmpUf.get(i2).toString();

                y_Lim[h2] = Integer.parseInt(j);
                x_Lim[h2] = h2 + 1;

                i2++;
                h2++;
            } // while

            series_Q.clear();
            series_E.clear();

            for (int w = 0; w < x_Feel.length; w++) {
                x_temp_Q = (double) x_Feel[w];
                y_temp_Q = (double) y_Feel[w];
                series_Q.add("bar" + x_temp_Q, y_temp_Q);
            }

            for (int w = 0; w < x_Lim.length; w++) {
                x_temp_E = (double) x_Lim[w];
                y_temp_E = (double) y_Lim[w];
                series_E.add("line" + x_temp_E, y_temp_E);
            }

            dataset.clear();
            dataset.addSeries(series_Q.toXYSeries());
            dataset.addSeries(series_E.toXYSeries());

            mRenderer.setXTitle(getString(R.string.xFeelTitle) + " " + nextAufgabe);
            mRenderer.setChartTitle(getString(R.string.chartFeelTitle) + " " + nextAufgabe);
            mRenderer.setYTitle(getString(R.string.yFeelTitle));

            gView.repaint();
        } catch (Exception e) {
            System.out.println("Error: Statistic.feelingGraph " + e);
        }

        timeGraph_boolean = false;
        ResultGraph_boolean = true;
        feelingGraph_boolean = false;
    } // feelingGraph

    /* Returns to the first screen */
    public void back2Start(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        //sends the name of which layout to use to the new intent
        int xmlScreen = R.layout.activity_main;
        intent.putExtra(EXTRA_MESSAGE, xmlScreen);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            //Goes back to the first screen when the android back button is pressed
            Intent intent = new Intent(this, MainActivity.class);
            int xmlScreen = R.layout.activity_main;
            intent.putExtra(EXTRA_MESSAGE, xmlScreen);
            startActivity(intent);
            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
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

    public boolean onTouch(View v, MotionEvent event) {
        // Shows the next graph (Time, feeling or back to ergebniss) when the graph is touched
        // Carlos Trujillo 08.11.2015
        System.out.println("--> OnTouch");

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (timeGraph_boolean) {
                    mRenderer.setYAxisMin(0);
                    timeGraph();
                } else if(ResultGraph_boolean) {
                    mRenderer.setYAxisMin(0);
                    correctnessGraph();
                } else if (feelingGraph_boolean) {
                    mRenderer.setYAxisMin(0);
                    fellingGraph();
                }
                //Toast.makeText(this, "ACTION_DOWN", Toast.LENGTH_SHORT) .show();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //Toast.makeText(this, "ACTION_POINTER_DOWN", Toast.LENGTH_SHORT) .show();
                break;
            case MotionEvent.ACTION_UP:
                //Toast.makeText(this, "ACTION_UP", Toast.LENGTH_SHORT) .show();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //Toast.makeText(this, "ACTION_POINTER_UP", Toast.LENGTH_SHORT) .show();
                break;
            case MotionEvent.ACTION_MOVE:
                //Toast.makeText(this, "ACTION_MOVE", Toast.LENGTH_SHORT) .show();
                break;
        }

        return true; // indicate event was handled
    }
}
