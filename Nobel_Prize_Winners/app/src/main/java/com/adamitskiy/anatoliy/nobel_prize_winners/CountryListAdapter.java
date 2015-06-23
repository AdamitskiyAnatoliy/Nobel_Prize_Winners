package com.adamitskiy.anatoliy.nobel_prize_winners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anatoliy on 6/18/15.
 */

/*
    Base Adapter for Main Fragment / Country List
 */
public class CountryListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Country> countryList;
    private static final int ID_CONSTANT = 0x1000000;

    public CountryListAdapter(Context _context, ArrayList<Country> _countryList) {
        mContext = _context; countryList = _countryList;
    }

    @Override
    public int getCount() {
        if (countryList != null) {
            return countryList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (countryList != null) {
            return countryList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.country_list_item_layout,
                    parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Country item = (Country) getItem(position);

        holder.countryCode.setText(item.getCountryCode());
        holder.countryName.setText(item.getCountryName());

        switch (position % 5) {
            case 0: holder.countryCode.setBackgroundResource(R.color.orange); break;
            case 1: holder.countryCode.setBackgroundResource(R.color.purple); break;
            case 2: holder.countryCode.setBackgroundResource(R.color.red); break;
            case 3: holder.countryCode.setBackgroundResource(R.color.green); break;
            case 4: holder.countryCode.setBackgroundResource(R.color.blue); break;
            default: holder.countryCode.setBackgroundResource(R.color.orange); break;
        }

        return convertView;
    }

    static class ViewHolder {

        TextView countryCode, countryName;

        public ViewHolder (View v) {

            countryCode = (TextView) v.findViewById(R.id.countryCode);
            countryName = (TextView) v.findViewById(R.id.countryName);
        }
    }
}
