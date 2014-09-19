package me.lehoo.android.pulltorefreshlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.lehoo.android.pulltorefreshlistview.R;

/**
 * Created by zhoulq on 14-9-18.
 */
public class HeaderView extends LinearLayout {
    private static final String TAG = "HeaderView";

    private static final int ROTATE_ANIM_DURATION = 180;
    private static final int LOADING_ROTATE_ANIM_DURATION = 1200;

    private LinearLayout mContainer;
    private TextView mPromptView;
    private ImageView mArrowView;
    // private ProgressBar mProgressBar;
    private ImageView mLoadingView;

    private RotateAnimation mUpAnimation, mDownAnimation, mLoadingAnimation;

    public static final int STATE_NORMAL = 0;
    public static final int STATE_READY = 1;
    public static final int STATE_REFRESHING = 2;
    private int mState;

    private boolean isFirst = false;

    public HeaderView(Context context) {
        super(context);
        initViews(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.header_view_layout, null);
        mContainer.setLayoutParams(lp);
        addView(mContainer);

        mPromptView = (TextView) findViewById(R.id.header_prompt_text);
        mArrowView = (ImageView) findViewById(R.id.header_arrow_image);
        //mProgressBar = (ProgressBar) findViewById(R.id.header_progressbar);
        mLoadingView = (ImageView) findViewById(R.id.header_loading_image);

        mUpAnimation = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mUpAnimation.setDuration(ROTATE_ANIM_DURATION);
        mUpAnimation.setInterpolator(new LinearInterpolator());
        mUpAnimation.setFillAfter(true);

        mDownAnimation = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mDownAnimation.setDuration(ROTATE_ANIM_DURATION);
        mDownAnimation.setInterpolator(new LinearInterpolator());
        mDownAnimation.setFillAfter(true);

        mLoadingAnimation = new RotateAnimation(0.0f, 720.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mLoadingAnimation.setDuration(LOADING_ROTATE_ANIM_DURATION);
        mLoadingAnimation.setFillAfter(true);
        mLoadingAnimation.setInterpolator(new LinearInterpolator());
        mLoadingAnimation.setRepeatCount(Animation.INFINITE);
        mLoadingAnimation.setRepeatMode(Animation.RESTART);
    }

    /**
     * set header view state.
     *
     * */
    public void setState(int state){
        if(state == mState && isFirst){
            isFirst = true;
            return;
        }

        if(state == STATE_REFRESHING){
            mArrowView.clearAnimation();
            mArrowView.setVisibility(INVISIBLE);
            // mProgressBar.setVisibility(VISIBLE);

            mLoadingView.setVisibility(VISIBLE);
            mLoadingView.startAnimation(mLoadingAnimation);
        }else {
            mArrowView.setVisibility(VISIBLE);
            // mProgressBar.setVisibility(INVISIBLE);
            // mLoadingAnimation.cancel();
            mLoadingView.clearAnimation();
            mLoadingView.setVisibility(INVISIBLE);
        }

        switch (state){
            case STATE_NORMAL:
                if(mState == STATE_REFRESHING){
                    mArrowView.clearAnimation();
                }

                if(mState == STATE_READY){
                    mArrowView.clearAnimation();
                    mArrowView.startAnimation(mDownAnimation);
                }
                mPromptView.setText(R.string.header_prompt_normal);
                break;
            case STATE_READY:
                if(mState != STATE_READY){
                    mArrowView.clearAnimation();
                    mArrowView.startAnimation(mUpAnimation);
                    mPromptView.setText(R.string.header_prompt_ready);
                }
                break;
            case STATE_REFRESHING:
                mPromptView.setText(R.string.header_prompt_loading);
                break;
        }

        mState = state;
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        return mContainer.getHeight();
    }

}
