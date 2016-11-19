package com.freeman.pinnedheaderlistview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PinnedListViewActivity extends AppCompatActivity  implements PinnedHeaderListView.IGetListView {
    PinnedHeaderListView pinnedHeaderListView;
    ListView listView;
    HorizontalListView mHorizontalListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_listview);
        pinnedHeaderListView = (PinnedHeaderListView) findViewById(R.id.pinnedHeaderListView);

        pinnedHeaderListView.setListView(this);
        final LinearLayout header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.mode_layout, null);

        mHorizontalListView = (HorizontalListView) header.findViewById(R.id.category_list_on_pinned);
        mHorizontalListView.setAdapter(new HAdapter(this));
        pinnedHeaderListView.setPinnedHeader(header);
    }

    @Override
    public ViewGroup getListView() {
        listView = (ListView) LayoutInflater.from(this).inflate(R.layout.listview_layout, pinnedHeaderListView, false);
        listView.addHeaderView(createHeaderFooterView(listView, Color.parseColor("#ff3ff1"), "this is header 1"));
        listView.addHeaderView(createHeaderFooterView(listView, Color.parseColor("#f3ffa1"), "this is header 2"));
        listView.addHeaderView(createHeaderFooterView(listView, Color.parseColor("#3fffa1"), "this is header 3"));
        listView.addHeaderView(createHeaderFooterView(listView, Color.parseColor("#4fffa1"), "this is header 4"));
        listView.addHeaderView(createHeaderFooterView(listView, Color.parseColor("#4f11a1"), "this is header 5"));
        listView.addHeaderView(createHeaderFooterView(listView, Color.parseColor("#4111a1"), "this is header 6"));
        listView.addHeaderView(createHeaderFooterView(listView, Color.parseColor("#41ffa1"), "this is header 7"));
        listView.setAdapter(new HAdapter(this));
        return listView;

    }

    private View createHeaderFooterView(ViewGroup viewRoot, int color, String text) {
        // inflate方法最后的一个参数false表示，viewRoot只用来为R.layout.item_layout的rootView设置正确的LayoutParams。
        View header = LayoutInflater.from(this).inflate(R.layout.item_layout, viewRoot, false);
        TextView textView = (TextView) header.findViewById(R.id.text);
        textView.setBackgroundColor(color);
        textView.setText(text);
        ViewGroup.LayoutParams lp = textView.getLayoutParams();
        lp.height = 300;
        textView.setLayoutParams(lp);
        return header;
    }
}
