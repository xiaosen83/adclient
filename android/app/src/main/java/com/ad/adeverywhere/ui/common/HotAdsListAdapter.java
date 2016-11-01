package com.ad.adeverywhere.ui.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.utils.Util;

import java.util.List;

/**
 * Created by Xiaosen_Wang on 10/15/2016.
 */

public class HotAdsListAdapter extends ArrayAdapter<DemoAD> {
    private final Activity context;
    private List<DemoAD> mAdList = null;
    public HotAdsListAdapter(Activity context, List<DemoAD> ads) {
        super(context, R.layout.ad_list_item, ads);

        this.context=context;
        this.mAdList = ads;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item_hot_ad, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.ad_list_desc);
        ImageView logoView = (ImageView) rowView.findViewById(R.id.ad_list_icon);
        TextView txtTotal = (TextView) rowView.findViewById(R.id.ad_list_cars);
        TextView txtStatus = (TextView) rowView.findViewById(R.id.ad_list_status);
        TextView txtPeriod = (TextView) rowView.findViewById(R.id.ad_list_period);

        if(mAdList != null) {
            txtTitle.setText(mAdList.get(position).getCompany());
            logoView.setImageResource(Util.getResourceId(mAdList.get(position).getLogo(), R.drawable.class));
            //new ImageLoadTask(mAdList.get(position).getLogo(), imageView).execute();
            txtTotal.setText(String.format(context.getString(R.string.car_format), mAdList.get(position).getCars()));
            txtPeriod.setText(String.format(context.getString(R.string.period_format), mAdList.get(position).getPeriod()));
            txtStatus.setText(statusToString(mAdList.get(position).getStatus()));
        }else{

        }
        return rowView;
    }

    private String statusToString(int status){
        if(status == DemoAD.AD_STATUS_PENDING)
            return context.getString(R.string.ad_status_pending);
        else if(status == DemoAD.AD_STATUS_READY)
            return context.getString(R.string.ad_status_ready);
        else if(status == DemoAD.AD_STATUS_FINISHED)
            return context.getString(R.string.ad_status_finished);
        return "";
    }

    @Override
    public DemoAD getItem(int position) {
        if(mAdList != null)
            return mAdList.get(position);
        else
            return null;
    }
}
