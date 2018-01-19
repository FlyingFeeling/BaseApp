package com.feeling.mybaseapp.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.utils.BitmapUtil;


/**
 * 自定义toolBar
 * Created by zcs on 2016/11/1.
 */

public class MyToolbar extends Toolbar {
    private LayoutInflater mInflater;
    private View mView;
    /**
     * 左侧TV
     */
    public TextView tv_toolbar_left;
    /**
     * 搜索框
     */
    public EditText mSearchView;//搜索框
    /**
     * 标题
     */
    public TextView mTitleView;//标题
    /**
     * 右侧按钮
     */
    public ImageButton mRightButton;//右侧按钮
    /**
     * 左侧按钮
     */
    public ImageButton mNavButtonView;
    /**
     * 搜索按钮
     */
    public ImageView mSearchButton;//搜索按钮
    /**
     * 消息数量提示图标
     */
    public TextView mMessageAlert;
    public TextView tv_toolbar_right;

    private boolean clickAble = false;//右边按钮是否可点击

    private Drawable rightButtonIcon;

    public boolean isClickAble() {
        return clickAble;
    }

    public void setClickAble(boolean clickAble) {
        this.clickAble = clickAble;
        setRightButton(rightButtonIcon);
    }

    private Drawable mSearchIcon;

    public MyToolbar(Context context) {
        this(context, null);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();

        if (attrs != null) {
            final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.MyToolbar, defStyleAttr, 0);

            final boolean isSearch = a.getBoolean(R.styleable.MyToolbar_isSearch, false);
            mSearchIcon = a.getDrawable(R.styleable.MyToolbar_searchIcon);
            String hintText = a.getString(R.styleable.MyToolbar_editTextHint);
            rightButtonIcon = a.getDrawable(R.styleable.MyToolbar_rightButtonIcon);
            clickAble = a.getBoolean(R.styleable.MyToolbar_clickAble, true);
            if (rightButtonIcon != null) {
                setRightButton(rightButtonIcon);
            }
            if (isSearch) {
                setSearchEditText(hintText);
            }
            if (mSearchIcon != null) {
                setSearchButton(mSearchIcon);
            }

            String rightButtonText = a.getString(R.styleable.MyToolbar_rightButtonText);
            if (null != rightButtonText) {
                setRightButtonText(rightButtonText);
            }
            a.recycle();
        }
    }

    /**
     * 设置右侧带文字按钮
     */
    public void setRightButtonText(String rightButtonText) {
        tv_toolbar_right.setText(rightButtonText);
        tv_toolbar_right.setVisibility(View.VISIBLE);
        mRightButton.setVisibility(View.INVISIBLE);
    }

    /**
     * 初始化布局资源
     */
    private void initView() {
        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar_layout, null);
            tv_toolbar_left = (TextView) mView.findViewById(R.id.toolbar_left_tv);
            mSearchView = (EditText) mView.findViewById(R.id.toolbar_searchView);
            mNavButtonView = (ImageButton) mView.findViewById(R.id.navButton);
            mTitleView = (TextView) mView.findViewById(R.id.toolbar_title);
            mRightButton = (ImageButton) mView.findViewById(R.id.toolbar_rightButton);
            mSearchButton = (ImageView) mView.findViewById(R.id.toolbar_search_button);
            mMessageAlert = (TextView) mView.findViewById(R.id.toolbar_messageAlert);
            tv_toolbar_right = (TextView) mView.findViewById(R.id.tv_toolbar_right);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, params);
        }
    }

    /**
     * 设置左侧带文字按钮
     */
    public void setLeftButtonText(String rightButtonText) {
        tv_toolbar_left.setText(rightButtonText);
        tv_toolbar_left.setVisibility(View.VISIBLE);
    }

    /**
     * 设置搜索框
     */
    public void setSearchEditText(String str) {
        mSearchView.setVisibility(View.VISIBLE);
        if (str != null) {
            mSearchView.setHint(str);
        }
    }

    public void setSearchButton(Drawable searchButton) {
        mSearchButton.setImageDrawable(mSearchIcon);
        mSearchButton.setVisibility(View.VISIBLE);
    }

    /**
     * 添加文本输入监听器
     */
    public void addTextChangedListener(TextWatcher watcher) {
        mSearchView.addTextChangedListener(watcher);
    }

    /**
     * 设置search Button监听器
     */
    public void setSearchButtonOnClickListener(OnClickListener listener) {
        mSearchButton.setOnClickListener(listener);
    }

    /**
     * 设置右侧button
     */
    public void setRightButton(@DrawableRes int resId) {
        this.setRightButton(AppCompatResources.getDrawable(getContext(), resId));
    }

    /**
     * 设置右侧button
     */
    public void setRightButton(Drawable drawable) {
//        initView();
        if (mRightButton == null)
            mRightButton = (ImageButton) mView.findViewById(R.id.toolbar_rightButton);
//        mRightButton.setImageDrawable(drawable);
        Bitmap bitmap = BitmapUtil.drawableToBitmap(drawable);
        if (clickAble) {
            mRightButton.setImageDrawable(drawable);
        } else {
            mRightButton.setImageBitmap(BitmapUtil.lum(bitmap, 90));
        }
        mRightButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        setTitle(getContext().getText(resId));
    }

    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTitleView != null)
            mTitleView.setText(title);
        mTitleView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        initView();
        if (icon != null) {
            if (mNavButtonView == null) {
                mNavButtonView = (ImageButton) mView.findViewById(R.id.navButton);
            }
            mNavButtonView.setImageDrawable(icon);
            mNavButtonView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setNavigationOnClickListener(OnClickListener listener) {
        initView();
        mNavButtonView.setOnClickListener(listener);
    }

    public void setMessageAlert(int number) {
        initView();
        if (mMessageAlert == null) {
            mMessageAlert = (TextView) mView.findViewById(R.id.toolbar_messageAlert);
        }
        mMessageAlert.setText(String.valueOf(number));
        mMessageAlert.setVisibility(View.VISIBLE);
    }

    public void setMessageAlertGone() {
        initView();
        if (mMessageAlert == null) {
            return;
        }
        mMessageAlert.setText("");
        mMessageAlert.setVisibility(View.GONE);
    }

}
