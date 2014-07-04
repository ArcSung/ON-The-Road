package com.arc.on_the_road;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.util.AttributeSet;
import android.view.View;

public class CameraViewToDraw extends View{
	
	private int imgHeight = 0, imgWidth = 0;
	private byte[] image = null;
	private boolean isCameraSet = false;
	int rgb[];
	int nx1,nx2,ny1,ny2;
	String cityid = "";
	String villageid = "";
	String streetid = "";

	
	Paint TimePaint = new Paint();
	Paint whitePaint2 = new Paint();
	Paint blackPaint = new Paint();
	Paint greenPaint = new Paint();

	public CameraViewToDraw(Context context) {
		super(context);
		setPaint();
	}
	
	public CameraViewToDraw(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPaint();
	}
	
	public void setPaint(){
		TimePaint.setColor(Color.WHITE);
		TimePaint.setStyle(Paint.Style.FILL);
		TimePaint.setStrokeWidth(3);
		TimePaint.setTextSize((int)(imgWidth/15));
		TimePaint.setTypeface(Typeface.MONOSPACE);
		whitePaint2.setColor(Color.WHITE);
		whitePaint2.setStyle(Paint.Style.FILL);
		whitePaint2.setStrokeWidth(3);
		whitePaint2.setTextSize(120);
		whitePaint2.setTypeface(Typeface.MONOSPACE);
	}

	public void setSize(int h, int w){
		imgHeight = h;
		imgWidth = w;
	}
	
	public void CameraSet(){
		isCameraSet = true;
	}
	
	public void setAddress(String city, String village, String street){
		cityid = city;
		villageid = village;
		streetid = street;
	}
	
	public void putImage(byte[] Img){
		image = Img;
		
	}

	//這是一個簡單的把YUV421轉成黑白的運算函式, 如要更進一步轉成RGB256, 請參考下面的方法
	private boolean getBoolean(int x, int y){
		int l;
		l = (0xff & ((int) image[x+y*imgWidth])) - 16;
	  	if (l > 128){
	  		return true;
	  	}else{
	  		return false;
	  	}
	}
	//網路上挖到的, 原始來源已不可考, 請小心使用
	static public void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
    	final int frameSize = width * height;
    	
    	for (int j = 0, yp = 0; j < height; j++) {
    		int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
    		for (int i = 0; i < width; i++, yp++) {
    			int y = (0xff & ((int) yuv420sp[yp])) - 16;
    			if (y < 0) y = 0;
    			if ((i & 1) == 0) {
    				v = (0xff & yuv420sp[uvp++]) - 128;
    				u = (0xff & yuv420sp[uvp++]) - 128;
    			}
    			
    			int y1192 = 1192 * y;
    			int r = (y1192 + 1634 * v);
    			int g = (y1192 - 833 * v - 400 * u);
    			int b = (y1192 + 2066 * u);
    			
    			if (r < 0) r = 0; else if (r > 262143) r = 262143;
    			if (g < 0) g = 0; else if (g > 262143) g = 262143;
    			if (b < 0) b = 0; else if (b > 262143) b = 262143;
    			
    			rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    		}
    	}
    }
	

	
	@Override
	protected void onDraw(Canvas canvas){
		
		float locationX = imgWidth/15; 
		float locationY = (imgHeight/16)*14;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    	Date curDate = new Date(System.currentTimeMillis());
    	String date = formatter.format(curDate);
    	canvas.drawText(date, locationX, locationY, TimePaint);
    	
    	canvas.drawText(cityid, 70, 1600, whitePaint2);
    	canvas.drawText(villageid, 400, 1600, TimePaint);
    	canvas.drawText(streetid, 70, 1700, TimePaint);
    	
	}
}
