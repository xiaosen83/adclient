package com.ad.adeverywhere.ui.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.Ad;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.utils.ImageLoadTask;
import com.ad.adeverywhere.utils.Util;

import java.util.List;

/**
 * Created by Xiaosen_Wang on 9/25/2016.
 */
public class MyAdsListAdaper extends ArrayAdapter<DemoAD> {
    private final Activity context;
    private List<DemoAD> mAdList = null;

    public MyAdsListAdaper(Activity context, List<DemoAD> ads) {
        super(context, R.layout.ad_list_driver_item, ads);

        this.context=context;
        this.mAdList = ads;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.ad_list_driver_item, null,true);

        TextView txtDescription = (TextView) rowView.findViewById(R.id.ad_list_desc);
        ImageView imageModle = (ImageView) rowView.findViewById(R.id.image_ad_modle);
        ImageView imageLogo = (ImageView) rowView.findViewById(R.id.image_ad_logo);
        TextView txtStartDate = (TextView) rowView.findViewById(R.id.ad_list_start);
        TextView txtCars = (TextView) rowView.findViewById(R.id.ad_list_cars);
        TextView txtPeriod = (TextView) rowView.findViewById(R.id.ad_list_period);
        TextView txtStatus = (TextView) rowView.findViewById(R.id.ad_list_status);
        TextView txtExpectIncome = (TextView) rowView.findViewById(R.id.ad_list_income);
        LinearLayout editLayout = (LinearLayout) rowView.findViewById(R.id.editLayout);
        editLayout.setVisibility(View.GONE);

        if(mAdList != null) {
            txtDescription.setText(mAdList.get(position).getCompany());
            txtCars.setText(String.format(context.getString(R.string.car_format), mAdList.get(position).getCars()));
            txtPeriod.setText(String.format(context.getString(R.string.period_format), mAdList.get(position).getPeriod()));
            txtStatus.setText(Util.statusToString(context,mAdList.get(position).getStatus()));
            txtStartDate.setText(mAdList.get(position).getStartDate());
            imageLogo.setImageResource(Util.getResourceId(mAdList.get(position).getLogo(), R.drawable.class));
            imageModle.setImageResource(Util.getResourceId(mAdList.get(position).getModel(), R.drawable.class));
            txtExpectIncome.setText(String.valueOf(mAdList.get(position).getPeriod()*300));
            if(mAdList.get(position).getStatus() == DemoAD.AD_STATUS_PENDING){
                //show 'edit/delete' button for not started AD
                editLayout.setVisibility(View.VISIBLE);
            }
        }else{

        }
        return rowView;
    }
}

