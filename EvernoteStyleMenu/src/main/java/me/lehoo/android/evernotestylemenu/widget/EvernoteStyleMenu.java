package me.lehoo.android.evernotestylemenu.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.nineoldandroids.view.ViewHelper;
import me.lehoo.android.evernotestylemenu.R;

/**
 * Created by zhoulq on 15-1-30.
 */
public class EvernoteStyleMenu extends FrameLayout implements View.OnClickListener{

    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 6);
    private final Spring mSpring;

    private Context mContext;
    private View mContainer;
    private ImageView mComposeView;
    private FloatActionButton mMenuHandwriting, mMenuAudio, mMenuReminder, mMenuAttachment, mMenuCamera, mMenuText;

    private int mMenuBaseHeight = 0;
    private float mBaseY = 0;

    public EvernoteStyleMenu(Context context) {
        this(context, null);
    }

    public EvernoteStyleMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EvernoteStyleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mSpring = SpringSystem
                .create()
                .createSpring()
                .setSpringConfig(ORIGAMI_SPRING_CONFIG)
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        render();
                    }
                });

        initView(mContext);
    }

    private void initView(Context context){
        mContainer = LayoutInflater.from(context).inflate(R.layout.view_evernote_style_menu, this, true);
        mComposeView = (ImageView) mContainer.findViewById(R.id.menu_compose);
        mComposeView.setOnClickListener(this);

        mMenuHandwriting = (FloatActionButton) mContainer.findViewById(R.id.menu_handwriting);
        mMenuAudio = (FloatActionButton) mContainer.findViewById(R.id.menu_audio);
        mMenuReminder = (FloatActionButton) mContainer.findViewById(R.id.menu_reminder);
        mMenuAttachment = (FloatActionButton) mContainer.findViewById(R.id.menu_attachment);
        mMenuCamera = (FloatActionButton) mContainer.findViewById(R.id.menu_camera);
        mMenuText = (FloatActionButton) mContainer.findViewById(R.id.menu_text);

        mMenuHandwriting.setText(getResources().getString(R.string.menu_handwriting));
        mMenuAudio.setText(getResources().getString(R.string.menu_audio));
        mMenuReminder.setText(getResources().getString(R.string.menu_reminder));
        mMenuAttachment.setText(getResources().getString(R.string.menu_attachment));
        mMenuCamera.setText(getResources().getString(R.string.menu_camera));
        mMenuText.setText(getResources().getString(R.string.menu_text));

        mMenuHandwriting.setImageResource(R.drawable.ic_handwriting);
        mMenuAudio.setImageResource(R.drawable.ic_audio);
        mMenuReminder.setImageResource(R.drawable.ic_reminder);
        mMenuAttachment.setImageResource(R.drawable.ic_attachment);
        mMenuCamera.setImageResource(R.drawable.ic_camera);
        mMenuText.setImageResource(R.drawable.ic_text);

        mMenuHandwriting.setOnClickListener(this);
        mMenuAudio.setOnClickListener(this);
        mMenuReminder.setOnClickListener(this);
        mMenuAttachment.setOnClickListener(this);
        mMenuCamera.setOnClickListener(this);
        mMenuText.setOnClickListener(this);

        mContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mMenuBaseHeight = mComposeView.getHeight() + 5;
                mBaseY = ViewHelper.getTranslationY(mComposeView);
                render();
                mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == mComposeView.getId()){
            toggleMenu();
        }else if(v.getId() == mMenuHandwriting.getId()){
            toggleMenu();
        }else if(v.getId() == mMenuAudio.getId()){
            toggleMenu();
        }else if(v.getId() == mMenuReminder.getId()){
            toggleMenu();
        }else if(v.getId() == mMenuAttachment.getId()){
            toggleMenu();
        }else if(v.getId() == mMenuCamera.getId()){
            toggleMenu();
        }else if(v.getId() == mMenuText.getId()){
            toggleMenu();
        }
    }

    private void toggleMenu(){
        if(mSpring.getCurrentValue() < 1.0){
            mSpring.setEndValue(1);
        }else {
            mSpring.setEndValue(0);
        }
    }

    private void render(){
        double value = mSpring.getCurrentValue();

        float composeMenuRotate = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 135);
        ViewHelper.setRotation(mComposeView, composeMenuRotate);

        // handwriting menu
        float handwritingMenuTranslationY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mBaseY, mBaseY -  6 * mMenuBaseHeight);
        ViewHelper.setTranslationY(mMenuHandwriting, handwritingMenuTranslationY);

        float handwritingMenuScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setScaleX(mMenuHandwriting, handwritingMenuScale);
        ViewHelper.setScaleY(mMenuHandwriting, handwritingMenuScale);

        float handwritingMenuAlpha = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setAlpha(mMenuHandwriting, handwritingMenuAlpha);


        // audio menu
        float audioMenuTranslationY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mBaseY, mBaseY - 5 * mMenuBaseHeight);
        ViewHelper.setTranslationY(mMenuAudio, audioMenuTranslationY);

        float audioMenuScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setScaleX(mMenuAudio, audioMenuScale);
        ViewHelper.setScaleY(mMenuAudio, audioMenuScale);

        float audioMenuAlpha = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setAlpha(mMenuAudio, audioMenuAlpha);

        // reminder menu
        float reminderMenuTranslationY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mBaseY, mBaseY - 4 * mMenuBaseHeight);
        ViewHelper.setTranslationY(mMenuReminder, reminderMenuTranslationY);

        float reminderMenuScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setScaleX(mMenuReminder, reminderMenuScale);
        ViewHelper.setScaleY(mMenuReminder, reminderMenuScale);

        float reminderMenuAlpha = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setAlpha(mMenuReminder, reminderMenuAlpha);

        // attachment menu
        float attachmentMenuTranslationY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mBaseY, mBaseY - 3 * mMenuBaseHeight);
        ViewHelper.setTranslationY(mMenuAttachment, attachmentMenuTranslationY);

        float attachmentMenuScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setScaleX(mMenuAttachment, attachmentMenuScale);
        ViewHelper.setScaleY(mMenuAttachment, attachmentMenuScale);

        float attachmentMenuAlpha = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setAlpha(mMenuAttachment, attachmentMenuAlpha);

        // camera menu
        float cameraMenuTranslationY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mBaseY, mBaseY - 2 * mMenuBaseHeight);
        ViewHelper.setTranslationY(mMenuCamera, cameraMenuTranslationY);

        float cameraMenuScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setScaleX(mMenuCamera, cameraMenuScale);
        ViewHelper.setScaleY(mMenuCamera, cameraMenuScale);

        float cameraMenuAlpha = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setAlpha(mMenuCamera, cameraMenuAlpha);

        // reminder menu
        float textMenuTranslationY = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, mBaseY, mBaseY - mMenuBaseHeight);
        ViewHelper.setTranslationY(mMenuText, textMenuTranslationY);

        float textMenuScale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setScaleX(mMenuText, textMenuScale);
        ViewHelper.setScaleY(mMenuText, textMenuScale);

        float textMenuAlpha = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 0, 1);
        ViewHelper.setAlpha(mMenuText, textMenuAlpha);
    }
}
