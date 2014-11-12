package me.lehoo.android.popupbubble.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import me.lehoo.android.popupbubble.R;

/**
 * Created by zhoulq on 14-9-19.
 */
public class PopupBubble extends PopupWindow {
    private PopupWindow mPopupWindow;
    private View mAnchor;
    private Context mContext;
    private WindowManager mWindowManager;
    private LayoutInflater mInflater;
    private View mContainer;
    private int posX, posY;
    private int mDrawableWidth = 30;

    public PopupBubble(View anchor, String dataString){
        this.mAnchor = anchor;
        this.mContext = mAnchor.getContext();
        this.mPopupWindow = new PopupWindow(mContext);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    PopupBubble.this.mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContainer = mInflater.inflate(R.layout.pop_bubble_layout, null);
        updateView(dataString);
        setContentView(mContainer);
    }

    public void show(){
        prepareShow();
        drawView();
        mPopupWindow.showAtLocation(this.mAnchor, Gravity.NO_GRAVITY, posX, posY);
        mPopupWindow.setAnimationStyle(R.style.popAnimation);
        mPopupWindow.update();
    }

    public void dismiss(){
        this.mPopupWindow.dismiss();
    }

    private void updateView(final String dataString){
       Button mCopyButton = (Button) mContainer.findViewById(R.id.button_copy);
        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, dataString + " : " + mContext.getResources().getString(R.string.button_copy_info), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        Button mNoteButton = (Button) mContainer.findViewById(R.id.button_note);
        mNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.button_note_info), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });


        Button mCorrectButton = (Button) mContainer.findViewById(R.id.button_correct);
        mCorrectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.button_correct_info), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        Button mShareButton = (Button) mContainer.findViewById(R.id.button_share);
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.button_share_info), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    private void drawView(){
        int[] locationXY = new int[2];
        mAnchor.getLocationOnScreen(locationXY);
        int localX = locationXY[0];
        int localY = locationXY[1];

        mContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mContainer.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        int mContainerHeight = mContainer.getMeasuredHeight();
        int mContainerWidth = mContainer.getMeasuredWidth();


        int mAnchorWidth = mAnchor.getMeasuredWidth();
        int mAnchorHeight = mAnchor.getMeasuredHeight();

        int mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();
        int mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        ImageView pointView;
        RelativeLayout.LayoutParams pointLayoutParams;

        int mThreeFourWidth = Math.round(mScreenWidth / 4*3);
        int mOneFourWidth = Math.round(mScreenWidth / 4);
        int mHalfWidth = Math.round(mScreenWidth / 2);
        int mOneFourHeight = Math.round(mScreenHeight / 4);
        getDrawableWidth();
        int mDrawableHalfWidth = Math.round(mDrawableWidth / 2);

        if(localY < mOneFourHeight){
            pointView = (ImageView) mContainer.findViewById(R.id.point_top_view);
            pointView.setVisibility(View.VISIBLE);
            pointLayoutParams = (RelativeLayout.LayoutParams) pointView.getLayoutParams();
            assert pointLayoutParams != null;
            if(localX < mOneFourWidth){
                posX = localX;
                posY = localY + mAnchorHeight;
            }else if(localX > mOneFourWidth && localX < mHalfWidth){
                posX = mOneFourWidth;
                posY = localY + mAnchorHeight;
            }else if(localX > mHalfWidth && localX < mThreeFourWidth){
                posX = mHalfWidth;
                posY = localY + mAnchorHeight;
            }else if(localX > mThreeFourWidth && localX < mScreenWidth){
                posX = mScreenWidth - mContainerWidth;
                posY = localY + mAnchorHeight;
            }
            pointLayoutParams.leftMargin = localX - posX + mAnchorWidth / 2 - mDrawableHalfWidth;
            pointView.setLayoutParams(pointLayoutParams);
        }else {
            pointView = (ImageView) mContainer.findViewById(R.id.point_bottom_view);
            pointView.setVisibility(View.VISIBLE);
            pointLayoutParams = (RelativeLayout.LayoutParams) pointView.getLayoutParams();
            assert pointLayoutParams != null;
            if(localX < mOneFourWidth){
                posX = localX;
                posY = localY - mContainerHeight;
            }else if(localX > mOneFourWidth && localX < mHalfWidth){
                posX = mOneFourWidth;
                posY = localY - mContainerHeight;
            }else if(localX > mHalfWidth && localX < mThreeFourWidth){
                posX = mHalfWidth;
                posY = localY - mContainerHeight;
            }else if(localX > mThreeFourWidth && localX < mScreenWidth){
                posX = mScreenWidth - mContainerWidth;
                posY = localY - mContainerHeight;
            }
            pointLayoutParams.leftMargin = localX - posX + mAnchorWidth / 2 - mDrawableHalfWidth;
            pointView.setLayoutParams(pointLayoutParams);
        }
    }

    private void prepareShow(){
        if(mContainer == null){
            throw new IllegalStateException("You should set a layout for window.");
        }
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setContentView(mContainer);
    }

    private void getDrawableWidth(){
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.bubble_arrow_bottom);
        if(drawable != null){
            mDrawableWidth = drawable.getMinimumWidth();
        }
    }
}




