package com.adamitskiy.anatoliy.nobel_prize_winners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anatoliy on 6/19/15.
 */

/*
    Base Adapter for Laureate List
 */
public class LaureateListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Laureate> laureateList;
    private static final int ID_CONSTANT = 0x1000000;

    public LaureateListAdapter(Context _context, ArrayList<Laureate> _laureates) {
        mContext = _context;
        laureateList = _laureates;
    }

    @Override
    public int getCount() {
        if (laureateList != null) {
            return laureateList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (laureateList != null) {
            return laureateList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.laureate_list_item_layout,
                    parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Laureate item = (Laureate) getItem(position);
        int color = 0;

        holder.name.setText(item.getFirstName() + " " + item.getLastName());
        holder.birthPlace.setText(item.getBornCity() + ", " + item.getBornCountry());
        holder.born.setText("B: " + item.getBorn());
        holder.died.setText("D: " + item.getDied());

        switch (position % 5) {
            case 0:
                holder.name.setBackgroundResource(R.color.orange);
                color = R.color.orange; break;
            case 1:
                holder.name.setBackgroundResource(R.color.purple);
                color = R.color.purple; break;
            case 2:
                holder.name.setBackgroundResource(R.color.red);
                color = R.color.red; break;
            case 3:
                holder.name.setBackgroundResource(R.color.green);
                color = R.color.green; break;
            case 4:
                holder.name.setBackgroundResource(R.color.blue);
                color = R.color.blue; break;
            default:
                holder.name.setBackgroundResource(R.color.orange);
                color = R.color.orange; break;
        }

        holder.prizeList.setAdapter(new PrizeListAdapter(mContext, color, item.getPrizes()));
        setListViewHeightBasedOnChildren(holder.prizeList);

        return convertView;
    }

    static class ViewHolder {

        TextView name, birthPlace, born, died;
        ListView prizeList;

        public ViewHolder(View v) {

            name = (TextView) v.findViewById(R.id.laureateName);
            birthPlace = (TextView) v.findViewById(R.id.birthPlace);
            born = (TextView) v.findViewById(R.id.birthDate);
            died = (TextView) v.findViewById(R.id.deathDate);
            prizeList = (ListView) v.findViewById(R.id.prizeList);

        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        switch (listAdapter.getCount()) {
            case 1: totalHeight += 110; break;
            case 2: totalHeight += 250; break;
            case 3: totalHeight += 350; break;
                default: break;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    static class PrizeListAdapter extends BaseAdapter {

        private Context mContext;
        private int color;
        private ArrayList<Prize> prizeList;
        private static final int ID_CONSTANT_PRIZE = 0x1000000;

        public PrizeListAdapter (Context _context, int _color, ArrayList<Prize> _prizes) {
            mContext = _context; color = _color; prizeList = _prizes;
        }

        @Override
        public int getCount() {
            if (prizeList != null) {
                return prizeList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (prizeList != null) {
                return prizeList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT_PRIZE + position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            PrizeViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.prize_item_layout,
                        parent, false);
                holder = new PrizeViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (PrizeViewHolder) convertView.getTag();
            }

            Prize item = (Prize) getItem(position);

            holder.year.setText(item.getYear());
            holder.year.setBackgroundResource(color);
            holder.category.setText(item.getCategory());
            holder.motivation.setText(item.getMotivation());

            switch (color) {
                case R.color.orange:
                    holder.winner.setBackgroundResource(R.mipmap.ic_person_orange); break;
                case R.color.purple:
                    holder.winner.setBackgroundResource(R.mipmap.ic_person_purple); break;
                case R.color.red:
                    holder.winner.setBackgroundResource(R.mipmap.ic_person_red); break;
                case R.color.green:
                    holder.winner.setBackgroundResource(R.mipmap.ic_person_green); break;
                case R.color.blue:
                    holder.winner.setBackgroundResource(R.mipmap.ic_person_blue); break;
            }

            switch (Integer.parseInt(item.getShare())) {
                case 1:
                    holder.winner2.setBackgroundColor(android.R.color.transparent);
                    holder.winner3.setBackgroundColor(android.R.color.transparent);
                    holder.winner4.setBackgroundColor(android.R.color.transparent);
                    break;
                case 2:
                    holder.winner2.setBackgroundResource(R.mipmap.ic_person_grey);
                    break;
                case 3:
                    holder.winner2.setBackgroundResource(R.mipmap.ic_person_grey);
                    holder.winner3.setBackgroundResource(R.mipmap.ic_person_grey);
                    break;
                case 4:
                    holder.winner2.setBackgroundResource(R.mipmap.ic_person_grey);
                    holder.winner3.setBackgroundResource(R.mipmap.ic_person_grey);
                    holder.winner4.setBackgroundResource(R.mipmap.ic_person_grey);
                    break;
            }

            return convertView;
        }
    }

    static class PrizeViewHolder {

        TextView year, category, motivation;
        ImageView winner, winner2, winner3, winner4;

        public PrizeViewHolder(View v) {

            year = (TextView) v.findViewById(R.id.prizeYear);
            category = (TextView) v.findViewById(R.id.prizeCategory);
            motivation = (TextView) v.findViewById(R.id.prizeMotivation);

            winner = (ImageView) v.findViewById(R.id.winner1);
            winner2 = (ImageView) v.findViewById(R.id.winner2);
            winner3 = (ImageView) v.findViewById(R.id.winner3);
            winner4 = (ImageView) v.findViewById(R.id.winner4);

        }
    }
}
