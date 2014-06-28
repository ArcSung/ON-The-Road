package com.arc.on_the_road;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	
	ImageButton ImageButton_Camera;
	ImageButton ImageButton_Search;
	ImageButton ImageButton_Gralloc;
	ImageButton ImageButton_Phone;	
	ImageButton ImageButton_People;
	
	private static final int ACTIVITY_SELECT_CAMERA = 0;
	private static final int ACTIVITY_SELECT_Search = 1;
	private static final int ACTIVITY_SELECT_Phone = 2;
	private static final int ACTIVITY_SELECT_Grolloc = 3;
	private static final int ACTIVITY_SELECT_People = 4;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		init_index_ui();
		
	}	
	
	private void init_index_ui()
	{
		setContentView(R.layout.main_layout);
		ImageButton_Camera  = (ImageButton)findViewById(R.id.index_camera);
		ImageButton_Search  = (ImageButton)findViewById(R.id.index_search);
		ImageButton_Gralloc = (ImageButton)findViewById(R.id.index_gralloc);
		ImageButton_Phone   = (ImageButton)findViewById(R.id.index_phone);
		ImageButton_People  = (ImageButton)findViewById(R.id.index_people);
		ImageButton_Search = (ImageButton)findViewById(R.id.index_search);
		
		ImageButton_Camera.setOnClickListener(ImageButtonlistener);
		ImageButton_Search.setOnClickListener(ImageButtonlistener);
		ImageButton_Gralloc.setOnClickListener(ImageButtonlistener);
		ImageButton_Phone.setOnClickListener(ImageButtonlistener);
		ImageButton_People.setOnClickListener(ImageButtonlistener);
		ImageButton_People.setOnClickListener(ImageButtonlistener);

	}
	
	private OnClickListener ImageButtonlistener =new OnClickListener(){
		 
		@Override
		    public void onClick(View v) {
		// TODO Auto-generated method stub
		        switch(v.getId()){
		            case R.id.index_camera:
		            	Log.i("On the road index", "camera");
		            break;
		            case R.id.index_search:
		            	Log.i("On the road index", "search");
		            break;
		            case R.id.index_gralloc:
		            	Log.i("On the road index", "gralloc");
		            break;
		            case R.id.index_phone:
		            	Log.i("On the road index", "phone");
		            	Intent intent = new Intent();
		    			intent.setClass(MainActivity.this,PhoneActivity.class);
		    			startActivityForResult(intent, ACTIVITY_SELECT_Phone);
		            break;
		            case R.id.index_people:
		            	Log.i("On the road index", "people");
		            break;
		        }
		    } 
		};

}
