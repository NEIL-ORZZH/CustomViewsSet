package me.lehoo.android.pulltorefreshlistview.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import me.lehoo.android.pulltorefreshlistview.R;

/**
 * Created by zhoulq on 14-9-18.
 */
public class PullToRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private Scroller mScroller;
    private HeaderView mHeader;
    private FooterView mFooterView;
    private RelativeLayout mHeaderContent;
    private TextView mHeaderTime;
    private LinearLayout mFooterLayout;

    private int mHeaderHeight;


    private boolean isFooterViewReady = false;

    private boolean mEnablePullRefresh = false;
    private boolean mEnablePullLoad = false;
    private boolean mEnableAutoLoad = false;
    private boolean mPullRefreshing = false;
    private boolean mPullLoading = false;

    private IListViewListener mListener;
    private OnScrollListener mScrollListener;

    private int mTotalItemCount;

    private int mScrollBack;

    private final static int SCROLL_BACK_HEADER = 0;
    private final static int SCROLL_BACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 300;

    private final static int PULL_LOAD_MORE_DELTA = 50;

    private final static float OFFSET_RADIO = 1.8f;

    private float mLastY = -1;

    public PullToRefreshListView(Context context) {
        super(context);
        initWithContext(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWithContext(context);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);

        // init header view
        mHeader = new HeaderView(context);
        mHeaderContent = (RelativeLayout) mHeader.findViewById(R.id.header_content_layout);
        mHeaderTime = (TextView) mHeader.findViewById(R.id.header_time_text);
        addHeaderView(mHeader);

        // init footer view
        mFooterView = new FooterView(context);
        mFooterLayout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mFooterLayout.addView(mFooterView, params);

        // init header height
        ViewTreeObserver observer = mHeader.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    mHeaderHeight = mHeaderContent.getHeight();
                    ViewTreeObserver observer = getViewTreeObserver();

                    if (null != observer) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            observer.removeGlobalOnLayoutListener(this);
                        } else {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }
    }


    @Override
    public void setAdapter(ListAdapter adapter) {
        if(!isFooterViewReady){
            addFooterView(mFooterLayout);
            isFooterViewReady = true;
        }
        super.setAdapter(adapter);
    }

    public void setPullRefreshEnable(boolean enable){
        mEnablePullRefresh = enable;
        mHeaderContent.setVisibility(enable ? VISIBLE : INVISIBLE);
    }

    public void setPullLoadEnable(boolean enable){
        mEnablePullLoad = enable;

        if (!mEnablePullLoad) {
            mFooterView.setBottomMargin(0);
            mFooterView.hideFooterView();
            mFooterView.setPadding(0, 0, 0, mFooterView.getHeight() * (-1));
            mFooterView.setOnClickListener(null);

        } else {
            mPullLoading = false;
            mFooterView.setPadding(0, 0, 0, 0);
            mFooterView.showFooterView();
            mFooterView.setState(FooterView.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    public void setAutoLoadEnable(boolean enable){
        mEnableAutoLoad = enable;
    }

    public void stopPullRefreshing(){
        if(mPullRefreshing){
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    public void stopPullLoading(){
        if(mPullLoading){
            mPullLoading = false;
            mFooterView.setState(FooterView.STATE_NORMAL);
        }
    }

    public void setRefreshTime(String time){
        mHeaderTime.setText(time);
    }

    public void setListViewListener(IListViewListener listener){
        mListener = listener;
    }

    public void autoRefresh(){
        mHeader.setVisibleHeight(mHeaderHeight);

        if(mEnablePullRefresh && !mPullRefreshing){
            if(mHeader.getVisibleHeight() > mHeaderHeight){
                mHeader.setState(HeaderView.STATE_READY);
            } else {
                mHeader.setState(HeaderView.STATE_NORMAL);
            }
        }

        mPullRefreshing = true;
        mHeader.setState(HeaderView.STATE_REFRESHING);
        refresh();
    }

    private void invokeOnScrolling(){
        if(mScrollListener instanceof IOnScrollListener){
            IOnScrollListener listener = (IOnScrollListener) mScrollListener;
            listener.onIScroll(this);
        }
    }

    private void resetHeaderHeight(){
        int height = mHeader.getVisibleHeight();
        if (height == 0) return;

        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderHeight) return;

        // default: scroll back to dismiss header.
        int finalHeight = 0;
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderHeight) {
            finalHeight = mHeaderHeight;
        }

        mScrollBack = SCROLL_BACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);

        // trigger computeScroll
        invalidate();
    }

    private void updateHeaderHeight(float delta){
        mHeader.setVisibleHeight((int) delta + mHeader.getVisibleHeight());

        if (mEnablePullRefresh && !mPullRefreshing) {
            if (mHeader.getVisibleHeight() > mHeaderHeight) {
                mHeader.setState(HeaderView.STATE_READY);
            } else {
                mHeader.setState(HeaderView.STATE_NORMAL);
            }
        }

        // scroll to top each time
        setSelection(0);
    }

    private void resetFooterHeight(){
        int bottomMargin = mFooterView.getBottomMargin();

        if (bottomMargin > 0) {
            mScrollBack = SCROLL_BACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void updateFooterHeight(float delta){
        int height = mFooterView.getBottomMargin() + (int) delta;

        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) {
                // height enough to invoke load more.
                mFooterView.setState(FooterView.STATE_READY);
            } else {
                mFooterView.setState(FooterView.STATE_NORMAL);
            }
        }

        mFooterView.setBottomMargin(height);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mLastY == -1){
            mLastY = ev.getRawY();
        }

//        if(mPullRefreshing || getAnimation() != null){    //当正在刷新的时候关闭下拉
//            return true;
//        }

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();

                if(getFirstVisiblePosition() == 0 && (mHeader.getVisibleHeight() > 0 || deltaY > 0)){
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
                    invokeOnScrolling();
                }else if(getLastVisiblePosition() == mTotalItemCount - 1 && (mFooterView.getBottomMargin() > 0 || deltaY < 0)){
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastY = -1;
                if(getFirstVisiblePosition() == 0){
                    if(mEnablePullRefresh && mHeader.getVisibleHeight() > mHeaderHeight && !mPullRefreshing){ /* !mPullRefreshing : while refreshing, do not trigger refresh again */
                        mPullRefreshing = true;
                        mHeader.setState(HeaderView.STATE_REFRESHING);
                        refresh();
                    }
                    resetHeaderHeight();
                }

                if(getLastVisiblePosition() == getCount() - 1){
                    if(mEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA){
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            if(mScrollBack == SCROLL_BACK_HEADER){
                mHeader.setVisibleHeight(mScroller.getCurrY());
            }else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }
            postInvalidate();
            invokeOnScrolling();
        }

        super.computeScroll();
    }

    private void startLoadMore(){
        mPullLoading = true;
        mFooterView.setState(FooterView.STATE_LOADING);
        loadMore();
    }

    private void loadMore(){
        if(mEnablePullLoad && mListener != null){
            mListener.onLoadMore();
        }
    }

    private void refresh(){
        if(mEnablePullRefresh && mListener != null){
            mListener.onRefresh();
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mScrollListener != null){
            mScrollListener.onScrollStateChanged(view, scrollState);
        }

        if(scrollState == IOnScrollListener.SCROLL_STATE_IDLE){
            if(mEnableAutoLoad && getLastVisiblePosition() == getCount() - 1){
                startLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
        if(mScrollListener != null){
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    public interface IOnScrollListener extends OnScrollListener{
        public void onIScroll(View view);
    }

    /**
     * list view listener
     * */
    public interface IListViewListener{
        public void onRefresh();

        public void onLoadMore();
    }
}

