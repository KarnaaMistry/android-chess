package com.example.androidchess10;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PieceAdapter extends BaseAdapter{
    private Context mContext;

    public PieceAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(64, 64));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public Integer getImgID(int position){
        return mThumbIds[position];
    }

    public Integer[] mThumbIds = {
            R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black,
            R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white,
            R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black,
            R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white,
            R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black,
            R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white,
            R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black,
            R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white, R.drawable.ic_black, R.drawable.ic_white
    };

}
