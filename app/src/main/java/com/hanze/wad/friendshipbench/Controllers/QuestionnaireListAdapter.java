package com.hanze.wad.friendshipbench.Controllers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanze.wad.friendshipbench.Models.Appointment;
import com.hanze.wad.friendshipbench.Models.Questionnaire;
import com.hanze.wad.friendshipbench.R;

import java.util.ArrayList;

public class QuestionnaireListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Questionnaire> objects;

    private class ViewHolder {
        TextView title;
        TextView subTitle;
        ImageView icon;
    }

    public QuestionnaireListAdapter(Context context, ArrayList<Questionnaire> objects) {
        inflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public int getCount() {
        return objects.size();
    }

    public Questionnaire getItem(int position) {
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
        holder.title.setText("Questionnaire " + objects.get(position).getId());
        holder.subTitle.setText(objects.get(position).getFancyTime());
        holder.icon.setColorFilter(Color.argb(255, 196, 42, 52));
        if(!objects.get(position).isRedflag())
            holder.icon.setImageResource(0);
        else
            holder.icon.setImageResource(objects.get(position).getRedFlagIcon());
        return convertView;
    }
}
