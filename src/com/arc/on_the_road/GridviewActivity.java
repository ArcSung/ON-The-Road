package com.arc.on_the_road;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * MainActivity 
 * 	取得SD卡圖片, 並給予id
 *  thumbs  存放縮圖的id
 *  imagePaths  存放圖片的路徑
 * ImageAdapter (Context context, List coll)
 *  coll = MainActivity.thumbs
 */

public class GridviewActivity extends Activity {

     private GridView gridView;
     private ImageView imageView;
     private List<String> thumbs;  //存放縮圖的id
     private List<String> imagePaths;  //存放圖片的路徑
     private GridviewAdapter imageAdapter;  //用來顯示縮圖

     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          
          
  		  requestWindowFeature(Window.FEATURE_NO_TITLE);
  	
  	      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
  	      Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
          setContentView(R.layout.gridview_layout);

          gridView = (GridView) findViewById(R.id.gridView1);
          imageView = (ImageView) findViewById(R.id.imageView1);

          ContentResolver cr = getContentResolver();
          String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };  //初始 欄位  DATA 代表路徑

          //查詢SD卡的圖片
          Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null, null);

          thumbs = new ArrayList<String>();
          imagePaths = new ArrayList<String>();

          for (int i = 0; i < cursor.getCount(); i++) {
        	  cursor.moveToPosition(i);
              String filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//抓路徑
              
              if (filepath.indexOf("ON_THE_ROAD") != -1)        //選擇特定路徑
              {
            	  imagePaths.add(filepath);
            	  int id = cursor.getInt(cursor
                         .getColumnIndex(MediaStore.Images.Media._ID));// ID
            	  thumbs.add(id + "");
              }
          }

          cursor.close();

          imageAdapter = new GridviewAdapter(GridviewActivity.this, thumbs);
          gridView.setAdapter(imageAdapter);
          imageAdapter.notifyDataSetChanged();


          imageView.setOnClickListener(new OnClickListener() { //在點小圖後 顯示的大圖的觸控事件

               @Override
               public void onClick(View v) {
                    // TODO Auto-generated method stub
                    imageView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
               }

          });
          imageView.setVisibility(View.GONE);

     }

     public void setImageView(int position){
          Bitmap bm = BitmapFactory.decodeFile(imagePaths.get(position));
          imageView.setImageBitmap(bm);
          imageView.setVisibility(View.VISIBLE);
          gridView.setVisibility(View.GONE);
     }
}
