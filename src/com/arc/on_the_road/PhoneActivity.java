package com.arc.on_the_road;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;

public class PhoneActivity extends Activity {

	ImageButton    ImageButton_call_road;
	ImageButton    ImageButton_call_city;
	ImageButton    ImageButton_about;
	

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
		setContentView(R.layout.phone_layout);
		
		ImageButton_call_road  = (ImageButton)findViewById(R.id.phone_road);
		ImageButton_call_city  = (ImageButton)findViewById(R.id.phone_city);
		ImageButton_about = (ImageButton)findViewById(R.id.phone_about);
		
		ImageButton_call_road.setOnClickListener(ImageButtonlistener);
		ImageButton_call_city.setOnClickListener(ImageButtonlistener);
		ImageButton_about.setOnClickListener(ImageButtonlistener);
	}
	
	private OnClickListener ImageButtonlistener =new OnClickListener(){
		 
		@Override
		    public void onClick(View v) {
		// TODO Auto-generated method stub
		        switch(v.getId()){
	            	case R.id.phone_road:
	            		 String phoneNumber_road = "062932689"; 
	            		 Intent intentDial_raod = new Intent("android.intent.action.CALL",Uri.parse("tel:"+phoneNumber_road));
	  				   	 startActivity(intentDial_raod);
	  				break;   	 
	            	case R.id.phone_city:
	            		 String phoneNumber_city = "1999"; 
	   				     Intent intentDial_city = new Intent("android.intent.action.CALL",Uri.parse("tel:"+phoneNumber_city));
	   				     startActivity(intentDial_city);
	   				break;     
		            case R.id.phone_about:
		            	About_Dialog();
		            break;
		        }
		    } 
		};
	
	@SuppressLint("ResourceAsColor")
	private void About_Dialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(1);
		
		ImageView AboutImage = new ImageView(this);
		AboutImage.setImageResource(R.drawable.phone_about_view);
		AboutImage.setScaleType(ScaleType.FIT_XY);
		
	    linear.addView(AboutImage);
	    builder.setView(linear); 
				
	    final AlertDialog About_Dialog = builder.create();
	    About_Dialog.show();
	    
	    AboutImage.setOnClickListener(new ImageButton.OnClickListener() 
		{
			   public void onClick(View v)
				{
				   About_Dialog.cancel();
				}
		});  
	}
}
