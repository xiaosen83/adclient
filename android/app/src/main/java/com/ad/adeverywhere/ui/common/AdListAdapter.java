package com.ad.adeverywhere.ui.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.Ad;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.utils.ImageLoadTask;

import java.util.List;

/**
 * Created by Xiaosen_Wang on 9/17/2016.
 */
public class AdListAdapter extends ArrayAdapter<Ad> {
    private final Activity context;
    private List<Ad> mAdList = null;

    public AdListAdapter(Activity context, List<Ad> ads, Integer size) {
        super(context, R.layout.ad_list_item, ads);

        this.context=context;
        this.mAdList = ads;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.ad_list_item, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.ad_list_desc);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.ad_list_icon);
        TextView txtCars = (TextView) rowView.findViewById(R.id.ad_list_cars);
        TextView txtStatus = (TextView) rowView.findViewById(R.id.ad_list_status);
        TextView txtPeriod = (TextView) rowView.findViewById(R.id.ad_list_period);

        if(mAdList != null) {
            txtTitle.setText(mAdList.get(position).getCompany());
            //imageView.setImageResource(imgid[position]);
            new ImageLoadTask(mAdList.get(position).getLogoUri(), imageView).execute();
            //txtCars.setText(String.format(context.getString(R.string.car_left_format), mAdList.get(position).getCars()));
            //txtPeriod.setText(String.format(context.getString(R.string.period_format), mAdList.get(position).getPeriod()));
            //txtStatus.setText(statusToString(mAdList.get(position).getStatus()));
        }else{

        }
        return rowView;
    }

    @Override
    public Ad getItem(int position) {
        if(mAdList != null)
            return mAdList.get(position);
        else
            return null;
    }
}
