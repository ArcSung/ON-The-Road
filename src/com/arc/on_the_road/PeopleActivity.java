package com.arc.on_the_road;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

import android.R.string;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup;

public class PeopleActivity extends Activity {
	String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	private static final int ACTIVITY_SELECT_name = 0;
	private static final int ACTIVITY_SELECT_email = 1;
	private static final int ACTIVITY_SELECT_Phone = 2;
	private static final int ACTIVITY_SELECT_address = 3;
	int select_flag = 0;
	ImageView pepole_info; 
	EditText  pepole_edittext;
	Button    pepole_button;
	
    String NAME;
    String EMAIL;
    String PHONE;
    String ADDRESS;
    
    SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.people_layout);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    
	    
	    prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    
	    init_people_data();
	    
	    pepole_info = (ImageView) findViewById(R.id.people_inof);
	    pepole_edittext = (EditText) findViewById(R.id.edittext);
	    pepole_button  = (Button) findViewById(R.id.button1);
	    
	    if(NAME != "NULL")
	    	pepole_edittext.setText(NAME);
	    
	    
	    pepole_button.setOnClickListener(new Button.OnClickListener() {
			   public void onClick(View v)
				{
				  if(select_flag == ACTIVITY_SELECT_name)
				  {
					  NAME = pepole_edittext.getText().toString();
					  pepole_edittext.setText(EMAIL);
					  select_flag = ACTIVITY_SELECT_email;
					  pepole_info.setImageResource(R.drawable.people_email);
					  
				  }
				  else if (select_flag == ACTIVITY_SELECT_email)
				  {
					  EMAIL = pepole_edittext.getText().toString();
					  pepole_edittext.setText(PHONE);
					  select_flag = ACTIVITY_SELECT_Phone;
					  pepole_info.setImageResource(R.drawable.people_callphone);
				  }
				  else if (select_flag == ACTIVITY_SELECT_Phone)
				  {
					  PHONE = pepole_edittext.getText().toString();
					  pepole_edittext.setText(ADDRESS);
					  select_flag = ACTIVITY_SELECT_address;
					  pepole_info.setImageResource(R.drawable.people_address);
					  pepole_button.setText("完成");
				  }
				  else if (select_flag == ACTIVITY_SELECT_address)
				  {
					  ADDRESS = pepole_edittext.getText().toString();
		
					  SharedPreferences.Editor mEditor = prefs.edit();  
			          
				        mEditor.putString("NAME", NAME);
				        mEditor.putString("EMAIL", EMAIL);
				        mEditor.putString("PHONE", PHONE);
				        mEditor.putString("ADDRESS", ADDRESS);
				        mEditor.commit();  
					  
					  Intent intent_people = new Intent();
		    		  intent_people.setClass(PeopleActivity.this,MainActivity.class);
		    		  startActivity(intent_people);
		    			finish();
				  }
				}
		});   
	    
	}
	
	private void init_people_data()
	{
	    prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    NAME = prefs.getString("NAME", "");
	    EMAIL = prefs.getString("EMAIL", "");
	    PHONE = prefs.getString("PHONE", "");
	    ADDRESS = prefs.getString("ADDRESS", "");
		Log.i("On the road index", "getString NAME: "+NAME+" EMAIL: "+EMAIL+" PHONE: "+PHONE+" ADDRESS"+ADDRESS);
		
	}

}
