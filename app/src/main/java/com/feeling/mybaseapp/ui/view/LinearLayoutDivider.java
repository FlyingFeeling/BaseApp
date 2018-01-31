package com.feeling.mybaseapp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.feeling.mybaseapp.utils.DensityUtil;

/**
 * Created by 123 on 2018/1/29.
 */

public class LinearLayoutDivider extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final String TAG = "DividerItem";
    private static final int[] ATTRS = new int[]{ android.R.attr.listDivider };

    private Context context;
    private Drawable mDivider;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private int paddingLeft,paddingRight;
    private int firstPadding;

    private int dividerSize;

    private final Rect mBounds = new Rect();

    private boolean isShowFirstDivider=false;
    private boolean isShowLastDivider=false;

    public LinearLayoutDivider(Builder builder){
        this.context=builder.context;
        mOrientation=builder.orientation;
        dividerSize=builder.dividerSize;
        paddingLeft=builder.paddingLeft;
        paddingRight=builder.paddingRight;
        firstPadding=builder.firstPadding;
        mDivider=builder.drawable;
        isShowLastDivider=builder.isShowLast;
        isShowFirstDivider=builder.isShowFirst;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null || mDivider == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft()+paddingLeft;
            right = parent.getWidth() - parent.getPaddingRight()-paddingRight;
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = paddingLeft;
            right = parent.getWidth()-paddingRight;
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            if(!isShowLastDivider&&i==childCount-1){
                continue;
            }
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - dividerSize;
            if(isShowFirstDivider && i==0){
                mDivider.setBounds(left, mBounds.top, right, mBounds.top+firstPadding);
                mDivider.draw(canvas);
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - dividerSize;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mDivider == null) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        if (mOrientation == VERTICAL) {
            if(parent.getChildAdapterPosition(view)==0 && isShowFirstDivider){
                outRect.set(0, firstPadding, 0, dividerSize);
                return;
            }
            outRect.set(0, 0, 0, dividerSize);
        } else {
            outRect.set(0, 0, dividerSize, 0);
        }
    }


    public static class Builder{
        private Context context;
        private int orientation;
        private int paddingLeft,paddingRight;
        private int firstPadding;
        private int dividerSize;
        private Drawable drawable;
        private boolean isShowLast;
        private boolean isShowFirst;

        public Builder(Context context) {
            this.context = context;
            init();
        }

        private void init(){
            orientation=LinearLayoutDivider.VERTICAL;
            paddingLeft=0;
            paddingRight=0;
            firstPadding=0;
            dividerSize=DensityUtil.dp2px(context,1);
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            drawable = a.getDrawable(0);
            if (drawable == null) {
                Log.w(TAG, "@android:attr/listDivider was not set in the theme used for this "
                        + "DividerItemDecoration. Please set that attribute all call setDrawable()");
            }
            isShowLast=false;
            isShowFirst=false;
        }

        public Builder setOrientation(int orientation){
            this.orientation=orientation;
            return this;
        }

        public Builder setPaddingLeft(int paddingLeft) {
            this.paddingLeft = DensityUtil.dp2px(context,paddingLeft);
            return this;
        }

        public Builder setPaddingRight(int paddingRight) {
            this.paddingRight = DensityUtil.dp2px(context,paddingRight);
            return this;
        }

        public Builder setDividerSize(int dividerSize) {
            this.dividerSize = dividerSize;
            return this;
        }

        public Builder setShowLast(boolean showLast) {
            isShowLast = showLast;
            return this;
        }

        public Builder setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder setColor(int color){
            return setDrawable(new ColorDrawable(color));
        }

        public Builder setColorResId(@ColorRes int color){
            return setDrawable(new ColorDrawable(ContextCompat.getColor(context,color)));
        }

        public Builder setShowFirst(boolean showFirst) {
            isShowFirst = showFirst;
            return this;
        }

        public Builder setFirstPadding(int firstPadding) {
            this.firstPadding = DensityUtil.dp2px(context,firstPadding);
            return this;
        }

        public int getOrientation() {
            return orientation;
        }

        public int getPaddingLeft() {
            return paddingLeft;
        }

        public int getPaddingRight() {
            return paddingRight;
        }

        public int getDividerSize() {
            return dividerSize;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public boolean isShowLast() {
            return isShowLast;
        }

        public LinearLayoutDivider builder(){
            return new LinearLayoutDivider(this);
        }
    }
}
