package com.arc.on_the_road;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CameraPreviewActivity extends Activity {
    /** Called when the activity is first created. */
    int correct=0;
    final int Camera_start_preview = 0;
    final int Camera_stop_preview  = 1;
    final int Camera_save_picture  = 2;
    final int Camera_close_camera  = 3;
    CameraView cameraView;
	Bitmap newb;
	
	Boolean Camera_button_check = false;
	
	ImageButton ImageButton_TakePicture;
	ImageButton ImageButton_Cancel;
	ImageButton ImageButton_Check_Grolloc;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 取得全螢幕
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
        
        // get monitor size
		DisplayMetrics metrics = new DisplayMetrics();  
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
 
        Intent intent = getIntent(); 
        Double longitude = intent.getDoubleExtra("longitude", 0);
        Double latitude = intent.getDoubleExtra("latitude", 0);
        
        //Log.i("Arc","longitude"+longitude+"  latitude"+latitude);
        setContentView(R.layout.camera_init_main_layout); //#1        
        //鎖住螢幕方向
        setRequestedOrientation(1);   //Upright    
        //取得畫圖的View
        CameraViewToDraw dtw = (CameraViewToDraw) findViewById(R.id.vtd);
        //dtw.setSize(metrics.widthPixels, metrics.heightPixels); //#2
        
        ImageButton_TakePicture = (ImageButton)findViewById(R.id.camera_takepicture);
        ImageButton_Cancel    = (ImageButton)findViewById(R.id.camera_cancel);
        ImageButton_Check_Grolloc      = (ImageButton)findViewById(R.id.camera_check_grolloc);
        
        ImageButton_TakePicture.setOnClickListener(ImageButtonlistener);
        ImageButton_Cancel.setOnClickListener(ImageButtonlistener);
        ImageButton_Check_Grolloc.setOnClickListener(ImageButtonlistener);
        
        ImageButton_Check_Grolloc.setVisibility(View.VISIBLE);
        ImageButton_Check_Grolloc.setBackgroundResource(R.drawable.camera_grolloc);
        ImageButton_Cancel.setVisibility(View.GONE);
        //ImageButton_Check_Grolloc.setVisibility(View.GONE);
        
        createDirIfNotExists("/sdcard/ON_THE_ROAD/");
 
        //產生攝影機預覽surfaceView
        cameraView = new CameraView(this, dtw, this.getApplicationContext(), longitude, latitude, metrics.widthPixels, metrics.heightPixels);
        //把預覽的surfaceView加到名為preview的FrameLayout
        ((FrameLayout) findViewById(R.id.preview)).addView(cameraView);
               
    }//end onCreate(Bundle savedInstanceState)
    
    
	private OnClickListener ImageButtonlistener =new OnClickListener(){
		 
		@Override
		    public void onClick(View v) {
		// TODO Auto-generated method stub
		        switch(v.getId()){
	            	case R.id.camera_takepicture:	
	            		cameraView.onButtonCheck(Camera_stop_preview);
	            		ImageButton_Cancel.setVisibility(View.VISIBLE);
	            		ImageButton_Check_Grolloc.setVisibility(View.VISIBLE);
	            		ImageButton_Check_Grolloc.setBackgroundResource(R.drawable.camera_check);
	                    ImageButton_TakePicture.setVisibility(View.GONE);	
	                    Camera_button_check = true;
		            break;
		            
	            	case R.id.camera_cancel:
	            		cameraView.onButtonCheck(Camera_start_preview);
	            		ImageButton_Cancel.setVisibility(View.GONE);
	            		ImageButton_Check_Grolloc.setVisibility(View.VISIBLE);
	            		ImageButton_Check_Grolloc.setBackgroundResource(R.drawable.camera_grolloc);
	                    ImageButton_TakePicture.setVisibility(View.VISIBLE);
	                    Camera_button_check = false;
		            break;
	            	case R.id.camera_check_grolloc:
	            		if(Camera_button_check = true) //stop preview state
	            		{	
	            			cameraView.onButtonCheck(Camera_save_picture);
	            			cameraView.onButtonCheck(Camera_start_preview);
	            			ImageButton_Cancel.setVisibility(View.GONE);
	            			ImageButton_Check_Grolloc.setVisibility(View.VISIBLE);
	            			ImageButton_Check_Grolloc.setBackgroundResource(R.drawable.camera_grolloc);
	            			ImageButton_TakePicture.setVisibility(View.VISIBLE);
	            			Camera_button_check = false;
	            		}
	            		else     //start preview state
	            		{
	            			
	            		}	
	               break;
		        }
		    } 
	};
    
	
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
	        if ((keyCode == KeyEvent.KEYCODE_BACK)) 
	        {   
	        	cameraView.onButtonCheck(Camera_close_camera);//When retrun, stop camera first
	        	CameraPreviewActivity.this.finish();
	            return true;  
	        }  
	        return super.onKeyDown(keyCode, event);  
	  }
	
	public static boolean createDirIfNotExists(String path) {
	    boolean ret = true;

	    File file = new File(Environment.getExternalStorageDirectory(), path);
	    if (!file.exists()) {
	        if (!file.mkdirs()) {
	            Log.e("TravellerLog :: ", "Problem creating Image folder");
	            ret = false;
	        }
	    }
	    return ret;
	}
}