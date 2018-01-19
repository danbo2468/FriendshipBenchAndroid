package com.hanze.wad.friendshipbench.Controllers;

import com.hanze.wad.friendshipbench.Models.Appointment;
import com.hanze.wad.friendshipbench.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Appointment> objects;

    private class ViewHolder {
        TextView title;
        TextView subTitle;
        ImageView icon;
    }

    public CustomListAdapter(Context context, ArrayList<Appointment> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }

    public Appointment getItem(int position) {
        return objects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            holder.title = convertView.findViewById(R.id.title);
            holder.subTitle = convertView.findViewById(R.id.subTitle);
            holder.icon = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(objects.get(position).getSummary());
        holder.subTitle.setText(objects.get(position).getReadableTime());
        holder.icon.setImageResource(objects.get(position).getStatusIcon());
        return convertView;
    }
}
