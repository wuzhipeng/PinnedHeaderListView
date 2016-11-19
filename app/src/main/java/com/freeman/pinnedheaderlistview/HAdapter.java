package com.freeman.pinnedheaderlistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freeman on 2016/11/1.
 */

public class HAdapter extends BaseAdapter {
    private List<String> mList = new ArrayList<>();
    private Context context = null;
    public HAdapter(Context c) {
        context = c;
        for(int i = 0; i < 45; i++) {
            mList.add("item" + i);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = new TextView(context);
        }
        String str = mList.get(position);
        ((TextView)convertView).setText("    "+str);
        ((TextView)convertView).setTextSize(40);

        return convertView;
    }
}