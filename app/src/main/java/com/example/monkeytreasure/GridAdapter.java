package com.example.monkeytreasure;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class GridAdapter extends BaseAdapter {
    private Context myContext;
    private myImage[] images;
    private Integer [] myCols, myRows;


    public  GridAdapter(myImage[] images, Context myContext, Integer[] myCols, Integer[] myRows) {
        this.images = images;
        this.myContext = myContext;
        this.myCols = myCols;
        this.myRows = myRows;
    }

    @Override
    public int getCount() {
        return myCols.length*myRows.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }


    @Override
    public long getItemId(int position) {
        return images[position].getIndex();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(myContext);
            imageView.setImageResource(images[position].getImage());

        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setLayoutParams(new GridView.LayoutParams(50, 70));
        imageView.setPadding(1, 1, 1, 1);
        imageView.setId(position);
        return imageView;
    }

    public Integer getMyCols() {
        return myCols.length;
    }

    public Integer getMyRows() {
        return myRows.length;
    }


    public Integer getMyRow(int place) {
        return myRows[place];
    }
}



