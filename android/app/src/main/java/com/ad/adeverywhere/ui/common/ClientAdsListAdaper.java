package com.ad.adeverywhere.ui.common;

import android.app.Activity;
import android.content.Intent;
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
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.ui.client.ClientEditAdActivity;
import com.ad.adeverywhere.ui.client.ClientMyAdsActivity;
import com.ad.adeverywhere.utils.ImageLoadTask;
import com.ad.adeverywhere.utils.Util;

import java.util.List;

/**
 * Created by Xiaosen_Wang on 9/25/2016.
 */
public class ClientAdsListAdaper extends ArrayAdapter<DemoAD> {
    private final Activity context;
    private List<DemoAD> mAdList = null;

    public ClientAdsListAdaper(Activity context, List<DemoAD> ads) {
        super(context, R.layout.ad_list_driver_item, ads);

        this.context=context;
        this.mAdList = ads;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.ad_list_client_item, null,true);

        TextView txtDescription = (TextView) rowView.findViewById(R.id.ad_list_desc);
        TextView txtCars = (TextView) rowView.findViewById(R.id.ad_list_cars);
        TextView txtPeriod = (TextView) rowView.findViewById(R.id.ad_list_period);
        TextView txtStatus = (TextView) rowView.findViewById(R.id.ad_list_status);
        ImageView imageModle = (ImageView) rowView.findViewById(R.id.image_ad_modle);
        ImageView imageLogo = (ImageView) rowView.findViewById(R.id.image_ad_logo);
        TextView txtStartDate = (TextView) rowView.findViewById(R.id.ad_list_start);
        LinearLayout editLayout = (LinearLayout) rowView.findViewById(R.id.editLayout);
        editLayout.setVisibility(View.GONE);
        if(mAdList != null) {
            txtDescription.setText(mAdList.get(position).getCompany());
            //imageView.setImageResource(imgid[position]);
            txtCars.setText(String.format(context.getString(R.string.car_format), mAdList.get(position).getCars()));
            txtPeriod.setText(String.format(context.getString(R.string.period_format), mAdList.get(position).getPeriod()));
            txtStatus.setText(Util.statusToString(context,mAdList.get(position).getStatus()));
            txtStartDate.setText(mAdList.get(position).getStartDate());
            if(mAdList.get(position).getLogo().length() == 0)
                imageLogo.setImageResource(R.drawable.logo_placehoder);
            else
                imageLogo.setImageResource(Util.getResourceId(mAdList.get(position).getLogo(), R.drawable.class));
            if(mAdList.get(position).getModel().length() == 0)
                imageModle.setImageResource(R.drawable.ad_placeholder);
            else
                imageModle.setImageResource(Util.getResourceId(mAdList.get(position).getModel(), R.drawable.class));
            if(mAdList.get(position).getStatus() == DemoAD.AD_STATUS_PENDING){
                //show 'edit/delete' button for not started AD
                editLayout.setVisibility(View.VISIBLE);
                //setup edit/delete action
                TextView btEdit = (TextView)rowView.findViewById(R.id.btEdit);
                TextView btDelete = (TextView)rowView.findViewById(R.id.btDelete);
                btEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ClientEditAdActivity.class);
                        i.putExtra(ClientEditAdActivity.TARGET_ADID, mAdList.get(position).getId());
                        i.putExtra(ClientEditAdActivity.ACTION_TYPE, ClientEditAdActivity.ACTION_TYPE_EDIT);
                        context.startActivityForResult(i, ClientMyAdsActivity.ACTIVITY_REQ_EDITAD);
                    }
                });
                btDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        {
                            DemoDataSource ds = new DemoDataSource(context);
                            ds.open();
                            ds.deleteAD((int)mAdList.get(position).getId());
                            ds.close();
                        }
                        mAdList.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        }else{

        }

        return rowView;
    }
}

