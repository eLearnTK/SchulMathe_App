package com.example.tavosh.schulemathe_app;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class XmlParser {
    //variables for parsing the XML
    private static boolean done = false;
    private static String currentTag = null;
    static int aufgabeNumber = 0;

    // names of the XML tags for test files
    static final String AUFGABEN = "aufgaben";
    static final String AUFGABE = "aufgabe";
    static final String BILD = "bild";
    static final String HILFE = "hilfe";
    static final String VALUE = "value";
    static final String LOESUNG = "loesung";
    static final String TEXT = "text";
    static final String TIME = "time";
    static final String TEST = "test";
    static final String TIMEREQUIRED = "timerequired";
    static final String ZUSTANG = "zustang";
    static final String QUALIFIKATION = "qualifikation";

    // Variable helpOn is to know when reading a help tag
    static boolean helpOn = false;
    static String helpValue = null;

    static ArrayList<Aufgabe> aufgabeList = null;
    private static Aufgabe currentAufgabe = null;
    private static Pool currentPool = null;

    //variable for parseIntro
    private static Vector vectorIntro;
    static final String INTRO = "intro";
    static final String STEP = "step";
    static final String INSTRUCTION = "instruction";

    public static Vector parseIntro(Context context, int xmlEval) {
        System.out.println("--> parseIntro");

        XmlPullParser parser = context.getResources().getXml(xmlEval);

        try {
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        vectorIntro = new Vector<String>();

                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(INTRO)) {
                            vectorIntro.addElement(parser.nextText());
                        } else if (currentTag.equalsIgnoreCase(STEP)) {
                            vectorIntro.addElement(parser.nextText());
                        } // end if

                        break;
                    case XmlPullParser.END_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(INSTRUCTION)) {
                            done = true;
                        }

                        break;
                } // end switch

                eventType = parser.next();
            } // end while
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error ???: " + e);
        }

        return vectorIntro;
    } //end parseIntro

    public static void parse(Context context, int xmlEval, int testNumbr) {
        // Parse the information from the XML that contains the different Tasks (Aufgaben)
        System.out.println("--> parse");

        XmlPullParser parser = context.getResources().getXml(R.xml.test01);

        try {
            int eventType = parser.getEventType();
            int aufgabeNumber = 0;

            while (eventType != XmlPullParser.END_DOCUMENT && !done) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        MainActivity.aufgb2Eval = new ArrayList<Aufgabe>();
                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(AUFGABE)) {
                            currentAufgabe = new Aufgabe();
                            aufgabeNumber++;
                            currentAufgabe.setAufgabe(aufgabeNumber);

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

                                    if (helpValue.length() > 0) {
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
                            MainActivity.aufgb2Eval.add(currentAufgabe);
                        } else if (currentTag.equalsIgnoreCase(AUFGABEN)) {
                            done = true;
                        }
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch (Exception e) {
            System.out.println("ERROR ???: .java parse --> " + e);
        } // try
    } // parse

    public static void parseXmlAufgabe(FileInputStream fis) {
        // Parse the information from the XML that contains the different SAVED Tasks (Aufgaben)
        // so the application can start from the last point
        System.out.println("--> parseXmlAufgabe");

        try {
            XmlPullParser parser = Xml.newPullParser(); //_/_/_/
            parser.setInput(fis, "UTF-8"); //_/_/_/

            int eventType = parser.getEventType();
            int testNum = 0;
            int testNumTemp;
            int aufNum = 0;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST)) {
                            testNum = Integer.parseInt(parser.getAttributeValue(0));
                            //currentAufgabe = MainActivity.aufgb2Eval.get(testNum - 1);
                            currentAufgabe = MainActivity.aufgb2Eval.get(aufNum);
                            currentAufgabe.setTest(testNum);
                            //_/1 if (testNum > 1) {
                            //_/1     MainActivity.n_Test = testNum - 1;
                            //_/1 } else {
                            //_/1     MainActivity.n_Test = testNum;
                            //_/1 }

                            //_/1 //Reads the xml with the necessary values for the next test
                            //_/1 if (testNum > 1) {
                            //_/1     MainActivity.readNextTest = true;
                            //_/1 }

                        } else if (currentTag != null) {
                            if (currentTag.equalsIgnoreCase(AUFGABE)) {
                                aufNum = Integer.parseInt(parser.nextText());
                                currentAufgabe.setAufgabe(aufNum);
                                //_/1 MainActivity.n_Aufgbe = aufNum;
                                //MainActivity.n_QntityAufEval++;
                            } else if (currentTag.equalsIgnoreCase(TIMEREQUIRED)) {
                                currentAufgabe.setTimeRequired(parser.nextText());
                            } else if (currentTag.equalsIgnoreCase(ZUSTANG)) {
                                currentAufgabe.setZustand(Integer.parseInt(parser.nextText()));
                            } else if (currentTag.equalsIgnoreCase(QUALIFIKATION)) {
                                int intQual = Integer.parseInt(parser.nextText());
                                currentAufgabe.setQualifikation(intQual);
                                MainActivity.sumQualfktion = MainActivity.sumQualfktion + intQual;
                            } // if
                        } // if
                        break;

                    case XmlPullParser.END_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST) && currentAufgabe != null) {
                            //_/1 MainActivity.n_QntityAufEval++;
                            //testNumTemp = MainActivity.n_QntityAufEval - 1;
                            testNumTemp = aufNum -1;
                            MainActivity.aufgb2Eval.set(testNumTemp, currentAufgabe);
                            MainActivity.startsFromSavedInfo = false;
                        } else if (currentTag.equalsIgnoreCase(AUFGABEN)) {

                        }
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch (Exception e) {
            System.out.println("ERROR ???: XmlParser.java parseXmlAufgabe --> " + e);
        } // try
    } // parseXmlAufgabe

    public static void parseXmlPool(FileInputStream fis) {
        // Parse the information from the XML that contains the different SAVED Tasks (Aufgaben)
        // so the application can start from the last point
        System.out.println("--> parseXmlPool");

        try {
            XmlPullParser parser = Xml.newPullParser(); //_/_/_/
            parser.setInput(fis, "UTF-8"); //_/_/_/

            int eventType = parser.getEventType();
            int testNum = 0;
            int poolNum = 0;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST)) {
                            testNum = Integer.parseInt(parser.getAttributeValue(0));

                            //_/2 // When MainActivity.poolAfg2Eval have not been already created, it throws an Exception and inside
                            //_/2 // the exception it declares poolAfg2Eval.
                            //_/2 try {
                            //currentPool = MainActivity.poolAfgb2Eval.get(testNum - 1);
                            currentPool = MainActivity.poolAfgb2Eval.get(poolNum);
                            //_/2 } catch (Exception e) {
                            //_/2     MainActivity.poolAfgb2Eval = new ArrayList<Pool>();
                            //_/2     currentPool = new Pool();
                            //_/2     MainActivity.poolAfgb2Eval.add(currentPool);
                            //_/2     //currentPool = MainActivity.poolAfgb2Eval.get(testNum - 1);
                            //_/2 }
                            currentPool.setPoolTest(testNum);
                        } else if (currentTag != null) {
                            if (currentTag.equalsIgnoreCase(AUFGABE)) {
                                poolNum = Integer.parseInt(parser.nextText());
                                currentPool.setPoolAufgb(poolNum);
                                //_/2 MainActivity.n_poolAufgb = poolNum;
                            } else if (currentTag.equalsIgnoreCase(TIMEREQUIRED)) {
                                currentPool.setTimeRequired(parser.nextText());
                            } else if (currentTag.equalsIgnoreCase(ZUSTANG)) {
                                currentPool.setZustand(Integer.parseInt(parser.nextText()));
                            } else if (currentTag.equalsIgnoreCase(QUALIFIKATION)) {
                                currentPool.setQualifikation(Integer.parseInt(parser.nextText()));
                            } // if
                        } // if
                        break;

                    case XmlPullParser.END_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST) && currentAufgabe != null) {
                            //_/2 MainActivity.n_poolQntAufEvl++;
                            // int testNumTemp = MainActivity.n_poolQntAufEvl - 1;
                            int testNumTemp = poolNum - 1;

                            if (testNum > 1) {
                                if (MainActivity.averageQualfktion <= MainActivity.qntPoolAufgaben) {
                                    if (poolNum < 4) {
                                        MainActivity.poolAfgb2Eval.set(testNumTemp, currentPool);
                                    }
                                } else if (poolNum > 3) {
                                    MainActivity.poolAfgb2Eval.set(testNumTemp, currentPool);
                                }
                            } else {
                                if (MainActivity.averageQualfktion <= MainActivity.qntPoolAufgaben) {
                                    if (poolNum < 4) {
                                        MainActivity.poolAfgb2Eval.set(testNumTemp, currentPool);
                                    }
                                } else if (poolNum > 1) {
                                   MainActivity.poolAfgb2Eval.set(testNumTemp, currentPool);
                                }
                            }

                        } else if (currentTag.equalsIgnoreCase(AUFGABEN)) {

                        }
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch (Exception e) {
            System.out.println("ERROR ???: XmlParser.java parseXmlPool --> " + e);
        } // try
    } // parseXmlPool

    public static void parseXmlAufQntTest(FileInputStream fis) {
        // Parse the information from the XML that contains the different SAVED Tasks (Aufgaben)
        // in order to create in the array aufgb2Eval the necessary space to fill after with the
        // method parseXmlAufgabe
        System.out.println("--> parseXmlAufQntTest");

        try {
            XmlPullParser parser = Xml.newPullParser(); //_/_/_/
            parser.setInput(fis, "UTF-8"); //_/_/_/

            int eventType = parser.getEventType();
            int testNum = 0;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST)) {
                            MainActivity.startsFromSavedInfo = true;
                            testNum = Integer.parseInt(parser.getAttributeValue(0));
                            if (testNum > 1) {
                                MainActivity.n_Test = testNum - 1;
                            } else {
                                MainActivity.n_Test = testNum;
                            }

                            // When readNextTest = true --> Reads the xml with the necessary values for the next test
                            if (testNum > 1) {
                                MainActivity.readNextTest = true;
                            }

                        } else if (currentTag != null) {
                            if (currentTag.equalsIgnoreCase(AUFGABE)) {
                                int aufNum = Integer.parseInt(parser.nextText());
                                MainActivity.n_Aufgbe = aufNum;
                            } // if
                        } // if
                        break;

                    case XmlPullParser.END_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST)) {
                            MainActivity.n_QntityAufEval++;
                            //MainActivity.startsFromSavedInfo = false;
                        } else if (currentTag.equalsIgnoreCase(AUFGABEN)) {

                        }
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch (Exception e) {
            System.out.println("ERROR ???: XmlParser.java parseXmlAufQntTest --> " + e);
        } // try
    } // parseXmlAufQntTest

    public static void parseXmlPoolQntTest(FileInputStream fis) {
        // Parse the information from the XML that contains the different SAVED Tasks (Aufgaben)
        // so the application can start from the last point
        System.out.println("--> parseXmlPoolQntTest");

        try {
            XmlPullParser parser = Xml.newPullParser(); //_/_/_/
            parser.setInput(fis, "UTF-8"); //_/_/_/

            int eventType = parser.getEventType();
            int testNum = 0;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST)) {
                            // When MainActivity.poolAfg2Eval have not been already created, it throws an Exception and inside
                            // the exception it declares poolAfg2Eval.
                            //try {
                            //    Pool currentPoolTemp = MainActivity.poolAfgb2Eval.get(testNum - 1);
                            //} catch (Exception e) {
                            //     MainActivity.poolAfgb2Eval = new ArrayList<Pool>();
                                 //currentPool = new Pool();
                                 //MainActivity.poolAfgb2Eval.add(currentPool);
                                 //currentPool = MainActivity.poolAfgb2Eval.get(testNum - 1);
                            //}

                            testNum = Integer.parseInt(parser.getAttributeValue(0));
                            MainActivity.n_poolTest = testNum;

                            // When readNextTest = true --> Reads the xml with the necessary values for the next test
                            if (testNum > 0) {
                                MainActivity.readNextPoolTest = true;
                            }

                            // The next "if" statement sets the variable Pool Activated when there is a pool to display
                            if (MainActivity.n_poolTest == MainActivity.n_Test) {
                                MainActivity.poolActivated = true;
                            } else {
                                MainActivity.poolActivated = false;
                            }
                        } else if (currentTag != null) {
                            if (currentTag.equalsIgnoreCase(AUFGABE)) {
                                int poolNum = Integer.parseInt(parser.nextText());
                                MainActivity.n_poolAufgb = poolNum;
                            } // if
                        } // if
                        break;

                    case XmlPullParser.END_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST)) {
                            MainActivity.n_poolQntAufEvl++;
                        } else if (currentTag.equalsIgnoreCase(AUFGABEN)) {

                        }
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch (Exception e) {
            System.out.println("ERROR ???: XmlParser.parseXmlPoolQntTest --> " + e);
        } // try
    } // parseXmlPoolQntTest

    public static void lectorDarchivos(FileInputStream fis){  //_/_/_/ PRUEBAS _/_/_/
    // Parse the information from the XML (in virtual memory) that contains the different SAVED Tasks (Aufgaben)
    // so the application can start from the last point and display it on the System.out
        System.out.println("--> lectorDarchivos");

        try {
            XmlPullParser parser = Xml.newPullParser(); //_/_/_/
            parser.setInput(fis, "UTF-8"); //_/_/_/

            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST)) {
                            System.out.println("<test id=\"" + parser.getAttributeValue(0) + "\">");
                        } else if (currentTag != null) {
                            if (currentTag.equalsIgnoreCase(AUFGABE)) {
                                System.out.println("   <aufgabe>" + parser.nextText() + "</aufgabe>");
                            } else if (currentTag.equalsIgnoreCase(TIMEREQUIRED)) {
                                System.out.println("   <timerequired>" + parser.nextText() + "</timerequired>");
                            } else if (currentTag.equalsIgnoreCase(ZUSTANG)) {
                                System.out.println("   <zustang>" + parser.nextText() + "</zustang>");
                            } else if (currentTag.equalsIgnoreCase(QUALIFIKATION)) {
                                System.out.println("   <qualifikation>" + parser.nextText() + "</qualifikation>");
                            } // if
                        } // if
                        break;

                    case XmlPullParser.END_TAG:
                        currentTag = parser.getName();

                        if (currentTag.equalsIgnoreCase(TEST) && currentAufgabe != null) {
                            System.out.println("</test>");
                        } else if (currentTag.equalsIgnoreCase(AUFGABEN)) {
                            done = true;
                        }
                        break;
                } // switch
                eventType = parser.next();
            } // while
        } catch(Exception e) {
            System.out.println("ERROR ???: XmlParser.java lectorDarchivos --> " + e);
        } // try
    } // Lector de archivos

} // XmlParser
