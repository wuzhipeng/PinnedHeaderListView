package com.freeman.pinnedheaderlistview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 *
 * Created by freeman on 2016/11/1.
 */
public class PinnedHeaderListView extends FrameLayout implements IListView{
    private IGetListView mIGetListView = null;
    private FrameLayout mWrapPinnedHeader = null;
    private Context mContext;
    private int mPinnedPosition = -1;
    private IListView mListView = null;

    public PinnedHeaderListView(Context context) {
        super(context);
        mContext = context;
        initPinnedHeaderContainer();
    }

    public PinnedHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPinnedHeaderContainer();
    }

    private void initPinnedHeaderContainer() {
        FrameLayout layout = new FrameLayout(mContext);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(lp);
        addView(layout);
    }

    public void setListView(IGetListView iGetListView) {
        if (mIGetListView != null) {
            throw new RuntimeException("IGetListView is already set");
        }
        if (iGetListView == null) {
            throw new IllegalArgumentException("IGetListView should not bel null");
        }
        mIGetListView = iGetListView;
        ViewGroup viewGroup = mIGetListView.getListView();
        if (viewGroup instanceof ListView) {
            mListView = new ListViewImpl((ListView) viewGroup);
        } else if (viewGroup instanceof RecyclerView) {
            mListView = new RecyclerViewImpl((RecyclerView) viewGroup);
        } else {
            throw new RuntimeException("getListView() only supports ListView or RecyclerView");
        }
        addView(viewGroup, 0);
    }

    @Override
    public void removePinnedHeader() {
        if(mListView == null) return;
        mListView.removePinnedHeader();
    }

    @Override
    public boolean isShowingPinnedHeader() {
        if(mListView == null) return false;
        return mListView.isShowingPinnedHeader();
    }

    @Override
    public ViewGroup getListView() {
        if(mListView == null) return null;
        return mListView.getListView();
    }

    @Override
    public void setPinnedHeader(View v) {
        if (mListView == null) {
            throw new NullPointerException("mListView is null, must call setListView(IGetListView iGetListView) first");
        }
        if (mWrapPinnedHeader == null) {
            mWrapPinnedHeader = new FrameLayout(mContext);
        }
        mWrapPinnedHeader.addView(v);
        if(mListView.getListView() instanceof ListView) {
            mPinnedPosition = ((ListView) mListView.getListView()).getHeaderViewsCount();
        } else if(mListView.getListView() instanceof  RecyclerView) {
            // TODO
        }
        mListView.setPinnedHeader(mWrapPinnedHeader);
    }


    public void setOnScrollListener(OnScrollListener onScrollListener) {
        if (mListView == null) {
            throw new NullPointerException("mListView is null, must call setListView(IGetListView iGetListView) first");
        }
        mListView.setOnScrollListener(onScrollListener);
    }

    private void attachPinnedViewToListView(ViewGroup layout, View pinnedView) {
        ViewGroup.LayoutParams lp = pinnedView.getLayoutParams();
        if (lp != null) {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        pinnedView.setLayoutParams(lp);
        layout.addView(pinnedView);
    }

    private void detachPinnedViewFromListView(ViewGroup layout, View pinnedView) {
        int height = pinnedView.getHeight();
        ViewGroup.LayoutParams lp1 = layout.getLayoutParams();
        if (lp1 != null) {
            lp1.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp1.height = height;
            layout.setLayoutParams(lp1);
        }
        ViewGroup.LayoutParams lp = pinnedView.getLayoutParams();
        if (lp != null) {
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        pinnedView.setLayoutParams(lp);
        // 取得存放PinnedView的容器layout
        FrameLayout container = (FrameLayout)getChildAt(1);
        container.addView(pinnedView);
    }

    private class ListViewImpl implements IListView {
        private ListView mListView;
        private OnListViewScrollListener mOnScrollListener;
        private ViewGroup mPinnedHeader;
        private boolean attachToListView = true;

        private ListViewImpl(ListView listView) {
            mListView = listView;

            mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if(mOnScrollListener != null) {
                        mOnScrollListener.onScrollStateChanged(view, scrollState);
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    ViewGroup layout = mPinnedHeader;
                    View pinnedView;
                    if (layout == null) return;

                    if (attachToListView && (layout.getTop() < 0 ||
                            (layout.getParent() == null && mPinnedPosition < mListView.getLastVisiblePosition()))) {
                        pinnedView = layout.getChildAt(0);
                        layout.removeAllViews();
                        if (pinnedView != null) {
                            attachToListView = false;
                            detachPinnedViewFromListView(layout, pinnedView);
                        }
                    } else if (!attachToListView && (layout.getTop() >= 0 && layout.getParent() != null)) {
                        // 取得存放PinnedView的容器layout
                        FrameLayout container = (FrameLayout)getChildAt(1);
                        pinnedView = container.getChildAt(0);
                        container.removeView(pinnedView);
                        if (pinnedView != null && !attachToListView) {
                            attachToListView = true;
                            attachPinnedViewToListView(layout, pinnedView);
                        }
                    }
                    if(mOnScrollListener != null) {
                        mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                    }
                }
            });
        }

        @Override
        public void removePinnedHeader() {
            if(mPinnedHeader != null) {
                mListView.removeHeaderView(mPinnedHeader);
                mPinnedHeader = null;
            }
        }

        @Override
        public boolean isShowingPinnedHeader() {
            return !attachToListView;
        }

        @Override
        public ViewGroup getListView() {
            return mListView;
        }

        @Override
        public void setPinnedHeader(View pinnedHeader) {
            mPinnedHeader = (ViewGroup) pinnedHeader;
            mListView.addHeaderView(mPinnedHeader);
            mPinnedPosition = mListView.getHeaderViewsCount();
        }


        @Override
        public void setOnScrollListener(OnScrollListener onScrollListener) {
            mOnScrollListener = (OnListViewScrollListener) onScrollListener;
        }
    }

    private class RecyclerViewImpl implements IListView {
        private RecyclerView mRecyclerView = null;
        private ViewGroup mPinnedHeader;
        private OnRecyclerViewScrollListener mOnScrollListener;
        private boolean attachToListView = true;

        private RecyclerViewImpl(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    ViewGroup layout = mPinnedHeader;
                    View pinnedView;
                    if (layout == null) return;

                    if (attachToListView && (layout.getTop() < 0 ||
                            (layout.getParent() == null))) {
                        pinnedView = layout.getChildAt(0);
                        layout.removeAllViews();
                        if (pinnedView != null) {
                            attachToListView = false;
                            detachPinnedViewFromListView(layout, pinnedView);
                        }
                    } else if (!attachToListView && (layout.getTop() >= 0 && layout.getParent() != null)) {
                        // 取得存放PinnedView的容器layout
                        FrameLayout container = (FrameLayout)getChildAt(1);
                        pinnedView = container.getChildAt(0);
                        container.removeView(pinnedView);
                        if (pinnedView != null && !attachToListView) {
                            attachToListView = true;
                            attachPinnedViewToListView(layout, pinnedView);
                        }
                    }

                    if(mOnScrollListener != null) {
                        mOnScrollListener.onScrolled(recyclerView, dx, dy);
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (mOnScrollListener != null) {
                        mOnScrollListener.onScrollStateChanged(recyclerView, newState);
                    }
                }
            });
        }

        @Override
        public void removePinnedHeader() {
            if(mRecyclerView.getAdapter() instanceof WrapAdapter) {
                WrapAdapter adapter = (WrapAdapter)mRecyclerView.getAdapter();
                adapter.removeHeaderView(mPinnedHeader);
            }
            mPinnedHeader = null;
        }

        @Override
        public boolean isShowingPinnedHeader() {
            return !attachToListView;
        }

        @Override
        public ViewGroup getListView() {
            return mRecyclerView;
        }

        @Override
        public void setPinnedHeader(View pinnedHeader) {
            mPinnedHeader = (ViewGroup) pinnedHeader;
            if (mRecyclerView.getAdapter() instanceof WrapAdapter) {
                ((WrapAdapter) mRecyclerView.getAdapter()).addHeaderView(mPinnedHeader);
                mPinnedPosition = ((WrapAdapter) mRecyclerView.getAdapter()).getHeaderViewSize();
            }
        }

        @Override
        public void setOnScrollListener(OnScrollListener onScrollListener) {
            mOnScrollListener = (OnRecyclerViewScrollListener) onScrollListener;
        }
    }

    private static class OnScrollListenerImpl implements AbsListView.OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }

    public static class OnListViewScrollListener extends OnScrollListenerImpl implements OnScrollListener {
    }

    public static class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener implements OnScrollListener {
    }

    public interface OnScrollListener {}

    public interface IGetListView {
        /**
         * 支持获取ListView和RecyclerView
         *
         * @return
         */
        ViewGroup getListView();
    }

}
