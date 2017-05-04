package com.lern.schulmathe_app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Xml;
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
    public static Vector vecEmpUf = new Vector<String>(); // records each empfindung of each task in order to create the Empfindug statistic (Up average line)
    public static Vector vecTime = new Vector<String>(); // records each time of each task in order to create the time statistic at the end (time of the user)
    public static Vector vecTimeTask = new Vector<String>(); // records the assigned time for each time, so it can be substract from the time made by the student
    public static Vector vecTime4taskUt = new Vector<String>(); // records each time of each task in order to create the time statistic at the end (time of the xml)
    public static Vector vecAvgUc = new Vector<String>(); //records each avgUc of each task to compare this values with vecQualifikation in the statistic
    public static Vector vectorIntroTitle = new Vector<String>();

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
    public static int countAuf = 0;  // Counts how many Aufgabe activities are stored in the XML in memory
    public static int countPool = 0; // Counts how many pool activities are stored in the XML in memory

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
    public static boolean xmlMemoryReadAuf = true; // Evita que el archivo que guarda los resultados en memoria sea leído dos veces
    public static boolean xmlMemoryReadPool = true; // Evita que el archivo que guarda los resultados en memoria sea leído dos veces
    private String timeTemp; // Trujillo 06/03/2016

    public static boolean pool01Completed = false;
    public static boolean pool02Completed = false;

    // names of the XML tags for test files
    static final String AUFGABEN = "aufgaben";
    static final String AUFGABE = "aufgabe";
    static final String BILD = "bild";
    static final String HILFE = "hilfe";
    static final String VALUE = "value";
    static final String LOESUNG = "loesung";
    static final String TEXT = "text";
    static final String TIME = "time";

    //for the statistik Lando Eisenmann!!!
    static final String AVGUC = "avguc";//avarage upper correctness
    static final String AVGDC = "avgdc";//avarage downer correctness
    static final String AVGUF = "avguf";//
    static final String AVGDF = "avgdf";
    static final String AVGUT = "avgut";
    static final String AVGDT = "avgdt";

    static final String TEST = "test";

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
    // Variables for configuration
    static final int score2pool = 80; // when the Test result > score2pool --> go to next Test if not --> go to the Pool
    static final int qntPoolAufgaben = 40; // When Test result < qntPoolAufgaben --> 3 Aufgaben from the Pool
    public static int qntAufTest1 = 5;
    public static int qntAufTest2 = 5;
    public static int qntAufTest3 = 5;
    // When Test result > qntPoolAufgaben --> 2 Aufgaben from the Pool

    Button btnStatistic, btnWeiter, btnNew;

    // Variables are use to have a control of which Tests or Pools where already loaded in the App
    public static Boolean parseNextxml02 = false;
    public static Boolean parseNextxml03 = false;
    public static Boolean parsePool01 = false;
    public static Boolean parsePool02 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (statisticData) {
            btnStatistic =  (Button) findViewById(R.id.btnStatistic);
            btnStatistic.setEnabled(true);

            btnWeiter = (Button) findViewById(R.id.btnWeiter);
            btnWeiter.setEnabled(false);
        }

        call2parseIntro();
        activateBtnStatistic();  // Verifies if the "Statistic"  and "forward" bottons should be enabled or not
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
        int nextStepTitle = 0;
        TextView textStep;
        TextView textIntro;
        TextView textTitle;
        String strStep, strStepTitle;

        try{
            Context context = this;
            vecInstruction = XmlParser.parseIntro(context, xmlIntro);

            Enumeration vEnum = vecInstruction.elements();
            Enumeration vEnumTitle = vectorIntroTitle.elements();

            textIntro = (TextView) findViewById(R.id.intro);
            textIntro.setText("" + vEnum.nextElement());

            while(vEnum.hasMoreElements()) {
                strStep = "step" + cont;
                strStepTitle = "step" + cont + "Title";

                nextStep = getResources().getIdentifier(strStep , "id", getPackageName());
                nextStepTitle = getResources().getIdentifier(strStepTitle , "id", getPackageName());

                textStep = (TextView) findViewById(nextStep);
                textTitle = (TextView) findViewById(nextStepTitle);
                //textStep.setText(cont + ". " + vEnum.nextElement().toString());

                textTitle.setText(vEnumTitle.nextElement().toString() + "  ");
                textStep.setText(vEnum.nextElement().toString());

                cont++;
            } // while
        } catch (Exception e) {
            System.out.println("ERROR 3: MainActivity.java call2parseIntro --> " + e);
        } // try
    } //  call2parseIntro

    public void parse(Context context, int xmlEval, int testNumbr) {
        // Parse the information from the XML that contains the different Tasks (Aufgaben)
        System.out.println("--> parse");

        //Context context = this.getBaseContext();
        XmlPullParser parser = context.getResources().getXml(xmlEval);
        //XmlPullParser parser = Xml.newPullParser(); //_/_/_/
        //FileInputStream fin = null; //_/_/_/
        String valTime;

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
                                timeTemp = parser.nextText(); //Trujillo 06_03_2016
                                currentAufgabe.setTime(timeTemp); //Trujillo 06_03_2016
                                vecTimeTask.add(timeTemp); //Trujillo 06_03_2016
                            } else if (currentTag.equalsIgnoreCase(AVGUC)) {
                                vecAvgUc.add(parser.nextText());
                            } else if (currentTag.equalsIgnoreCase(AVGDC)) {
                            } else if (currentTag.equalsIgnoreCase(AVGUF)) {
                                vecEmpUf.add(parser.nextText());
                            } else if (currentTag.equalsIgnoreCase(AVGDF)) {
                            } else if (currentTag.equalsIgnoreCase(AVGUT)) {
                                vecTime4taskUt.add(parser.nextText());
                            } else if (currentTag.equalsIgnoreCase(AVGDT)) {
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

            if (countAuf > 0) {
                btnStatistic =  (Button) findViewById(R.id.btnStatistic);
                btnStatistic.setEnabled(true);
            }
        } catch (Exception e) {
            System.out.println("MainActivity.readXML " + e);
        }
    } // readXML

    public void clearFiles(View view){
        System.out.println("--> clearFiles");
        Context context = MainActivity.this;

        try {
            statisticData = false;
            btnStatistic =  (Button) findViewById(R.id.btnStatistic);
            btnStatistic.setEnabled(false);
            btnWeiter = (Button) findViewById(R.id.btnWeiter);
            btnWeiter.setEnabled(false);
            poolActivated = false;
            ArrayList<Aufgabe> aufgb2Eval = null;
            ArrayList<Pool> poolAfgb2Eval = null;
            vecInstruction = new Vector<String>();
            vecQualifikation = new Vector<String>();
            vecTest = new Vector<String>();
            vecEmpfindung = new Vector<String>();
            vecEmpUf = new Vector<String>();
            vecTime = new Vector<String>();
            vecTimeTask = new Vector<String>();
            vecTime4taskUt = new Vector<String>();
            vecAvgUc = new Vector<String>();
            vectorIntroTitle = new Vector<String>();
            sumQualfktion = 0;
            n_QntityAufEval = 0;
            n_poolQntAufEvl = 0;
            n_Aufgbe = 0;
            n_poolAufgb = 0;
            n_Test = 0;
            n_poolTest = 0;
            Aufgabe aufLoad = new Aufgabe();
            Pool aufPoolLoad = new Pool();
            averageQualfktion = 0;
            countAuf = 0;
            countPool = 0;
            done = false;
            currentTag = null;
            currentAufgabe = null;
            helpOn = false;
            helpValue = null;
            readNextTest = false;
            readNextPoolTest = false;
            starts2ndPool = false;
            startsFromSavedInfo = false;
            startsFromSavedPoolInfo = true;
            statisticData = false;

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
            System.out.println("ERROR 1: MainActivity.java ClearFiles --> " + e);
        }
    } // clearFiles

    /* Shows the current statistic */
    public void ShowStatistic(View view) {
        System.out.println("--> ShowStatistic");

        n_Aufgbe = 0; //the first Aufgabe is going to be evaluated
        n_Test = 1; // the first test is going to be evaluated

        if (!statisticData) {
            if (vecAvgUc.size() == 0) {
                //starts the XML parsing
                Context contextParse = this;
                parse(contextParse, xml01, n_Test);
                readXMLQntTest();
                parseNextMain(xml02, 2);
                parseNextMain(xml03, 3);
                poolParseMain(xmlPool01, 1);
                poolParseMain(xmlPool02, 2);
            } // if
        } // if

        try {
            FileInputStream fin = null;

            if ((vecTest.size() == 0)) {
                fin = openFileInput("aufgabeValues.xml");
                XmlParser.parseXmlAufStatistic(fin);
                fin.close();


                fin = openFileInput("poolValues.xml");
                XmlParser.parseXmlPoolStatistic(fin);
                fin.close();
            }

            Intent intent = new Intent(this, Statistic.class);
            //sends the name of which layout to use to the new intent
            int xmlScreen = R.layout.activity_statistic;
            intent.putExtra(EXTRA_MESSAGE, xmlScreen);
            startActivity(intent);
            //finish();
        } catch(Exception e) {
            System.out.println("MainActivity.ShowStatistic " + e);
        }
    } // ShowStatistic

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

    public void activateBtnStatistic() {
        // Verifies when the button Statistic in the main screen should be activated
        System.out.println("--> activateBtnStatistic");

        int testNum = 0;
        int quantTest3 = 0;
        FileInputStream fin = null;
        try {
            fin = openFileInput("aufgabeValues.xml");
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fin, "UTF-8");
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        if (currentTag.equalsIgnoreCase(TEST)){
                            testNum = Integer.parseInt(parser.getAttributeValue(0));
                        } else if (currentTag.equalsIgnoreCase(AUFGABE)) {
                            MainActivity.countAuf++;
                            if (testNum == 3){
                                quantTest3 ++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                } // switch
                eventType = parser.next();
            } // while
            fin.close();

            if (countAuf > 0) {
                btnStatistic =  (Button) findViewById(R.id.btnStatistic);
                btnStatistic.setEnabled(true);

                if (quantTest3 < 5) {
                    btnWeiter = (Button) findViewById(R.id.btnWeiter);
                    btnWeiter.setEnabled(true);
                } else {
                    btnWeiter = (Button) findViewById(R.id.btnWeiter);
                    btnWeiter.setEnabled(false);
                }
            } else {
                btnStatistic =  (Button) findViewById(R.id.btnStatistic);
                btnStatistic.setEnabled(false);

                btnWeiter = (Button) findViewById(R.id.btnWeiter);
                btnWeiter.setEnabled(false);
            }

            fin = openFileInput("poolValues.xml");
            XmlPullParser parser2 = Xml.newPullParser();
            parser2.setInput(fin, "UTF-8");
            int eventType2 = parser2.getEventType();

            while (eventType2 != XmlPullParser.END_DOCUMENT) {
                switch (eventType2) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();
                        if (currentTag.equalsIgnoreCase(AUFGABE)) {
                            MainActivity.countPool++;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                } // switch
                eventType2 = parser2.next();
            } // while
            fin.close();

        } catch (Exception e) {
            System.out.println("MainActivity.activateBtnStatistic " + e);
        }
    } // activateBtnStatistic

    public static void call2WriteClasses(View view) {
        TestAufgabe.writeSystemOutAufgabeXML();
    }

    public void parseNextMain(int xmlEval, int testNumbr) {
        // Parse the information from the XML that contains the different Tasks (Aufgaben)
        System.out.println("--> parseNextMain" + testNumbr + " xml " + xmlEval);

        Context context = this;
        XmlPullParser parser = context.getResources().getXml(xmlEval);

        try{
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                       if (currentTag.equalsIgnoreCase(MainActivity.AVGUC)) {
                           vecAvgUc.add(parser.nextText());
                       } else if (currentTag.equalsIgnoreCase(AVGDC)) {
                       } else if (currentTag.equalsIgnoreCase(AVGUF)) {
                           vecEmpUf.add(parser.nextText());
                       } else if (currentTag.equalsIgnoreCase(AVGDF)) {
                       } else if (currentTag.equalsIgnoreCase(AVGUT)) {
                           vecTime4taskUt.add(parser.nextText());
                       } else if (currentTag.equalsIgnoreCase(AVGDT)) {
                       } else if (currentTag.equalsIgnoreCase(TIME)) { //Trujillo 06_03_2016
                        timeTemp = parser.nextText(); //Trujillo 06_03_2016
                        vecTimeTask.add(timeTemp); //Trujillo 06_03_2016
                       } // if

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch (Exception e) {
            System.out.println("ERROR ???: TestAufgabe.parseNextMain --> " + e);
        } // try
    } // parseNextMain

    public void poolParseMain(int xmlEval, int poolTestNumbr) {
        // Parse the information from the XML that contains the different Tasks (Aufgaben)
        System.out.println("--> poolParseMain" + poolTestNumbr + " xml " + xmlEval );

        Context context = this;
        XmlPullParser parser = context.getResources().getXml(xmlEval);

        try{
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(MainActivity.AVGUC)) {
                            vecAvgUc.add(parser.nextText());
                        } else if (currentTag.equalsIgnoreCase(AVGDC)) {
                        } else if (currentTag.equalsIgnoreCase(AVGUF)) {
                            vecEmpUf.add(parser.nextText());
                        } else if (currentTag.equalsIgnoreCase(AVGDF)) {
                        } else if (currentTag.equalsIgnoreCase(AVGUT)) {
                            vecTime4taskUt.add(parser.nextText());
                        } else if (currentTag.equalsIgnoreCase(AVGDT)) {
                        } else if (currentTag.equalsIgnoreCase(TIME)) { //Trujillo 06_03_2016
                            timeTemp = parser.nextText(); //Trujillo 06_03_2016
                            vecTimeTask.add(timeTemp); //Trujillo 06_03_2016
                        } // if

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch (Exception e) {
            System.out.println("ERROR ???: TestAufgabe.poolParseMain --> " + e);
        } // try
    } // poolParseMain
} // MainActivity

