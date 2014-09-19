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
public class FooterView extends LinearLayout {
    private LinearLayout mContainer;
    // private ProgressBar mProgressbar;
    private TextView mFooterPromptText;
    private ImageView mLoadingView;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;
    private int mState;

    private RotateAnimation mLoadingAnimation;
    private static final int LOADING_ROTATE_ANIM_DURATION = 1200;

    public FooterView(Context context) {
        super(context);
        initViews(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    private void initViews(Context context){
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.footer_view_layout, null);
        mContainer.setLayoutParams(lp);
        addView(mContainer);

        // mProgressbar = (ProgressBar) findViewById(R.id.footer_progressbar);
        mFooterPromptText = (TextView) findViewById(R.id.footer_prompt_text);
        mLoadingView = (ImageView) findViewById(R.id.footer_loading_image);

        mLoadingAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mLoadingAnimation.setDuration(LOADING_ROTATE_ANIM_DURATION);
        mLoadingAnimation.setFillAfter(true);
        mLoadingAnimation.setInterpolator(new LinearInterpolator());
        mLoadingAnimation.setRepeatCount(Animation.INFINITE);
        mLoadingAnimation.setRepeatMode(Animation.RESTART);
    }

    /**
     * set footer view state
     *
     * @param state : int
     * */
    public void setState(int state){
        if(state == mState){
            return;
        }

        if(state == STATE_LOADING){
            // mProgressbar.setVisibility(VISIBLE);
            mLoadingView.setVisibility(VISIBLE);
            mLoadingView.setAnimation(mLoadingAnimation);
            mFooterPromptText.setText(R.string.footer_prompt_loading);
        } else {
            // mProgressbar.setVisibility(INVISIBLE);
            mLoadingView.clearAnimation();
            mLoadingView.setVisibility(INVISIBLE);
            mFooterPromptText.setText(R.string.footer_prompt_ready);
        }

        switch (state){
            case STATE_NORMAL:
                mFooterPromptText.setText(R.string.footer_prompt_loading);
                break;
            case STATE_READY:
                if(mState != STATE_READY){
                    mFooterPromptText.setText(R.string.footer_prompt_ready);
                }
                break;
            case STATE_LOADING:
                break;
        }

        mState = state;
    }

    public void setBottomMargin(int height){
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.bottomMargin = height;
        mContainer.setLayoutParams(lp);
    }

    public int getBottomMargin(){
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.bottomMargin;
    }

    public void hideFooterView(){
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = 0;
        mContainer.setLayoutParams(lp);
    }

    public void showFooterView(){
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mContainer.setLayoutParams(lp);
    }

}
