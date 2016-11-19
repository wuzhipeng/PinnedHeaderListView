package com.freeman.pinnedheaderlistview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PinnedRecyclerViewActivity extends AppCompatActivity implements PinnedHeaderListView.IGetListView, View.OnClickListener{
    RecyclerView recyclerView;
    HorizontalListView mHorizontalListView;
    PinnedHeaderListView pinnedHeaderListView;

    Button btnLinear;
    Button btnStagger;
    Button btnGrid;

    RecyclerView.LayoutManager linearLayoutManager;
    RecyclerView.LayoutManager staggerLayoutManager;
    RecyclerView.LayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinned_recycler_view);
        pinnedHeaderListView = (PinnedHeaderListView) findViewById(R.id.pinnedHeaderListView);

        pinnedHeaderListView.setListView(this);
        final LinearLayout header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.mode_layout, null);
        btnLinear = (Button) header.findViewById(R.id.btn1);
        btnGrid = (Button) header.findViewById(R.id.btn2);
        btnStagger = (Button) header.findViewById(R.id.btn3);

        btnLinear.setOnClickListener(this);
        btnGrid.setOnClickListener(this);
        btnStagger.setOnClickListener(this);

        mHorizontalListView = (HorizontalListView) header.findViewById(R.id.category_list_on_pinned);
        mHorizontalListView.setAdapter(new HAdapter(this));
        mHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PinnedRecyclerViewActivity.this, "点击"+mHorizontalListView.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        pinnedHeaderListView.setPinnedHeader(header);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn1:
                recyclerView.setLayoutManager(linearLayoutManager);
                break;
            case R.id.btn2:
                recyclerView.setLayoutManager(gridLayoutManager);
                recyclerView.getAdapter().onAttachedToRecyclerView(recyclerView);
                break;
            case R.id.btn3:
                recyclerView.setLayoutManager(staggerLayoutManager);
                break;
        }
    }

    @Override
    public ViewGroup getListView() {
        recyclerView = (RecyclerView) LayoutInflater.from(this).inflate(R.layout.recyclerview, pinnedHeaderListView, false);

        linearLayoutManager = new LinearLayoutManager(this);
        staggerLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        Adapter adapter = new Adapter(this, recyclerView);
        WrapAdapter mWrapAdapter = new WrapAdapter(adapter);
        View header1 = createHeaderFooterView(recyclerView, Color.parseColor("#ff3ff1"), "this is header 1");
        View header2 = createHeaderFooterView(recyclerView, Color.parseColor("#f3ffa1"), "this is header 2");
        View header3 = createHeaderFooterView(recyclerView, Color.parseColor("#3fffa1"), "this is header 3");
        mWrapAdapter.addHeaderView(header1);
        mWrapAdapter.addHeaderView(header2);
        mWrapAdapter.addHeaderView(header3);

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
