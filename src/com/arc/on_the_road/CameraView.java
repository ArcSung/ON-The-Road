package com.arc.on_the_road;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback { 

	public Camera mycamera; 
	public TextView fpsText;
	public CameraViewToDraw vtd;
	
    final int Camera_start_preview = 0;
    final int Camera_stop_preview  = 1;
    final int Camera_save_picture  = 2;
    final int Camera_close_camera  = 3;
	
	int rgb[];
	int locat[];
	int FaceRct[];
	int[] bitmapData;
    int numberOfFaceDetected;
    int x1,x2,y1,y2,correct;
    float myEyesDistance,PoseX,PoseY,PoseZ;
    float X,Y;
    private Bitmap image2;
    Paint whitePaint = new Paint();
    Paint whitePaint2 = new Paint();
    JSONArray jsonArrayMain = null;
    Double longitude;
    Double latitude;
    static String addressid = "";
    static String cityid = "";
    static String villageid = "";
    static String streetid = "";
    
	
	SurfaceHolder mHolder;	
	
	int pickedH, pickedW;
	int monitor_sizeW, monitor_sizeH;

	List<Camera.Size> cameraSize;
	
	public void onButtonCheck(int flag){
		 //this.surfaceDestroyed(mHolder);
		if(flag == Camera_stop_preview)
		{	
			mycamera.stopPreview();
		}	 
		else if(flag == Camera_close_camera)
		{	
			mycamera.stopPreview();
		}
		else if (flag == Camera_start_preview)
		{	
			mycamera.startPreview();
		}
		else if (flag == Camera_save_picture)
		{
		    Toast toast = Toast.makeText(getContext(),"儲存照片",Toast.LENGTH_SHORT);
		    toast.show();
		        
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		    Date curDate = new Date(System.currentTimeMillis());
		    String date = formatter.format(curDate);
		    	
		   
		    	
		    //create the new blank bitmap
		    Bitmap newb = Bitmap.createBitmap( image2.getWidth(), image2.getHeight(), Bitmap.Config.RGB_565 );//創建一個新的和SRC長度寬度一樣的點陣圖
		    Canvas cv = new Canvas( newb );
		    //draw src into
		    
		    cv.drawBitmap( image2, 0, 0, null );//在 0，0座標開始畫入src
		    
			float locationX = monitor_sizeW/15; 
			float locationY = (monitor_sizeH/16)*14;

	    	cv.drawText(date, locationX, locationY, vtd.TimePaint);
	    	
			locationX = monitor_sizeW/15; 
			locationY = (monitor_sizeH/18)*14;
	    	cv.drawText(cityid, locationX, locationY, vtd.CityPaint);
			locationX = (monitor_sizeW/15)*4; 
			locationY = (monitor_sizeH/18)*14;
	    	cv.drawText(villageid, locationX, locationY, vtd.TimePaint);
			locationX = (monitor_sizeW/15); 
			locationY = (monitor_sizeH/18)*15;
	    	cv.drawText(streetid, locationX, locationY, vtd.TimePaint);
	    	
		    cv.save( Canvas.ALL_SAVE_FLAG );//保存
		    	
		    cv.restore();//存儲

			try {
				FileOutputStream fos = new FileOutputStream( "/sdcard/ON_THE_ROAD/Tainna_"+date+".jpg" );
					if ( fos != null )
					{
						newb.compress(Bitmap.CompressFormat.JPEG, 100, fos );
						fos.close();
					}
					    // setWallpaper( bitmap );
				} 
				catch( IOException e )
				{
				    Log.e("testSaveView", "Exception: " + e.toString() );
				}
		}
   }
  
	public CameraView(Context context, CameraViewToDraw _vtd, Context _context, Double longit, Double latitude, int monitor_sizeX, int monitor_sizeY) {
		super(context);
		mHolder = getHolder(); 
		mHolder.addCallback(this); 
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    monitor_sizeW = monitor_sizeX;
	    monitor_sizeH = monitor_sizeY;
		this.vtd = _vtd;
    	String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latitude+","+longit+"&sensor=true&&language=zh-TW";
		//String url = "http://linarnan.co:3000/roadhelper/aloha?lat="+latitude+"&lng="+longit;
		//String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=23.0167143,120.2107831&sensor=true&hl=zh-CN";
		Log.i("ARC",url);
    	new HttpAsyncTask().execute(url);
    	vtd.setAddress(cityid, villageid, streetid);
		vtd.setSize(monitor_sizeY, monitor_sizeX);
		vtd.setPaint();
	}


	public void surfaceCreated(SurfaceHolder holder) { 
		int i, temp;
		mycamera = Camera.open();
		//取得相機所支援的所有解析度
		cameraSize = mycamera.getParameters().getSupportedPreviewSizes();
		if(cameraSize != null){
			//選取最大的攝影機解析度(其實不是很建議用最大解析度, 因為相當吃資源速度也會被拖慢)
			temp = 0;
			for(i=0;i<cameraSize.size();i++){
			if(temp < ((cameraSize.get(i).height) * (cameraSize.get(i).width))){
					pickedH = (cameraSize.get(i).height);
					pickedW = (cameraSize.get(i).width);
					temp = ((cameraSize.get(i).height) * (cameraSize.get(i).width));
				}
			}

		}else{
			Log.e("tag","null");
		};
		try {
			mycamera.setPreviewDisplay(holder); 
			mycamera.setDisplayOrientation(90);
		} catch (IOException e) { 
			e.printStackTrace();
		}
 	}
	
	public void surfaceDestroyed(SurfaceHolder holder) { 
		mycamera.setPreviewCallback(null);
		mycamera.release();
		mycamera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) 
	{ 	
		Camera.Parameters parameters = mycamera.getParameters();
		parameters.setPreviewSize(pickedW, pickedH);
		mycamera.setParameters(parameters);
		
		//產生 buffer
        PixelFormat p = new PixelFormat();
        PixelFormat.getPixelFormatInfo(parameters.getPreviewFormat(),p);
        int bufSize = (pickedW*pickedH*p.bitsPerPixel);
        
        //把buffer給preview callback備用
        byte[] buffer = new byte[bufSize];
        mycamera.addCallbackBuffer(buffer);                            
        buffer = new byte[bufSize];
        mycamera.addCallbackBuffer(buffer);
        buffer = new byte[bufSize];
        mycamera.addCallbackBuffer(buffer);
        //設定預覽畫面更新時的callback
        mycamera.setPreviewCallbackWithBuffer(new PreviewCallback() {
        	
			public void onPreviewFrame(byte[] data, Camera camera) { 
				vtd.putImage(data);
				vtd.CameraSet();
				vtd.setAddress(cityid, villageid, streetid);
				//更新畫布 (call onDraw())
				vtd.invalidate();
				bitmapData = new int[pickedW * pickedH];
			    byte[] rgbBuffer = new byte[pickedW * pickedH * 3];  
			    vtd.decodeYUV420SP(bitmapData, data, pickedW, pickedH);
			    Bitmap image = Bitmap.createBitmap(bitmapData, pickedW, pickedH, Bitmap.Config.RGB_565);
			    int width = image.getWidth();
				int height = image.getHeight();
				Matrix mMatrix = new Matrix();
				mMatrix.setRotate(90);
				image2 =Bitmap.createBitmap(image,0,0,width,height,mMatrix,true);
			    				
	        	mycamera.addCallbackBuffer(data);

			}
		});
        
		mycamera.startPreview();
	}
	
	public class HttpAsyncTask extends AsyncTask<String, Void, String> {
		
		JSONArray jsonArrayMain = null;
	    
	 @Override
	protected String doInBackground(String... urls) 
	{
	   	jsonArrayMain = GET(urls[0]); 
	    return "OK";
	}
	 
	 // onPostExecute displays the results of the AsyncTask.
	 @Override
	protected void onPostExecute(String result) 
	 {
		 
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

	public JSONArray  GET(String url)
	{
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
	 
	 	    try {
	 	    		JSONObject jsnJsonObject = new JSONObject(result);
	 	    		JSONArray contacts = jsnJsonObject.getJSONArray("results");
	 	    		Log.i("Parsed data is",":"+contacts.length());
	 	    		
                    JSONObject js = contacts.getJSONObject(0);
                    JSONArray jaa = (JSONArray) js.get("address_components");
                    
                    JSONObject jss = jaa.getJSONObject(4);
                    cityid = jss.getString("long_name");
                    
                    jss = jaa.getJSONObject(3);
                    villageid = jss.getString("long_name");
                    
                    jss = jaa.getJSONObject(1);
                    streetid = jss.getString("long_name");

                    Log.d(" Parsed data is","cityid="+cityid+" villageid"+villageid+" streetid"+streetid);
	 	        

	 	    } catch (JSONException e) {
	        	Log.i("ARC","3");
	 	        e.printStackTrace();
	 	    }

	    return jsonArray;
	    }
	}

	public void onAutoFocus(boolean success, Camera camera) {
		// TODO Auto-generated method stub
	}
}