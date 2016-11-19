package com.freeman.pinnedheaderlistview;

import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by freeman on 2016/10/12.
 */

public class WrapAdapter extends RecyclerView.Adapter {
    private List<Pair<View, Integer>> mHeaders = new ArrayList<>();
    private List<Pair<View, Integer>> mFooters = new ArrayList<>();
    private int viewType = 1000;
    private RecyclerView.Adapter mAdapter;

    public WrapAdapter(RecyclerView.Adapter adapter) {
        mAdapter = adapter;
    }

    /**
     * 添加headerView
     *
     * @param headerView
     */
    public void addHeaderView(View headerView) {
        mHeaders.add(new Pair<>(headerView, viewType++));
    }

    public int getHeaderViewSize() {
        return mHeaders.size();
    }

    public int getFooterViewSize() {
        return mFooters.size();
    }

    public void removeHeaderView(View headerView) {
        for (Pair pair : mHeaders) {
            if(pair.first == headerView) {
                mHeaders.remove(pair);
                break;
            }
        }
    }

    /**
     * 添加footerView
     *
     * @param footerView
     */
    public void addFooterView(View footerView) {
        mFooters.add(new Pair<View, Integer>(footerView, viewType++));
    }

    @Override
    public int getItemViewType(int position) {
        int headerOffset = getHeaderViewSize();
        int footerOffset;
        if (position < headerOffset) {
            // 返回对应位置上的header的viewType
            return mHeaders.get(position).second;
        }
        if (position < (footerOffset = headerOffset + mAdapter.getItemCount())) {
            return mAdapter.getItemViewType(position);
        }
        if (position > mAdapter.getItemCount() + mFooters.size()) {
            return mFooters.get(position - footerOffset).second;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (Pair<View, Integer> pair : mHeaders) {
            if (pair.second == viewType) {
                return new SimpleHolder(pair.first);
            }
        }
        for (Pair<View, Integer> pair : mFooters) {
            if (pair.second == viewType) {
                return new SimpleHolder(pair.first);
            }
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 不是Header和Footer的holder才往下传递
        if (!(holder instanceof SimpleHolder)) {
            // 传给下一层的onBindViewHolder的position需要调整，减去HeaderView的个数。
            mAdapter.onBindViewHolder(holder, position - getHeaderViewSize());
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderViewSize() + mAdapter.getItemCount() + getFooterViewSize();
    }


    // 简单包装HeaderView和FooterView的ViewHolder
    private class SimpleHolder extends RecyclerView.ViewHolder {
        public SimpleHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            int headerOffset = getHeaderViewSize();
            int position = holder.getLayoutPosition();
            // 只有HeaderView和FooterView才需整行显示
            if (position < headerOffset || position > headerOffset + mAdapter.getItemCount() - 1)
                ((StaggeredGridLayoutManager.LayoutParams) lp).setFullSpan(true);
        }
        mAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int headerOffset = getHeaderViewSize();
                    boolean flag = false;
                    // position位置上是HeaderView
                    if (position < headerOffset) {
                        flag = true;
                    }
                    // position位置上是FooterView
                    if (position > (headerOffset + mAdapter.getItemCount() - 1)) {
                        flag = true;
                    }
                    return flag ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }
}
