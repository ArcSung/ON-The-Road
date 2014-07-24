package com.arc.on_the_road;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class GridviewAdapter extends BaseAdapter {

    private ViewGroup layout;
    private Context context;
    private List coll;   //存放縮圖的id

    public GridviewAdapter(Context context, List coll) {

        super();
        this.context = context;
        this.coll = coll;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowview = inflater.inflate(R.layout.gridview_item_photo, parent, false);
        layout = (ViewGroup) rowview.findViewById(R.id.rl_item_photo);
        ImageView imageView = (ImageView) rowview.findViewById(R.id.imageView1);

        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float dd = dm.density;
        float px = 25 * dd;
        float screenWidth = dm.widthPixels;
        int newWidth = (int) (screenWidth - px) / 2; // 一行顯示2個縮圖

        layout.setLayoutParams(new GridView.LayoutParams(newWidth, newWidth));
        imageView.setId(position);
        // Bitmap bm = BitmapFactory.decodeFile((String)coll.get(position));
        // Bitmap newBit = Bitmap.createScaledBitmap(bm, newWidth, newWidth,
        // true);

        Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context
                .getApplicationContext().getContentResolver(), Long
                .parseLong((String) coll.get(position)),
                MediaStore.Images.Thumbnails.MICRO_KIND, null);

        imageView.setImageBitmap(bm);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        //點擊小照片的事件
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "index:" + position, Toast.LENGTH_SHORT)
                        .show();

                ((GridviewActivity)context).setImageView(position);
            }

        });

        return rowview;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return coll.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return coll.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}