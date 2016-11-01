package com.ad.adeverywhere.ui.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.adeverywhere.R;

/**
 * Created by Xiaosen_Wang on 9/17/2016.
 */
public class MeListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private  Integer[] mTitle = null;
    private Integer[] mDescription = null;

    public MeListAdapter(Activity context, Integer[] title, Integer[] desc, int size){
        super(context, R.layout.me_list_item, new String[size]);
        this.context = context;
        mTitle = title;
        mDescription = desc;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.me_list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.tvTitle);
        TextView extratxt = (TextView) rowView.findViewById(R.id.tvDesc);

        txtTitle.setText(context.getString(mTitle[position]));
        extratxt.setText(context.getString(mDescription[position]));
        return rowView;

    };
}

