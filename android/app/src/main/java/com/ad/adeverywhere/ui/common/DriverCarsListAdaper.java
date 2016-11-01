package com.ad.adeverywhere.ui.common;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoCar;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.ui.driver.DriverMyCarsActivity;
import com.ad.adeverywhere.utils.Util;

import java.util.List;

/**
 * Created by Xiaosen_Wang on 9/25/2016.
 */
public class DriverCarsListAdaper extends ArrayAdapter<DemoCar> {
    private final Activity context;
    private List<DemoCar> mAdList = null;

    public DriverCarsListAdaper(Activity context, List<DemoCar> ads) {
        super(context, R.layout.list_item_driver_cars, ads);

        this.context=context;
        this.mAdList = ads;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_item_driver_cars, null,true);

        TextView txBrand = (TextView) rowView.findViewById(R.id.txBrand);
        TextView txModel = (TextView) rowView.findViewById(R.id.txModel);
        ImageView imageLogo = (ImageView) rowView.findViewById(R.id.image_car_logo);
        if(mAdList != null) {
            txBrand.setText(mAdList.get(position).getBrand());
            txModel.setText(mAdList.get(position).getModel());
            imageLogo.setImageResource(R.drawable.test_car1);
            TextView btDelete = (TextView)rowView.findViewById(R.id.btDelete);
            btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove from demo database
                    {
                        DemoDataSource ds = new DemoDataSource(context);
                        ds.open();
                        ds.deleteCarById((int)mAdList.get(position).getId());
                        ds.close();
                    }
                    mAdList.remove(position);
                    notifyDataSetChanged();
                }
            });
        }else{

        }

        return rowView;
    }
}

