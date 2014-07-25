package com.arc.on_the_road;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

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
     private ViewPager imageView;
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
          imageView = (ViewPager) findViewById(R.id.imageView1);

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
          //Bitmap bm = BitmapFactory.decodeFile(imagePaths.get(position));
          //imageView.setImageBitmap(bm);
          imageView.setAdapter(new SamplePagerAdapter(GridviewActivity.this, imagePaths, position));
          imageView.setCurrentItem(position);    //setup Image 為當初gridview點選的圖片 
          imageView.setVisibility(View.VISIBLE);
          gridView.setVisibility(View.GONE);
     }
     
 	static class SamplePagerAdapter extends PagerAdapter {

		//private static final int[] sDrawables = { R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
		//		R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper };
		
		private List<String> mListViews;
		private int view_position;
		
	    public SamplePagerAdapter(Context context, List<String> mListViews, int position) 
	    {  
	           this.mListViews = mListViews;
	           this.view_position = position;
	    }  

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			//photoView.setImageResource(sDrawables[position]);
			photoView.setImageBitmap(BitmapFactory.decodeFile(mListViews.get(position)));

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
