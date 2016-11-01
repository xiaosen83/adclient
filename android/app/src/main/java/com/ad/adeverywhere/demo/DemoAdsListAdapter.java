package com.ad.adeverywhere.demo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.Ad;
import com.ad.adeverywhere.utils.ImageLoadTask;
import com.ad.adeverywhere.utils.Util;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Xiaosen_Wang on 10/10/2016.
 */

public class DemoAdsListAdapter extends ArrayAdapter<DemoAD> {
    private final Activity context;
    private List<DemoAD> mAdList = null;
    public DemoAdsListAdapter(Activity context, List<DemoAD> ads) {
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
        TextView txtStart = (TextView) rowView.findViewById(R.id.ad_list_start);

        if(mAdList != null) {
            txtTitle.setText(mAdList.get(position).getCompany());
            imageView.setImageResource(Util.getResourceId(mAdList.get(position).getLogo(), R.drawable.class));
            //new ImageLoadTask(mAdList.get(position).getLogo(), imageView).execute();
            txtPeriod.setText(String.format(context.getString(R.string.period_format), mAdList.get(position).getPeriod()));
            txtStatus.setText(Util.statusToString(context, mAdList.get(position).getStatus()));
            txtStart.setText(mAdList.get(position).getStartDate());
            int total = mAdList.get(position).getCars();
            int current = mAdList.get(position).getCarsNow();
            txtCars.setText(String.format(context.getString(R.string.car_left_format), total-current, total));
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
