package me.lehoo.android.evernotestylemenu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import me.lehoo.android.evernotestylemenu.R;

/**
 * Created by zhoulq on 15-1-30.
 */
public class FloatActionButton extends LinearLayout{
    private TextView mTextLabel;
    private ImageView mImageView;

    public FloatActionButton(Context context) {
        super(context);
        init(context);
    }

    public FloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_float_action_button, this, true);
        mTextLabel=(TextView) findViewById(R.id.button_text_label);
        mImageView=(ImageView) findViewById(R.id.button_image_view);
    }

    public void setText(String text){
        mTextLabel.setText(text);
        invalidate();
    }

    public void setImageResource(int resId){
        mImageView.setImageResource(resId);
        invalidate();
    }
}
