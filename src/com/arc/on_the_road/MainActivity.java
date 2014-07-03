package com.arc.on_the_road;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	ImageButton ImageButton_Camera;
	ImageButton ImageButton_Search;
	ImageButton ImageButton_Gralloc;
	ImageButton ImageButton_Phone;	
	ImageButton ImageButton_People;
	
	ImageView   ImageView_preview;
	
	HttpAsyncTask HttpGetData;
	
	Double longitude;
	Double latitude;
	
	String people_NAME;
	String people_EMAIL;
	String people_PHONE;
	String people_ADDRESS;
	
	SharedPreferences prefs;
	
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
	    
	    //init_index_preview();	
	    
	    init_index_ui();
	}	
	
	private void init_index_preview()
	{
		setContentView(R.layout.preview_layout);
		ImageView_preview = (ImageView)findViewById(R.id.index_preview);
		ImageView_preview.setOnClickListener(ImageButtonlistener);
		
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
	            	case R.id.index_preview:
	            		Log.i("On the road index", "preview");
	            		Get_Coordinates();  // get Coordinates
	            		String url = "http://linarnan.co:3000/roadhelper/aloha?lat="+latitude+"&lng="+longitude;
	            		Log.i("On the road index", "lat="+latitude+"&lng="+longitude);
	            		HttpGetData = new HttpAsyncTask();
	            		HttpGetData.execute(url);
	            	break;
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
		            	Intent intent_phone = new Intent();
		            	intent_phone.setClass(MainActivity.this,PhoneActivity.class);
		    			startActivityForResult(intent_phone, ACTIVITY_SELECT_Phone);
		            break;
		            case R.id.index_people:
		            	Log.i("On the road index", "people");
		            	Intent intent_people = new Intent();
		    			intent_people.setClass(MainActivity.this,PeopleActivity.class);
		    			startActivityForResult(intent_people, ACTIVITY_SELECT_People);
		            break;
		        }
		    } 
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_SELECT_CAMERA ) 
		{

			try {

				
				
			} catch (Exception e) {}
		}
		else if (requestCode == ACTIVITY_SELECT_Search ) 
		{

			try {
			} catch (Exception e) {}			
		}
		else if (requestCode == ACTIVITY_SELECT_Grolloc ) 
		{

			try {

			} catch (Exception e) {}			
		}
		else if (requestCode == ACTIVITY_SELECT_People)
		{
			  Intent intent = getIntent(); 
			  people_NAME = intent.getStringExtra("NAME");
			  people_EMAIL = intent.getStringExtra("EMAIL");
			  people_PHONE = intent.getStringExtra("PHONE");
			  people_ADDRESS = intent.getStringExtra("ADDRESS");
			  Log.i("On the road index", "getIntent NAME: "+people_NAME+" EMAIL: "+people_EMAIL+" PHONE: "+people_PHONE+" ADDRESS"+people_ADDRESS);
		}	
	}
	
	public void Get_Coordinates()
	{       
	   LocationManager status=(LocationManager)(this.getSystemService(Context.LOCATION_SERVICE));
	   if(status.isProviderEnabled(LocationManager.GPS_PROVIDER)||status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
	        //if GPS or internet location open， than  call locationServiceInitial() 
	        locationServiceInitial();
	   } 
	   else 
	   {
	        Toast.makeText(this,"請開啟GPS或網路謝",Toast.LENGTH_LONG).show();
	    }
	 
	}
	
	private void locationServiceInitial()
	{
		LocationManager lms=(LocationManager)getSystemService(LOCATION_SERVICE);//取得系統location service
        Criteria criteria=new Criteria();//system provider standard
        criteria.setSpeedRequired(true);
        String bestProvider = lms.getBestProvider(criteria,true);//選擇最高精度        
        Location location=lms.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if(location == null){
//如果抓不到就取得最後一筆有記錄的地點
            location=lms.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            
            if(location != null)
            {
            	longitude= location.getLongitude();//取得經度
            	latitude = location.getLatitude();//取得緯度
            }	
            else	
            	Toast.makeText(this,"無法取得位置",Toast.LENGTH_LONG).show();
        }
        if (location != null){
        	longitude=location.getLongitude();//取得經度
        	latitude = location.getLatitude();//取得緯度
 
        }
    }
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
            return GET_JSON_DATA(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
    		init_index_ui();
       }
        
    	private String  GET_JSON_DATA(String url){
            InputStream inputStream = null;
            String result = "";
            JSONArray jsonArray = null;
            try {
     
                // create HttpClient
                HttpClient httpclient = new DefaultHttpClient();
     
                // make GET request to the given URL
                HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
     
                // receive response as inputStream
                inputStream = httpResponse.getEntity().getContent();
     
                // convert inputstream to string
                if(inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";
            } catch (Exception e) {
                Log.d("InputStream", e.getLocalizedMessage());
            }
            
         // 讀取回應
         		try {
         			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf8"),9999999);
         			//99999為傳流大小，若資料很大，可自行調整
         			StringBuilder sb = new StringBuilder();
         			String line = null;
         			while ((line = reader.readLine()) != null) {
         				//逐行取得資料
         				sb.append(line + "\n");
         			}
         			inputStream.close();
         			result = sb.toString();
         		} catch(Exception e) {
         			e.printStackTrace();
         			Log.i("On the road index", "Can't get data");
         		}
         		//String strJson="{\n\"000000000000000\": [\n    {\n        \"employee_boycode\": \"00\",\n        \"id\": \"000\",\n        \"address\": \"abcdef\",\n        \"name\": \"name\",\n        \"bankcode\": \"abc\",\n        \"branch_name\": \"abcd\",\n        \"account_no\": \"789\"\n    }\n]\n}\n";

         	    /*try {
         	    JSONObject jsnJsonObject = new JSONObject(result);


         	   JSONArray contacts = jsnJsonObject.getJSONArray("hasDigs");

           
         	           for(int i =0; i< contacts.length(); i ++)
         	           { 
         	        	   
         					String id = String.valueOf(i);
         					JSONObject data = contacts.getJSONObject(i);
         					String location = data.getString("location");
         					String comp = data.getString("placeholder");
         					Boolean flag = data.getBoolean("isOnRoadFlatenProject");
         	          //cityid = contacts.getString("city");
         	          //villageid = contacts.getString("village");
         	          //streetid = contacts.getString("street");
         	            Log.i("Parsed data is",":"+location+", "+comp+ ", "+flag);
         	            String YesOrNol;
         	            if(flag = true)
         	            	YesOrNol = "Yes";
         	            else
         	            	YesOrNol = "No";
         	           add(location, comp, YesOrNol);
         	           }   
         	        

         	    } catch (JSONException e) {
                	Log.i("ARC","3");
         	        e.printStackTrace();
         	    }
         		//轉換文字為JSONArray
         		/*try {
         			JSONObject jsnJsonObject = new JSONObject(result);
         		} catch(JSONException e) {
         			Log.i("ARC","2C" + e.getMessage());
         			e.printStackTrace();
         		}*/
     
            return "OK";
        } 
    	
    	
    	private String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;
     
            inputStream.close();
            return result;
     
        }
    }

}
