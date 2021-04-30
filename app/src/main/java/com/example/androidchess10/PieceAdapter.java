package com.example.androidchess10;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
            imageView.setLayoutParams(new GridView.LayoutParams(80, 80));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
            int[] pos = MainActivity.translate(position);
            if (pos[0] % 2 == 0) {
                if (pos[1] % 2 == 1) { imageView.setBackgroundColor(mContext.getColor(R.color.board_white)); }
                else { imageView.setBackgroundColor(mContext.getColor(R.color.board_black)); }
            } else {
                if (pos[1] % 2 == 0) { imageView.setBackgroundColor(mContext.getColor(R.color.board_white)); }
                else { imageView.setBackgroundColor(mContext.getColor(R.color.board_black));}
            }

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
            R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank,
            R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank,
            R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank,
            R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank,
            R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank,
            R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank,
            R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank,
            R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank, R.drawable.ic_blank
    };

    //public Integer[] mThumbIds = new Integer[64];

}
