package com.assignment.e_wallet_project.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.assignment.e_wallet_project.R;

public class ListAdapter extends BaseAdapter {
        int[] frame, icon;
        Context context;

        ListAdapter(Context context){
            frame = null;
            icon = null;
            this.context =context;
        }

        public ListAdapter(int[] frames, int[] icons, Context context) {
            frame = frames;
            icon = icons;
            this.context = context;
        }

        @Override
        public int getCount() {
            return frame.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater listInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View customizedList;
            customizedList = listInflater.inflate(R.layout.customized_list, parent, false);
            ImageView iconImage = (ImageView)customizedList.findViewById(R.id.icon);
            FrameLayout frameImage = (FrameLayout)customizedList.findViewById(R.id.frame);
            iconImage.setImageResource(icon[position]);
            frameImage.setBackgroundResource(frame[position]);
            return customizedList;
        }
    }