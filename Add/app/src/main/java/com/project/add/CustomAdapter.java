package com.project.add;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by user on 2016-01-02.
 */
public class CustomAdapter extends BaseAdapter {

    Context mContext;

    ArrayList<Address> data;

    public CustomAdapter(Context context, ArrayList<Address> data){

        this.mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView==null)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            convertView=inflater.inflate(R.layout.list_address_layout, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.textViewStreet =(TextView) convertView.findViewById(R.id.textViewStreet);
            viewHolder.textViewLocale =(TextView) convertView.findViewById(R.id.textViewLocale);
            viewHolder.textViewCity=(TextView)convertView.findViewById(R.id.textViewCity);
            viewHolder.textViewCountry=(TextView)convertView.findViewById(R.id.textViewCountry);
            viewHolder.textViewLongitude=(TextView)convertView.findViewById(R.id.textViewLongitude);
            viewHolder.textViewLatitude=(TextView)convertView.findViewById(R.id.textViewLatitude);
            convertView.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Address address = data.get(position);
        viewHolder.textViewStreet.setText(address.getStreet());
        viewHolder.textViewLocale.setText(address.getLocal());
        viewHolder.textViewCity.setText(address.getCity());
        viewHolder.textViewCountry.setText(address.getCountry());
        viewHolder.textViewLongitude.setText(address.getLongitude());
        viewHolder.textViewLatitude.setText(address.getLatitude());

        return convertView;
    }

    static class ViewHolder {
        TextView textViewStreet;
        TextView textViewLocale;
        TextView textViewCity;
        TextView textViewCountry;
        TextView textViewLongitude;
        TextView textViewLatitude;
    }
}
