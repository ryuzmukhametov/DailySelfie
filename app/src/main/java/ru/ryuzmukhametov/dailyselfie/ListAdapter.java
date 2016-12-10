package ru.ryuzmukhametov.dailyselfie;

/**
 * Created by ryuzmukhametov on 10/12/16.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapter extends ArrayAdapter<ListAdapter.Item> {

    static public class Item {
        String mFileName;
        String mFullPath;
        public Item(String fileName, String fullPath) {
            this.mFileName = fileName;
            this.mFullPath = fullPath;
        }
    }

    private final Activity context;
    private final Item[] items;

    public ListAdapter(Activity context, Item[] items) {
        super(context, R.layout.mylist, items);
        this.context = context;
        this.items = items;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.mylist, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(items[position].mFileName);

        ImageUtils.setPic(imageView, items[position].mFullPath, 50, 50);

        return rowView;

    };
}