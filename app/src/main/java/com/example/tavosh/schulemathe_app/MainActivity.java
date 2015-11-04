package com.example.tavosh.schulemathe_app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public Intent intent;
    public static ArrayList<Aufgabe> aufgb2Eval = null; // List of Aufgabe to evaluate
    public static ArrayList<Pool> poolAfgb2Eval = null; // List of PoolAufgabe to evaluate
    public Vector vecInstruction = new Vector<String>();
    public static Vector vecQualifikation = new Vector<String>();// records each score of each task in order to create the statistic at the end
    public static Vector vecTest = new Vector<String>(); // records teh number of each task, works together with VecQualification to create the statistics.
    public static Vector vecEmpfindung = new Vector<String>(); // records each empfindung of each task in order to create the statistic at the end
    public static Vector vecTime = new Vector<String>(); // records each time of each task in order to create the time statistic at the end (time of the user)
    public static Vector vecTime4task = new Vector<String>(); // records each time of each task in order to create the time statistic at the end (time of the xml)
    public static boolean poolActivated = false; // When the score is too low, the next test will be from the Pool

    public static int sumQualfktion = 0; // Sums the qualification for each Test or pool.
    public static int n_QntityAufEval = 0; // Indicates how many Aufgabe have been evaluated from the list aufgb2Eval
    public static int n_poolQntAufEvl = 0; // Indicates how many poolAufgabe have been evaluated from the list poolAfg2Eval
    public static int n_Aufgbe = 0; // Indicates the number of the currently executing Aufgabe
    public static int n_poolAufgb = 0; // Indicates the number of the currently executing Aufgabe from the pool
    public static int n_Test = 0; // Indicates the number of the currently executing Test
    public static int n_poolTest = 0; // Indicates the number of the currently executing Pool
    public static Aufgabe aufLoad = new Aufgabe();
    public static Pool aufPoolLoad = new Pool();
    public static long averageQualfktion = 0; //Sets the Average qualification for every test or pool

    private static boolean done = false;
    private static String currentTag = null;
    private static Aufgabe currentAufgabe = null;

    static boolean helpOn = false;
    static String helpValue = null;
    public static boolean readNextTest = false; // It becomes true when the Next Test xml needs to be read
    public static boolean readNextPoolTest = false; // It becomes true when the Next PoolTest xml need to be read
    public static boolean starts2ndPool = false; // when the first pool was not executed then the ArrayList Pool needs to be created
    public static boolean startsFromSavedInfo = false; // when the app is open with already saved data this variable turns to true
    public static boolean startsFromSavedPoolInfo = true; // when the app writes the first pool task in the scrren this variable turns to false
    public static boolean statisticData = false; // If there is no data to show in the statistic --> false

    // names of the XML tags for test files
    static final String AUFGABEN = "aufgaben";
    static final String AUFGABE = "aufgabe";
    static final String BILD = "bild";
    static final String HILFE = "hilfe";
    static final String VALUE = "value";
    static final String LOESUNG = "loesung";
    static final String TEXT = "text";
    static final String TIME = "time";

    private static final int OFF_TOPIC = 0;
    static final int xml01 = R.xml.test01;
    static final int xml02 = R.xml.test02;
    static final int xml03 = R.xml.test03;

    //static final int xml01 = R.xml.test01short; // Short version of test 1 (just for tests)
    //static final int xml02 = R.xml.test02short; // Short version of test 2 (just for tests)
    //static final int xml03 = R.xml.test03short; // Short version of test 3 (just for tests)

    static final int xmlIntro = R.xml.intro;
    static final int xmlPool01 = R.xml.pool01;
    static final int xmlPool02 = R.xml.pool02;

    int valuesXML;
    static final int score2pool = 80; // when the Test result > score2pool --> go to next Test if not --> go to the Pool
    static final int qntPoolAufgaben = 40; // When Test result < qntPoolAufgaben --> 3 Aufgaben from the Pool
    // When Test result > qntPoolAufgaben --> 2 Aufgaben from the Pool
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (statisticData) {
            Button btnStatistic;
            btnStatistic =  (Button) findViewById(R.id.btnStatistic);
            btnStatistic.setEnabled(true);
        }

        call2parseIntro();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void CallFirstTask (View view) {
        // Opens the screen that shows the tasks
        System.out.println("--> CallFistTask");

        n_Aufgbe = 0; //the first Aufgabe is going to be evaluated
        n_Test = 1; // the first test is going to be evaluated

        //starts the XML parsing
        Context contextParse = this;
        parse(contextParse, xml01, n_Test);
        readXMLQntTest();

        try{
            Intent intent = new Intent(this, TestAufgabe.class);

            //sends the name of which layout to use to the new intent
            int xmlScreen = R.layout.activity_test_aufgabe;
            intent.putExtra(EXTRA_MESSAGE, xmlScreen);

            startActivity(intent);
        } catch (Exception e) {
            System.out.println("ERROR 1: MainActivity.java CallFirstTask --> " + e);
        }
    } // CallFirstTask

    public void call2parseIntro() {
        // Calls the parser that reads the introduction info
        System.out.println("--> call2parseIntro");

        int cont = 1;
        int nextStep = 0;
        TextView textStep;
        TextView textIntro;
        String strStep;

        try{
            Context context = this;
            vecInstruction = XmlParser.parseIntro(context, xmlIntro);

            Enumeration vEnum = vecInstruction.elements();
            textIntro = (TextView) findViewById(R.id.intro);
            textIntro.setText("" + vEnum.nextElement());

            while(vEnum.hasMoreElements()) {
                strStep = "step" + cont;

                nextStep = getResources().getIdentifier(strStep , "id", getPackageName());
                textStep = (TextView) findViewById(nextStep);
                textStep.setText(cont + ". " + vEnum.nextElement().toString());

                cont++;
            } // while
        } catch (Exception e) {
            System.out.println("ERROR 3: MainActivity.java call2parseIntro --> " + e);
        } // try
    } // call2parseIntro

    public void parse(Context context, int xmlEval, int testNumbr) {
        // Parse the information from the XML that contains the different Tasks (Aufgaben)
        System.out.println("--> parse");

        //Context context = this.getBaseContext();
        XmlPullParser parser = context.getResources().getXml(xmlEval);
        //XmlPullParser parser = Xml.newPullParser(); //_/_/_/
        //FileInputStream fin = null; //_/_/_/

        try{
            //fin = openFileInput("test01"); //_/_/_/
            //parser.setInput(fin, "UTF-8"); //_/_/_/

            int eventType = parser.getEventType();
            int aufgabeNumber = 0;

            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        aufgb2Eval = new ArrayList<Aufgabe>();
                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(AUFGABE)) {
                            currentAufgabe = new Aufgabe();
                            aufgabeNumber++;
                            currentAufgabe.setAufgabe(aufgabeNumber);
                            /*System.out.println("Attribute Name " + parser.getAttributeName(0));
                            System.out.println("Attribute Value " + parser.getAttributeValue(0));
                            System.out.println("getAttributeCount " + parser.getAttributeCount());
                            System.out.println("ColumnNumber " + parser.getColumnNumber());
                            System.out.println("aufgabeNumber " + aufgabeNumber);*/

                        } else if (currentAufgabe != null) {
                            if (currentTag.equalsIgnoreCase(BILD)) {
                                currentAufgabe.setImageAufgabe(parser.nextText());

                            } else if (currentTag.equalsIgnoreCase(HILFE)) {
                                helpOn = true;
                                eventType = parser.next();
                                currentTag = parser.getName();

                                // Loop to read the different values that the tag HILFE could have
                                while (currentTag.equalsIgnoreCase(VALUE)) {
                                    helpValue = parser.nextText();

                                    if (helpValue.length() > 0){
                                        currentAufgabe.setHilfe(helpValue);
                                    }

                                    eventType = parser.next();
                                    currentTag = parser.getName();
                                } // end while

                            } else if (currentTag.equalsIgnoreCase(LOESUNG)) {
                                currentAufgabe.setImageLoesung(parser.nextText());
                            } else if (currentTag.equalsIgnoreCase(TEXT)) {
                                currentAufgabe.setText(parser.nextText());
                            } else if (currentTag.equalsIgnoreCase(TIME)) {
                                currentAufgabe.setTime(parser.nextText());
                            } // if
                        } // if
                        break;

                    case XmlPullParser.END_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(AUFGABE) && currentAufgabe != null) {
                            // add the current Aufgabe (class Aufgabe) to the Aufgabe List
                            currentAufgabe.setZustand(0); //Standby
                            currentAufgabe.setTest(testNumbr);
                            aufgb2Eval.add(currentAufgabe);
                        } else if (currentTag.equalsIgnoreCase(AUFGABEN)) {
                            done = true;
                        }
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch (Exception e) {
            System.out.println("ERROR ???: MainActivity.java parse --> " + e);
        } // try
    } // parse

    public void exitSystem() {
        // Close the App
        System.out.println("--> exitSystem");
        //finish();
        //System.exit(OFF_TOPIC);
        Intent intent = new Intent(Intent.ACTION_MAIN); finish();
    } // exitSystem

    public void readXMLQntTest() {
        // Reads the values saved in the memory in a .xml file
        // to prepare the quantity of values that the array will get
        // aufgabeValues.xml --> saves the values for the normal tests
        // poolValues.xml --> saves the values for the pool
        System.out.println("--> readXML");

        FileInputStream fin = null;
        try {
            fin = openFileInput("aufgabeValues.xml");
            XmlParser.parseXmlAufQntTest(fin);
            fin.close();

            fin = openFileInput("poolValues.xml");
            XmlParser.parseXmlPoolQntTest(fin);
            fin.close();
        } catch (Exception e) {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    } // readXML

    public void clearFiles(View view){
        System.out.println("--> clearFiles");
        Context context = MainActivity.this;

        try {
            File myFile = new File("/sdcard/aufgabeValues.xml");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter OSW = new OutputStreamWriter(fOut);
            OSW.append("");

            OSW.close();
            fOut.close();

            FileOutputStream fOutCntxt = null;
            fOutCntxt = context.openFileOutput("aufgabeValues.xml", Context.MODE_PRIVATE);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOutCntxt);

            myOutWriter.append("");
            myOutWriter.close();
            fOutCntxt.close();
            //-------------------------------------------------------------------
            File myFile2 = new File("/sdcard/poolValues.xml");
            myFile2.createNewFile();
            FileOutputStream fOut2 = new FileOutputStream(myFile2);
            OutputStreamWriter OSW2 = new OutputStreamWriter(fOut2);
            OSW2.append("");

            OSW.close();
            fOut.close();

            FileOutputStream fOutCntxt2 = null;
            fOutCntxt2 = context.openFileOutput("poolValues.xml", Context.MODE_PRIVATE);
            OutputStreamWriter myOutWriter2 = new OutputStreamWriter(fOutCntxt2);

            myOutWriter2.append("");
            myOutWriter2.close();
            fOutCntxt2.close();
        } catch (FileNotFoundException e) {
            System.out.println("Exception " + e);
        } catch (Exception e) {
            System.out.println("Exception " + e);
        }


        System.out.println("--> CallFistTask");

        n_Aufgbe = 0; //the first Aufgabe is going to be evaluated
        n_Test = 1; // the first test is going to be evaluated

        //starts the XML parsing
        Context contextParse = this;
        parse(contextParse, xml01, n_Test);
        readXMLQntTest();

        try{
            Intent intent = new Intent(this, TestAufgabe.class);

            //sends the name of which layout to use to the new intent
            int xmlScreen = R.layout.activity_test_aufgabe;
            intent.putExtra(EXTRA_MESSAGE, xmlScreen);

            startActivity(intent);
        } catch (Exception e) {
            System.out.println("ERROR 1: MainActivity.java CallFirstTask --> " + e);
        }

    } // clearFiles

    /* Shows the current statistic */
    public void ShowStatistic(View view) {
        Intent intent = new Intent(this, Statistic.class);

        //sends the name of which layout to use to the new intent
        int xmlScreen = R.layout.activity_statistic;
        intent.putExtra(EXTRA_MESSAGE, xmlScreen);
        startActivity(intent);
        finish();
    }

    public void call2read(View view) {
        System.out.println("--> call2read");

        FileInputStream fin = null;
        try {
            fin = openFileInput("aufgabeValues.xml");
            XmlParser.lectorDarchivos(fin);
            fin.close();

            fin = openFileInput("poolValues.xml");
            XmlParser.lectorDarchivos(fin);
            fin.close();
        } catch (Exception e) {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void call2WriteClasses(View view) {
        TestAufgabe.writeSystemOutAufgabeXML();
    }
} // MainActivity

