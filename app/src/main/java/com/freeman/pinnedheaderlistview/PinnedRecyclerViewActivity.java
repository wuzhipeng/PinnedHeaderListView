package com.freeman.pinnedheaderlistview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PinnedRecyclerViewActivity extends AppCompatActivity implements PinnedHeaderListView.IGetListView{
    RecyclerView recyclerView;
    HorizontalListView mHorizontalListView;
    PinnedHeaderListView pinnedHeaderListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_recycler_view);
        pinnedHeaderListView = (PinnedHeaderListView) findViewById(R.id.pinnedHeaderListView);

        pinnedHeaderListView.setListView(this);
        final LinearLayout header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.mode_layout, null);

        mHorizontalListView = (HorizontalListView) header.findViewById(R.id.category_list_on_pinned);
        mHorizontalListView.setAdapter(new HAdapter(this));
        pinnedHeaderListView.setPinnedHeader(header);
    }

    @Override
    public ViewGroup getListView() {
        recyclerView = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.recyclerview, pinnedHeaderListView, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
     //   RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
     //   RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Adapter adapter = new Adapter(this, recyclerView);
        WrapAdapter mWrapAdapter = new WrapAdapter(adapter);
        View header1 = createHeaderFooterView(recyclerView, Color.parseColor("#ff3ff1"), "this is header 1");
        View header2 = createHeaderFooterView(recyclerView, Color.parseColor("#f3ffa1"), "this is header 2");
        View header3 = createHeaderFooterView(recyclerView, Color.parseColor("#3fffa1"), "this is header 3");
        View header4 = createHeaderFooterView(recyclerView, Color.parseColor("#4fffa1"), "this is header 4");
        View header5 = createHeaderFooterView(recyclerView, Color.parseColor("#4f11a1"), "this is header 5");
        View header6 = createHeaderFooterView(recyclerView, Color.parseColor("#4111a1"), "this is header 6");
        View header7 = createHeaderFooterView(recyclerView, Color.parseColor("#41ffa1"), "this is header 7");
        mWrapAdapter.addHeaderView(header1);
        mWrapAdapter.addHeaderView(header2);
        mWrapAdapter.addHeaderView(header3);
        mWrapAdapter.addHeaderView(header4);
        mWrapAdapter.addHeaderView(header5);
        mWrapAdapter.addHeaderView(header7);

        recyclerView.setAdapter(mWrapAdapter);
        return recyclerView;
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
