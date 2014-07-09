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
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

public class SearchActivity extends Activity {
	
	private ListView listData = null;	
	JSONArray jsonArrayMain = null;
	JSONArray jsonArrayMain2 = null;
	ImageButton    pepole_info_button;
	ImageButton    search_button;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(1);
	
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
        setContentView(R.layout.search_layout);
        
        initView();
        
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
				  // showInList();

				}
		});

		
    }
	
	public boolean onTouchEvent(MotionEvent event)
	{
        //if(event.getAction() == MotionEvent.ACTION_UP){
        int X= (int)event.getX();
        int Y= (int)event.getY();
        Log.i("Arc","X"+X+"Y"+Y);

        	//showInList();        
        //}

		return false;  	
   }
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void Search_info(Double longitude, Double latitude){
		
		String url = "http://linarnan.co:3000/roadhelper/aloha?lat="+latitude+"&lng="+longitude;
    	new HttpAsyncTask().execute(url);

	}
	

	private void initView(){
		listData = (ListView) findViewById(R.id.listData);
    	listData.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long id) {
				// TODO Auto-generated method stub
				SQLdb_Dialog(id);
				return false;
			}
			
		});
	}
	

	    

    

    




	

	private void SQLdb_Dialog(final long RowID)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//init SQL
	    //Cursor cursor = fetehData(RowID);
    	    	
    	//final String Path_db   = cursor.getString(1);
    	//final String Company_db   = cursor.getString(2);
    	//String Date_db = cursor.getString(3);
		
		// Setting_Dialog UI set up
		LinearLayout linear=new LinearLayout(this);
		linear.setOrientation(1);
		
		linear.setBackgroundColor(Color.rgb(60,60, 60));
		ImageView PhotoImage = new ImageView(this);	
		PhotoImage.setImageResource(R.drawable.pushcar);
		linear.addView(PhotoImage);
		/*if(Date_db == "Yes")
		{	
			TextView road = new TextView(this);
			road.setTextSize(20);
			road.setTextColor(Color.rgb(255,255,0));;
			road.setText("路平專案中");
			linear.addView(road);
		}
				
		TextView DateText = new TextView(this);
		DateText.setTextSize(20);
		DateText.setTextColor(Color.rgb(255,255, 255));;
		DateText.setText("路段: "+Path_db);
				
		TextView TimeText = new TextView(this);
		TimeText.setTextSize(20);
		TimeText.setTextColor(Color.rgb(255,255, 255));;
		TimeText.setText("公司: "+Company_db);
			
					
		
	    linear.addView(DateText);
	    linear.addView(TimeText);


	    builder.setView(linear); */
				
	    
	    AlertDialog setting_dialog = builder.create();
	    setting_dialog.show();
	}
	
	public static JSONArray  GET(String url){
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
     		}
     		//String strJson="{\n\"000000000000000\": [\n    {\n        \"employee_boycode\": \"00\",\n        \"id\": \"000\",\n        \"address\": \"abcdef\",\n        \"name\": \"name\",\n        \"bankcode\": \"abc\",\n        \"branch_name\": \"abcd\",\n        \"account_no\": \"789\"\n    }\n]\n}\n";

     	    try {
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
     	           //add(location, comp, YesOrNol);
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
 
        return jsonArray;
    }
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
	
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
 
        	jsonArrayMain = GET(urls[0]); 
            return "OK";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

       }
    }
    
}