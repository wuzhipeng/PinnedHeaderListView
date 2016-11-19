package com.freeman.pinnedheaderlistview;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Belmont on 2016/11/19.
 */

public interface IListView {
    void setPinnedHeader(View pinnedHeader);
    void removePinnedHeader();
    void setOnScrollListener(PinnedHeaderListView.OnScrollListener onScrollListener);
    boolean isShowingPinnedHeader();
    ViewGroup getListView();
}
