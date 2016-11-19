package com.freeman.pinnedheaderlistview;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freeman on 2016/10/10.
 */

public class Adapter extends RecyclerView.Adapter {

    private List<String> mData = new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;
    private RecyclerView mRecyclerView;
    int[] heights = {50, 67, 83, 99, 115};
    int density = 0;

    public Adapter(Context context, RecyclerView recyclerView) {
        for (int i = 0; i < 100; i++) {
            mData.add(i + ", " + i);
        }
        mContext = context;
        mRecyclerView = recyclerView;
        inflater = LayoutInflater.from(mContext);
        density = (int) mContext.getResources().getDisplayMetrics().density;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 创建ViewHolder缓存
        View itemView = inflater.inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView tv = ((ViewHolder) holder).textView;
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        // 如果是瀑布流模式，强制设置item的高度，从而形成瀑布流
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            ViewGroup.LayoutParams lp = tv.getLayoutParams();
            lp.height = heights[position % 5] * density;
            tv.setLayoutParams(lp);
        }
        // 设置View显示的数据
        tv.setText(mData.get(position));
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        System.out.println("打印" + "onAttachedToRecyclerView");

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        System.out.println("打印" + "onViewAttachedToWindow");
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
