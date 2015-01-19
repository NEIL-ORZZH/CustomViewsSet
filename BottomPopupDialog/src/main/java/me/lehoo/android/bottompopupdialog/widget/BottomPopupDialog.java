package me.lehoo.android.bottompopupdialog.widget;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.lang.reflect.Method;
import me.lehoo.android.bottompopupdialog.R;

/**
 * Created by zhoulq on 15-1-19.
 */
public class BottomPopupDialog extends Dialog implements View.OnClickListener {

    static {
        // Android allows a system property to override the presence of the navigation bar.
        // Used by the emulator.
        // See https://github.com/android/platform_frameworks_base/blob/master/policy/src/com/android/internal/policy/impl/PhoneWindowManager.java#L1076
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
                sNavBarOverride = null;
            }
        }
    }

    private Context mContext;

    private static String sNavBarOverride;

    private final boolean mInPortrait;

    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
    private static final String NAV_BAR_WIDTH_RES_NAME = "navigation_bar_width";
    private static final String SHOW_NAV_BAR_RES_NAME = "config_showNavigationBar";

    private static int Theme = R.style.PopupDialogStyle;

    private View.OnClickListener mPickButtonListener;
    private View.OnClickListener mTakeButtonListener;
    private View.OnClickListener mCancelListener;

    public BottomPopupDialog(Context context, View.OnClickListener pickButtonListener, View.OnClickListener takeButtonListener, View.OnClickListener cancelListener) {
        super(context, Theme);
        mContext = context;
        mInPortrait = (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

        if(mPickButtonListener == null){
            mPickButtonListener = pickButtonListener;
        }

        if(mTakeButtonListener == null){
            mTakeButtonListener = takeButtonListener;
        }

        if(mCancelListener == null){
            mCancelListener = cancelListener;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;

        TypedArray a = getContext().obtainStyledAttributes(new int[]{android.R.attr.layout_width});
        try {
            params.width = a.getLayoutDimension(0, ViewGroup.LayoutParams.MATCH_PARENT);
        } finally {
            a.recycle();
        }

        getWindow().setAttributes(params);
    }

    private void init() {
        setCanceledOnTouchOutside(true);
        final FrameLayout mDialogView = (FrameLayout) View.inflate(mContext, R.layout.bottom_popup_dialog_layout, null);
        setContentView(mDialogView);

        TextView pickView = (TextView) mDialogView.findViewById(R.id.pick_gallery);
        pickView.setOnClickListener(this);

        TextView takeView = (TextView) mDialogView.findViewById(R.id.take_photo);
        takeView.setOnClickListener(this);

        TextView cancelView = (TextView) mDialogView.findViewById(R.id.cancel_action);
        cancelView.setOnClickListener(this);

    }

    public int getBottombarHeight(Context context){
        return getNavigationBarHeight(context);
    }

    @TargetApi(14)
    private int getActionBarHeight(Context context) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            TypedValue tv = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
            result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return result;
    }


    @TargetApi(14)
    private int getNavigationBarHeight(Context context) {
        Resources res = context.getResources();
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar(context)) {
                String key;
                if (mInPortrait) {
                    key = NAV_BAR_HEIGHT_RES_NAME;
                } else {
                    key = NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME;
                }
                return getInternalDimensionSize(res, key);
            }
        }
        return result;
    }


    @TargetApi(14)
    private int getNavigationBarWidth(Context context) {
        Resources res = context.getResources();
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar(context)) {
                return getInternalDimensionSize(res, NAV_BAR_WIDTH_RES_NAME);
            }
        }
        return result;
    }

    private int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @TargetApi(14)
    private boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier(SHOW_NAV_BAR_RES_NAME, "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag (see static block)
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.pick_gallery){
            if(mPickButtonListener != null){
                mPickButtonListener.onClick(v);
            }
        }else if(v.getId() == R.id.take_photo){
            mTakeButtonListener.onClick(v);

        }else if(v.getId() == R.id.cancel_action){
mCancelListener.onClick(v);
        }
        this.cancel();
    }
}
