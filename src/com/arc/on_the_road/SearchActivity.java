package com.arc.on_the_road;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class SearchActivity extends Activity {
	
	private ListView listData = null;	
	ImageButton    pepole_info_button;
	ImageButton    search_button;
	
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
        setContentView(R.layout.search_layout);
        
        initView();
        
        showInList();
        
        Intent intent = getIntent(); 
        final Double longitude = intent.getDoubleExtra("longitude", 0);
        final Double latitude = intent.getDoubleExtra("latitude", 0);
        Search_info(longitude, latitude);
        
	    pepole_info_button  = (ImageButton) findViewById(R.id.Search_camera_image);
	    pepole_info_button.setOnClickListener(new ImageButton.OnClickListener() {
			   public void onClick(View v)
				{
				    Intent intent = new Intent();
					intent.setClass(SearchActivity.this,CameraPreviewActivity.class);
					intent.putExtra("longitude",longitude); 
					intent.putExtra("latitude",latitude);
					startActivityForResult(intent, 0);


				}
		});  
	    
	    search_button  = (ImageButton) findViewById(R.id.Search_title_image);
	    search_button.setOnClickListener(new ImageButton.OnClickListener() {
			   public void onClick(View v)
				{
				  showInList();

				}
		});

		
    }
	
	private void initView(){
		listData = (ListView) findViewById(R.id.listData);
    	listData.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				// TODO Auto-generated method stub
				Search_Dialog(id);
				return false;
			}
			
		});
	}
	
    private void showInList(){
    	
    	//Cursor cursor = getCursor();
    	
    	 for(int i=0; i<mPathStart.length; i++)
    	 {
    		 HashMap<String,String> item = new HashMap<String,String>();
    		 item.put("PathStart", mPathStart[i]);
    		 item.put("PathEnd", mPathEnd[i]);
    		 item.put("Distance",mDistance[i]+"m");
    		
    		 list.add( item );
    	}
    	
    	//新增SimpleAdapter
    	SimpleAdapter adapter = new SimpleAdapter( 
    	 this, 
    	 list,
    	 R.layout.search_data_item,
    	 new String[] { "PathStart","PathEnd","Distance"},
    	 new int[] { R.id.txtPathStart, R.id.txtPathEnd, R.id.txtDistance} );
    	 
    	 //ListActivity設定adapter
    	listData.setAdapter( adapter );
    }
    
	private void Search_info(Double longitude, Double latitude){
		
		String url = "http://linarnan.co:3000/roadhelper/aloha?lat="+latitude+"&lng="+longitude;
    	//new HttpAsyncTask().execute(url);

	}
	
	private void Search_Dialog(final long RowID)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(1);
		
		linear.setBackgroundColor(Color.rgb(60,60,60));
		ImageView PhotoImage = new ImageView(this);	
		PhotoImage.setImageResource(R.drawable.search_pushcar);
		linear.addView(PhotoImage);
		/*if(Date_db == "Yes")
		{	
			TextView road = new TextView(this);
			road.setTextSize(20);
			road.setTextColor(Color.rgb(255,255,0));;
			road.setText("路平專案中");
			linear.addView(road);
		}*/
				
		TextView PathStartText = new TextView(this);
		PathStartText.setTextSize(20);
		PathStartText.setTextColor(Color.rgb(255,255, 255));;
		PathStartText.setText("起 "+mPathStart[(int)RowID]);
				
		TextView PathPathEndText = new TextView(this);
		PathPathEndText.setTextSize(20);
		PathPathEndText.setTextColor(Color.rgb(255,255, 255));;
		PathPathEndText.setText("終 "+mPathEnd[(int)RowID]);
		
		TextView SpaceText = new TextView(this);
		SpaceText.setTextSize(20);
		SpaceText.setTextColor(Color.rgb(255,255,255));;
		SpaceText.setText("");
		
		TextView CompanyText = new TextView(this);
		CompanyText.setTextSize(20);
		CompanyText.setTextColor(Color.rgb(255,255,255));;
		CompanyText.setText(mCompany[(int)RowID]);
		
		TextView PhoneText = new TextView(this);
		PhoneText.setTextSize(20);
		PhoneText.setTextColor(Color.rgb(255,255,255));;
		PhoneText.setText("電話 ");
				
		
	    linear.addView(PathStartText);
	    linear.addView(PathPathEndText);
	    linear.addView(SpaceText);
	    linear.addView(CompanyText);
	    linear.addView(PhoneText);


	    builder.setView(linear); 
				
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	private static final String[] mPathStart = new String[] {
		 "國安街56巷121弄","北安路三段412-1號","西門路三段466號"
	};
	
	private static final String[] mPathEnd = new String[] {
		 "國安街156巷156弄"," "," "
	};
	
	private static final String[] mDistance= new String[] {
		 "50","350","450"
	};
	
	private static final Boolean[] mRoadFlating= new Boolean[] {
		 false, false, true
	};
	
	private static final String[] mCompany= new String[] {
		"龍泉水資源股份公司", "宏達電子股份公司", "威盛電子股份公司"
	};
    
}