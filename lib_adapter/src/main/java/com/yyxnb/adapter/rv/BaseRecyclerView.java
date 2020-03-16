package com.yyxnb.adapter.rv;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;


import com.yyxnb.adapter.BaseViewHolder;
import com.yyxnb.adapter.MultiItemTypeAdapter;
import com.yyxnb.adapter.R;

import java.util.ArrayList;
import java.util.List;

public class BaseRecyclerView extends RecyclerView {

    /**
     * 多类型适配器的ItemViewType需要设置为小于10000的值
     */
    private static final int TYPE_REFRESH_HEADER = 10000;     // RefreshHeader type
    private static final int TYPE_LOAD_MORE_VIEW = 10001;     // LoadingMore type
    private static final int TYPE_STATE_VIEW = 10002;         // StateView type
    private static final int TYPE_FOOTER_VIEW = 10003;        // FooterView type
    private static final int HEADER_INIT_INDEX = 10004;       // HeaderView starting type
    private List<Integer> mHeaderTypes = new ArrayList<>();   // HeaderView type list
    private ArrayList<View> mHeaderViews = new ArrayList<>(); // HeaderView view list
    private LinearLayout mFooterLayout;                       // FooterView layout
    private FrameLayout mStateLayout;                         // StateView  layout

    private boolean mRefreshEnabled = false;              // 是否 启动下拉刷新
    private boolean mLoadMoreEnabled = false;             // 是否 启动加载更多
    private boolean mHeaderViewEnabled = false;           // 是否 显示 HeaderView
    private boolean mFootViewEnabled = false;             // 是否 显示 FooterView
    private boolean mStateViewEnabled = true;             // 是否 显示 StateView
    private boolean misNoLoadMoreIfNotFullScreen = false; // 是否 不满一屏不加载更多

    private boolean mIsLoadingData = false;        // 是否正在加载更多
    private boolean mIsNoMore = false;             // 是否没有更多数据了
    private boolean mIsScrollUp = false;           // 手指是否上滑
    private float mLastY = -1f;                     // 手指按下的Y坐标值，用于处理下拉刷新View的高度
    private float mPullStartY = 0f;                 // 手指按下的Y坐标值，用于处理不满全屏时是否可进行上拉加载
    private float mPullMaxY = 0f;                       // 手指上滑最高点的值，值越小位置越高
    private float mDragRate = 2.5f;                // 下拉时候的偏移计量因子，越小拉动距离越短
    private long mLoadMoreDelayMillis = 0;         // 延迟多少毫秒后再调用加载更多接口
    private long mRefreshDelayMillis = 0;          // 延迟多少毫秒后再调用下拉刷新接口

    private int mStateLoadingResId = R.layout._loading_layout_loading;
    private int mStateEmptyResId = R.layout._loading_layout_empty;
    private int mStateErrorResId = R.layout._loading_layout_error;

    private OnRefreshListener mRefreshListener;    // 下拉刷新监听
    private BaseRefreshHeader mRefreshHeader;      // 自定义下拉刷新布局需要实现的接口
    private OnLoadMoreListener mLoadMoreListener;  // 加载更多监听
    private BaseLoadMore mLoadMore;                // 自定义加载更多布局需要实现的接口

    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    private WrapAdapter mWrapAdapter;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        init();
    }

    private void init() {
        mLoadMore = new SimpleLoadMoreView(getContext());
        mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
    }

    /**
     * 使用布局id添加头部布局。
     */
    public void addHeaderView(int layoutResId) {
        addHeaderView(getLayoutView(layoutResId));
    }

    /**
     * 在内容列表或空布局之前添加头部布局。
     */
    public void addHeaderView(View headerView) {
        mHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(headerView);
        mHeaderViewEnabled = true;
        if (mWrapAdapter != null) {
            mWrapAdapter.getOriginalAdapter().notifyItemInserted(getPullHeaderSize() + getHeaderViewCount() - 1);
        }
    }

    /**
     * 根据itemType确定它是哪个HeaderView
     */
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    /**
     * 确定类型是否为HeaderType
     */
    private boolean isHeaderType(int itemViewType) {
        return mHeaderViewEnabled && getHeaderViewCount() > 0 && mHeaderTypes.contains(itemViewType);
    }

    /**
     * 确定它是否为BaseRecyclerView保留的itemViewType
     */
    private boolean isReservedItemViewType(int itemViewType) {
        return itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_LOAD_MORE_VIEW || itemViewType == TYPE_STATE_VIEW || mHeaderTypes.contains(itemViewType);
    }

    /**
     * 自定义加载更多，需实现BaseLoadMore
     */
    public void setLoadingMoreView(BaseLoadMore loadingMore) {
        mLoadMore = loadingMore;
        mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
    }

    /**
     * 自定义下拉刷新，需实现BaseRefreshHeader
     */
    public void setRefreshHeaderView(BaseRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
    }

    /**
     * 加载完成。可以继续拉。
     */
    public void loadMoreComplete() {
        if (getLoadMoreSize() == 0) {
            return;
        }
        mIsNoMore = false;
        mIsLoadingData = false;
        mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
    }

    /**
     * 加载完成。无法加载上拉。
     */
    public void loadMoreEnd() {
        mIsLoadingData = false;
        mIsNoMore = true;
        mLoadMore.setState(BaseLoadMore.STATE_NO_MORE);
    }

    /**
     * 加载更多错误。继续向上拉或单击“加载”。
     */
    public void loadMoreFail() {
        if (getLoadMoreSize() == 0 || mLoadMore.getFailureView() == null || mLoadMoreListener == null) {
            return;
        }
        mIsLoadingData = false;
        mLoadMore.setState(BaseLoadMore.STATE_FAILURE);
        mLoadMore.getFailureView().setOnClickListener(v -> {
            mIsLoadingData = true;
            mLoadMore.setState(BaseLoadMore.STATE_LOADING);
            if (mLoadMoreDelayMillis <= 0) {
                mLoadMoreListener.onLoadMore();
            } else {
                postDelayed(() -> mLoadMoreListener.onLoadMore(), mLoadMoreDelayMillis);
            }
        });
    }

    /**
     * 设置刷新状态。false：重置以加载更多状态
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing) {
            if (getPullHeaderSize() == 0 || mRefreshHeader.getState() == BaseRefreshHeader.STATE_REFRESHING) {
                return;
            }
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPosition(0);
            }
            mRefreshHeader.setState(BaseRefreshHeader.STATE_REFRESHING);
            if (mRefreshListener != null) {
                postDelayed(() -> mRefreshListener.onRefresh(), 300 + mRefreshDelayMillis);
            }
        } else {
            if (getPullHeaderSize() > 0) {
                mRefreshHeader.refreshComplete();
            }
            loadMoreComplete();
        }
    }

    /**
     * 设置是否启用下拉刷新。
     */
    public void setRefreshEnabled(boolean enabled) {
        mRefreshEnabled = enabled;
        if (mRefreshHeader == null) {
            mRefreshHeader = new SimpleRefreshHeaderView(getContext());
        }
    }

    /**
     * 设置是否启用加载更多。
     */
    public void setLoadMoreEnabled(boolean enabled) {
        mLoadMoreEnabled = enabled;
        if (!enabled) {
            mLoadMore.setState(BaseLoadMore.STATE_COMPLETE);
        }
    }

    /**
     * 对于需要透明显示的页面部分，为“加载更多布局”提供底部高度。
     */
    public void setLoadingMoreBottomHeight(float heightDp) {
        mLoadMore.setLoadingMoreBottomHeight(heightDp);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof MultiItemTypeAdapter) {
            ((MultiItemTypeAdapter) adapter).setRecyclerView(this);
        }
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
        setRefreshing(false);
    }

    @Override
    public Adapter getAdapter() {
        if (mWrapAdapter != null) {
            return mWrapAdapter.getOriginalAdapter();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public void setDataItems(List<Object> list) {
        if (getAdapter() instanceof MultiItemTypeAdapter) {
            ((MultiItemTypeAdapter) getAdapter()).setDataItems(list);
        }
    }

    @SuppressWarnings("unchecked")
    public void addDataItem(List<Object> list) {
        if (getAdapter() instanceof MultiItemTypeAdapter) {
            ((MultiItemTypeAdapter) getAdapter()).addDataItem(list);
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (mWrapAdapter != null) {
            if (layout instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) layout);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeaderView(position)
                                || isFootView(position)
                                || isLoadMoreView(position)
                                || isStateView(position)
                                || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });

            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreListener != null && !mIsLoadingData && mLoadMoreEnabled) {
            LayoutManager layoutManager = getLayoutManager();
            if (layoutManager == null) {
                return;
            }
            int lastVisibleItemPosition = -1;
            if (layoutManager instanceof LinearLayoutManager) {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();

            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);

            }
            if (layoutManager.getChildCount() > 0
                    && !mIsNoMore
                    && lastVisibleItemPosition == mWrapAdapter.getItemCount() - 1 // 最后一个完全可视item是最后一个item
                    && isNoFullScreenLoad()
                    && isScrollLoad()
                    && (!mRefreshEnabled || mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING)) {
                mIsScrollUp = false;
                mIsLoadingData = true;
                mLoadMore.setState(BaseLoadMore.STATE_LOADING);
                if (mLoadMoreDelayMillis <= 0) {
                    mLoadMoreListener.onLoadMore();
                } else {
                    postDelayed(() -> mLoadMoreListener.onLoadMore(), mLoadMoreDelayMillis);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        if (mPullStartY == 0) {
            mPullStartY = ev.getY();
            mPullMaxY = mPullStartY;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果该项设置了click事件，则在此处不获取值，它将保持为0
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getY() < mPullMaxY) {
                    mPullMaxY = ev.getY();
                }
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (mRefreshEnabled && mRefreshListener != null && isOnTop() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeader.onMove(deltaY / mDragRate);
                    if (mRefreshHeader.getVisibleHeight() > 0 && mRefreshHeader.getState() < BaseRefreshHeader.STATE_REFRESHING) {
                        ev.setAction(MotionEvent.ACTION_DOWN);
                        super.onTouchEvent(ev);
                        return false;
                    }
                }
                break;
            default:
                /*
                 * 判断是否上拉了逻辑：
                 *  开启了加载更多
                 *  按下的纵坐标 - 最后的纵坐标>=-10(为了更灵敏,原点向下惯性滑动时==0)
                 *  最高点的纵坐标 - 松开时的纵坐标<=150(为了防止上滑后然后再下拉)
                 */
                mIsScrollUp = mLoadMoreEnabled && mPullStartY - ev.getY() >= -10 && ev.getY() - mPullMaxY <= 150;

                mPullStartY = 0;
                mLastY = -1;
                if (mRefreshEnabled
                        && isOnTop()
                        && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader.releaseAction()) {
                        if (mRefreshListener != null) {
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mRefreshListener.onRefresh();
                                }
                            }, 300 + mRefreshDelayMillis);
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private boolean isOnTop() {
        if (mRefreshHeader != null
                && mRefreshHeader instanceof View
                && ((View) mRefreshHeader).getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    private class WrapAdapter extends RecyclerView.Adapter<ViewHolder> {

        private RecyclerView.Adapter adapter;

        WrapAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
        }

        RecyclerView.Adapter getOriginalAdapter() {
            return this.adapter;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new BaseViewHolder((View) mRefreshHeader);
            } else if (isHeaderType(viewType)) {
                return new BaseViewHolder(getHeaderViewByType(viewType));
            } else if (viewType == TYPE_STATE_VIEW) {
                return new BaseViewHolder(mStateLayout);
            } else if (viewType == TYPE_FOOTER_VIEW) {
                return new BaseViewHolder(mFooterLayout);
            } else if (viewType == TYPE_LOAD_MORE_VIEW) {
                return new BaseViewHolder((View) mLoadMore);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (isRefreshHeader(position) || isHeaderView(position) || isStateView(position) || isFootView(position)) {
                return;
            }
            int adjPosition = position - getCustomTopItemViewCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> objectList) {
            if (isHeaderView(position) || isRefreshHeader(position) || isStateView(position) || isFootView(position)) {
                return;
            }
            if (adapter != null) {
                // Get the position with the type of the header removed
                int adjPosition = position - getCustomTopItemViewCount();
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    if (objectList.isEmpty()) {
                        adapter.onBindViewHolder(holder, adjPosition);
                    } else {
                        adapter.onBindViewHolder(holder, adjPosition, objectList);
                    }
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter != null) {
                return getPullHeaderSize() + getHeaderViewCount() + getFooterViewSize() + getLoadMoreSize() + getStateViewSize() + adapter.getItemCount();
            } else {
                return getPullHeaderSize() + getHeaderViewCount() + getFooterViewSize() + getLoadMoreSize() + getStateViewSize();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeaderView(position)) {
                position = position - getPullHeaderSize();
                return mHeaderTypes.get(position);
            }
            if (isFootView(position)) {
                return TYPE_FOOTER_VIEW;
            }
            if (isStateView(position)) {
                return TYPE_STATE_VIEW;
            }
            if (isLoadMoreView(position)) {
                return TYPE_LOAD_MORE_VIEW;
            }
            int adapterCount;
            if (adapter != null) {
                int adjPosition = position - getCustomTopItemViewCount();
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    int type = adapter.getItemViewType(adjPosition);
                    if (isReservedItemViewType(type)) {
                        throw new IllegalStateException("BaseRecyclerView require itemViewType in adapter should be less than 10000 !");
                    }
                    return type;
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getCustomTopItemViewCount()) {
                int adjPosition = position - getCustomTopItemViewCount();
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        // 占一行
                        return (isHeaderView(position)
                                || isFootView(position)
                                || isLoadMoreView(position)
                                || isStateView(position)
                                || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeaderView(holder.getLayoutPosition())
                    || isFootView(holder.getLayoutPosition())
                    || isRefreshHeader(holder.getLayoutPosition())
                    || isLoadMoreView(holder.getLayoutPosition())
                    || isStateView(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(@NonNull RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(@NonNull AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

    }

    /**
     * 是否为StateView布局
     */
    public boolean isStateView(int position) {
        return mStateViewEnabled && mStateLayout != null && position == getHeaderViewCount() + getPullHeaderSize();
    }

    /**
     * 是否为HeaderView布局
     */
    public boolean isHeaderView(int position) {
        return mHeaderViewEnabled && position >= getPullHeaderSize() && position < getHeaderViewCount() + getPullHeaderSize();
    }

    /**
     * 是否为FootView布局
     */
    public boolean isFootView(int position) {
        if (mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0) {
            return position == mWrapAdapter.getItemCount() - 1 - getLoadMoreSize();
        } else {
            return false;
        }
    }

    /**
     * 是否为RefreshHeaderView布局
     */
    public boolean isRefreshHeader(int position) {
        if (mRefreshEnabled) {
            return position == 0;
        } else {
            return false;
        }
    }

    /**
     * 是否为LoadMoreView布局
     */
    public boolean isLoadMoreView(int position) {
        if (mLoadMoreEnabled) {
            return position == mWrapAdapter.getItemCount() - 1;
        } else {
            return false;
        }
    }

    /**
     * FooterView数量
     */
    public int getFooterViewSize() {
        return mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0 ? 1 : 0;
    }

    /**
     * StateView数量
     */
    public int getStateViewSize() {
        return mStateViewEnabled && mStateLayout != null && mStateLayout.getChildCount() != 0 ? 1 : 0;
    }

    /**
     * HeaderView数量
     */
    public int getHeaderViewCount() {
        return mHeaderViewEnabled ? mHeaderViews.size() : 0;
    }

    /**
     * 自定义类型头部布局的个数：用于取到正确的position
     */
    public int getCustomTopItemViewCount() {
        return getHeaderViewCount() + getPullHeaderSize() + getStateViewSize();
    }

    public interface OnLoadMoreListener {

        void onLoadMore();
    }

    public interface OnRefreshListener {

        void onRefresh();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener, long delayMillis) {
        setLoadMoreEnabled(true);
        mLoadMoreListener = listener;
        mLoadMoreDelayMillis = delayMillis;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        setLoadMoreEnabled(true);
        mLoadMoreListener = listener;
    }

    public void setOnRefreshListener(OnRefreshListener listener, long delayMillis) {
        setRefreshEnabled(true);
        mRefreshListener = listener;
        mRefreshDelayMillis = delayMillis;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        setRefreshEnabled(true);
        mRefreshListener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 解决折叠工具栏布局冲突
        AppBarLayout appBarLayout = null;
        ViewParent p = getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p != null) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }

    /**
     * 区分刷新布局是否需要计数
     * 如果使用控件附带的下拉刷新，则需要计算位置
     */
    public int getPullHeaderSize() {
        if (mRefreshEnabled) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 如果使用上拉刷新，则需要计算位置
     */
    public int getLoadMoreSize() {
        if (mLoadMoreEnabled) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setLoadingResId(int layoutResId) {
        mStateLoadingResId = layoutResId;
    }

    public void setEmptyResId(int layoutResId) {
        mStateLoadingResId = layoutResId;
    }

    public void setErrorResId(int layoutResId) {
        mStateLoadingResId = layoutResId;
    }

    public void setStateType(BaseState state) {
        switch (state) {
            case LOADING:
                setStateView(mStateLoadingResId);
                break;
            case SUCCESS:
                setStateViewEnabled(false);
                break;
            case EMPTY:
                setStateView(mStateEmptyResId);
                break;
            case ERROR:
                setStateView(mStateErrorResId);
                break;
        }
    }

    public void setStateView(int layoutResId) {
        setStateView(getLayoutView(layoutResId));
    }

    /**
     * 设置是否显示StateView
     */
    public void setStateViewEnabled(boolean stateViewEnabled) {
        this.mStateViewEnabled = stateViewEnabled;
    }

    /**
     * 设置状态布局，它可以包括：EmptyView、LoadingView、ErrorView等
     * 刷新视图->HeaderViews->StateView->列表内容视图->FooterViews->加载更多视图
     */
    public void setStateView(View stateView) {
        boolean insert = false;
        if (mStateLayout == null) {
            mStateLayout = new FrameLayout(stateView.getContext());
            final LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            final ViewGroup.LayoutParams lp = stateView.getLayoutParams();
            if (lp != null) {
                layoutParams.width = lp.width;
                layoutParams.height = lp.height;
            }
            mStateLayout.setLayoutParams(layoutParams);
            insert = true;
        }
        mStateLayout.removeAllViews();
        mStateLayout.addView(stateView);
        mStateViewEnabled = true;
        if (insert) {
            if (getStateViewSize() == 1) {
                int position = getHeaderViewCount() + getPullHeaderSize();
                if (mWrapAdapter != null) {
                    mWrapAdapter.getOriginalAdapter().notifyItemInserted(position);
                }
            }
        }
    }

    private View getLayoutView(int layoutResId) {
        return LayoutInflater.from(getContext()).inflate(layoutResId, (ViewGroup) this.getParent(), false);
    }

    public int addFooterView(int layoutResId) {
        return addFooterView(getLayoutView(layoutResId), -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer) {
        return addFooterView(footer, -1, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer, int index) {
        return addFooterView(footer, index, LinearLayout.VERTICAL);
    }

    public int addFooterView(View footer, int index, int orientation) {
        if (mFooterLayout == null) {
            mFooterLayout = new LinearLayout(footer.getContext());
            if (orientation == LinearLayout.VERTICAL) {
                mFooterLayout.setOrientation(LinearLayout.VERTICAL);
                mFooterLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                mFooterLayout.setOrientation(LinearLayout.HORIZONTAL);
                mFooterLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        }
        final int childCount = mFooterLayout.getChildCount();
        if (index < 0 || index > childCount) {
            index = childCount;
        }
        mFooterLayout.addView(footer, index);
        mFootViewEnabled = true;
        if (mFooterLayout.getChildCount() == 1) {
            if (mWrapAdapter != null) {
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomTopItemViewCount();
                if (position != -1) {
                    mWrapAdapter.getOriginalAdapter().notifyItemInserted(position);
                }
            }
        }
        return index;
    }

    /**
     * 是否显示头部布局
     */
    public void setHeaderViewEnabled(boolean headerViewEnabled) {
        this.mHeaderViewEnabled = headerViewEnabled;
    }

    /**
     * 是否显示底部布局
     */
    public void setFootViewEnabled(boolean footViewEnabled) {
        this.mFootViewEnabled = footViewEnabled;
    }

    /**
     * 小于一个屏幕，根据距离向上确定是否加载
     */
    private boolean isScrollLoad() {
        return isFullScreen() || mIsScrollUp;
    }

    /**
     * 设置少于一个屏幕不加载
     */
    public void setNotFullScreenNoLoadMore() {
        misNoLoadMoreIfNotFullScreen = true;
    }

    /**
     * 如果屏幕未加载。默认加载
     */
    private boolean isNoFullScreenLoad() {
        if (misNoLoadMoreIfNotFullScreen) {
            return isFullScreen();
        } else {
            return true;
        }
    }

    /**
     * 是否全屏
     */
    private boolean isFullScreen() {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null) {
            return false;
        }
        if (layoutManager instanceof LinearLayoutManager) {
            final LinearLayoutManager llm = (LinearLayoutManager) layoutManager;

            return (llm.findLastCompletelyVisibleItemPosition() + 1) != mWrapAdapter.getItemCount() ||
                    llm.findFirstCompletelyVisibleItemPosition() != 0;

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) layoutManager;

            int[] last = new int[sglm.getSpanCount()];
            sglm.findLastCompletelyVisibleItemPositions(last);

            int[] first = new int[sglm.getSpanCount()];
            sglm.findFirstCompletelyVisibleItemPositions(first);

            return findMax(last) + 1 != mWrapAdapter.getItemCount() || first[0] != 0;
        }
        return false;
    }

    /**
     * remove HeaderView
     * tip:
     * 删除HeaderView后不能再添加它，
     * 因为类型可能重复，导致添加失败
     */
    public void removeHeaderView(@NonNull View header) {
        if (getHeaderViewCount() == 0) {
            return;
        }
        if (mWrapAdapter != null) {
            int index = -1;
            for (int i = 0; i < mHeaderViews.size(); i++) {
                if (mHeaderViews.get(i) == header) {
                    mHeaderViews.remove(mHeaderViews.get(i));
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                mHeaderTypes.remove(index);
                mWrapAdapter.getOriginalAdapter().notifyItemRemoved(getPullHeaderSize() + index);
            }
        }
    }

    /**
     * remove all HeaderView
     */
    public void removeAllHeaderView() {
        if (getHeaderViewCount() == 0) {
            return;
        }
        mHeaderViewEnabled = false;
        if (mWrapAdapter != null) {
            mHeaderViews.clear();
            mHeaderTypes.clear();
            mWrapAdapter.getOriginalAdapter().notifyItemRangeRemoved(getPullHeaderSize(), getHeaderViewCount());
        }
    }

    /**
     * remove FooterView
     * 因为你只处理一个项目的布局，
     * 除非删除所有页脚视图，否则不必刷新布局
     *
     * @param footer
     */
    public void removeFooterView(View footer) {
        if (mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0) {
            mFooterLayout.removeView(footer);
            if (mWrapAdapter != null && mFooterLayout.getChildCount() == 0) {
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomTopItemViewCount();
                if (position != -1) {
                    mWrapAdapter.getOriginalAdapter().notifyItemRemoved(position);
                }
            }
        }
    }

    /**
     * remove all footer view from mFooterLayout and set null to mFooterLayout
     */
    public void removeAllFooterView() {
        if (mFootViewEnabled && mFooterLayout != null && mFooterLayout.getChildCount() != 0) {
            mFooterLayout.removeAllViews();
            if (mWrapAdapter != null) {
                int position = mWrapAdapter.getOriginalAdapter().getItemCount() + getCustomTopItemViewCount();
                if (position != -1) {
                    mWrapAdapter.getOriginalAdapter().notifyItemRemoved(position);
                }
            }
        }
    }

    /**
     * 设置下拉时候的偏移计量因子。y = deltaY/mDragRate，默认值 3
     * 越大，意味着，用户要下拉滑动更久来触发下拉刷新。相反越小，就越短距离
     */
    public void setDragRate(float rate) {
        if (rate <= 0.5) {
            return;
        }
        mDragRate = rate;
    }

    /**
     * 是否正在加载更多
     */
    public boolean isLoadingMore() {
        return mIsLoadingData;
    }

    /**
     * 是否正在刷新
     */
    public boolean isRefreshing() {
        return mRefreshHeader != null && mRefreshHeader.getState() == BaseRefreshHeader.STATE_REFRESHING;
    }

    public boolean isRefreshEnabled() {
        return mRefreshEnabled;
    }

    /**
     * 释放
     */
    public void destroy() {
        mHeaderViewEnabled = false;
        mFootViewEnabled = false;
        mStateViewEnabled = false;
        mRefreshEnabled = false;
        mLoadMoreEnabled = false;
        if (mHeaderViews != null) {
            mHeaderViews.clear();
        }
        if (mHeaderTypes != null) {
            mHeaderTypes.clear();
        }
        if (mFooterLayout != null) {
            mFooterLayout.removeAllViews();
        }
        if (mStateLayout != null) {
            mStateLayout.removeAllViews();
        }
    }
}
