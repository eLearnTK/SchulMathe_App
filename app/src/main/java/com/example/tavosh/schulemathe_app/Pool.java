package com.example.tavosh.schulemathe_app;

import java.util.Vector;

public class Pool {
	private int poolTest;
	private int poolAufgb;
	private String imagePool;
	private String imagePoolLsng = new String();
	private Vector hilfe = new Vector<String>();
	private String text = new String();
	private String time = new String();
    private String timeRequired = new String();
	private int zustand = 0;
	private int qualifikation;

	public Pool() {
	// Constructor without parameters	
	}

	public Pool(int poolTest, int poolAufgb, String imagePool, String hilfe, String iconPoolLsng, String Text, String time, String timeRequired, int zustand, int qlfktn) {
		// Constructor with parameters
		
		super();
		this.poolAufgb = poolAufgb;
		this.imagePool = imagePool;
		this.hilfe.add(hilfe);
		this.imagePoolLsng = iconPoolLsng;
		this.text = Text;
		this.time = time;
        this.timeRequired = timeRequired;
		this.zustand = zustand;
		this.qualifikation = qlfktn;
		this.poolTest = poolTest;
	}
	
	//Getters
	public int getPoolAufgb() {
		return poolAufgb;
	}
	
	public String getImagePool() {
		return (imagePool.trim());
	}

	public String getImageLoesung() {
		return imagePoolLsng;
	}

	public Vector getHilfe() {
		return hilfe;
	}
	
	public String getText() {
		return text;
	}
	
	public String getTime(){
		return time;
	}

    public String getTimeRequired() {return timeRequired;}
	
	public int getZustand(){
		return zustand;
	}
	
	public int getQualifikation(){
		return qualifikation;
	}
	
	public int getPoolTest(){
		return poolTest;
	}
	
	//Setters
	public void setImagePool(String imagePool) {
		String temp = imagePool.replaceAll(" ", "");;
		this.imagePool = temp.trim();
	}

	public void setImagePoolLsng(String imageLoesung) {
		this.imagePoolLsng = imageLoesung;
	}

	public void setPoolAufgb(int poolAfgb) {
		this.poolAufgb = poolAfgb;
	}

	public void setHilfe(String hilfe) {
		this.hilfe.add(hilfe);
	}

	public void setText(String text){
		this.text = text;
	}
	
	public void setTime(String time){this.time = time;}

    public void setTimeRequired(String timeReq){this.timeRequired = timeReq;}
	
	public void setZustand(int zstndVal){
		// Indicates the different values from the Pool:
		//  (StandBy = 0, Started = 1, TaskSolved = 2, Personal evaluated = 3, % evaluated = 4)
			this.zustand = zstndVal;
	}
	
	public void setQualifikation(int qlfktn){
		this.qualifikation = qlfktn;
	}
	
	public void setPoolTest(int poolTst) {
		this.poolTest = poolTst;
	}
}

