package com.feeling.mybaseapp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.feeling.mybaseapp.utils.DensityUtil;

/**
 * Created by 123 on 2018/1/30.
 */

public class GridLayoutDivider extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    public static final int DEFAULT_DIVIDER_SIZE = 1;
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;
    private int dividerSize;
    private int mOrientation;
    private boolean isShowLastDivider = false;
    private final Rect mBounds = new Rect();

    public GridLayoutDivider(Builder builder) {
        mDivider = builder.drawable;
        dividerSize = builder.dividerSize;
        mOrientation = builder.orientation;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        if (spanCount < 0) return;
        canvas.save();
        if (mOrientation == HORIZONTAL) {
            drawHorizontal(canvas, parent, spanCount);
        } else {
            drawVertical(canvas, parent, spanCount);
        }
        canvas.restore();
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent, int spanCount) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);

            if (i % spanCount == 0) {
                mDivider.setBounds(mBounds.left, mBounds.bottom - dividerSize / 2, mBounds.right, mBounds.bottom);
                mDivider.draw(canvas);

                mDivider.setBounds(mBounds.right - dividerSize, mBounds.top, mBounds.right, mBounds.bottom - dividerSize / 2);
                mDivider.draw(canvas);
            } else if ((i + 1) % spanCount == 0) {
                mDivider.setBounds(mBounds.left, mBounds.top, mBounds.right, mBounds.top + dividerSize / 2);
                mDivider.draw(canvas);

                mDivider.setBounds(mBounds.right - dividerSize, mBounds.top + dividerSize / 2, mBounds.right, mBounds.bottom);
                mDivider.draw(canvas);
            } else {
                mDivider.setBounds(mBounds.left, mBounds.top, mBounds.right, mBounds.top + dividerSize / 2);
                mDivider.draw(canvas);

                mDivider.setBounds(mBounds.left, mBounds.bottom - dividerSize / 2, mBounds.right, mBounds.bottom);
                mDivider.draw(canvas);

                mDivider.setBounds(mBounds.right - dividerSize, mBounds.top + dividerSize / 2, mBounds.right, mBounds.bottom - dividerSize / 2);
                mDivider.draw(canvas);
            }
            if (i == childCount - 1 && (i + 1) % spanCount != 0) {
                mDivider.setBounds(mBounds.left, mBounds.bottom, mBounds.right, mBounds.bottom + dividerSize / 2);
                mDivider.draw(canvas);
            }
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent, int spanCount) {
        int itemSize=parent.getAdapter().getItemCount();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = ((RecyclerView.LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
            parent.getDecoratedBoundsWithMargins(child, mBounds);

            if (isShowLastDivider || !isLastRow(itemSize,spanCount,position)) {
                //draw水平的分割线
                mDivider.setBounds(mBounds.left, mBounds.bottom - dividerSize, mBounds.right, mBounds.bottom);
                mDivider.draw(canvas);
            }

            if (i % spanCount == 0) {
                //draw垂直的分割线
                mDivider.setBounds(mBounds.right - dividerSize / 2, mBounds.top, mBounds.right, mBounds.bottom);
                mDivider.draw(canvas);
            } else if ((i + 1) % spanCount == 0) {
                //draw垂直的分割线
                mDivider.setBounds(mBounds.left, mBounds.top, mBounds.left + dividerSize / 2, mBounds.bottom);
                mDivider.draw(canvas);
            } else {
                //draw垂直的分割线
                mDivider.setBounds(mBounds.left, mBounds.top, mBounds.left + dividerSize / 2, mBounds.bottom);
                mDivider.draw(canvas);
                mDivider.setBounds(mBounds.right - dividerSize / 2, mBounds.top, mBounds.right, mBounds.bottom);
                mDivider.draw(canvas);
            }
            if (i == childCount - 1 && (i + 1) % spanCount != 0) {
                mDivider.setBounds(mBounds.right, mBounds.top, mBounds.right + dividerSize / 2, mBounds.bottom);
                mDivider.draw(canvas);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        if (spanCount < 0) return;
        if (mDivider == null || spanCount < 0) {
            outRect.set(0, 0, 0, 0);
            return;
        }
        int childCount=parent.getAdapter().getItemCount();
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (mOrientation == VERTICAL) {
            if (!isShowLastDivider && isLastRow(childCount, spanCount, position)) {
                if (position % spanCount == 0) {
                    outRect.set(0, 0, dividerSize / 2, 0);
                } else if ((position + 1) % spanCount == 0) {
                    outRect.set(dividerSize / 2, 0, 0, 0);
                } else {
                    outRect.set(dividerSize / 2, 0, dividerSize / 2, 0);
                }
            } else {
                if (position % spanCount == 0) {
                    outRect.set(0, 0, dividerSize / 2, dividerSize);
                } else if ((position + 1) % spanCount == 0) {
                    outRect.set(dividerSize / 2, 0, 0, dividerSize);
                } else {
                    outRect.set(dividerSize / 2, 0, dividerSize / 2, dividerSize);
                }
            }

        } else {
            if (position % spanCount == 0) {
                outRect.set(0, 0, dividerSize, dividerSize / 2);
            } else if ((position + 1) % spanCount == 0) {
                outRect.set(0, dividerSize / 2, dividerSize, 0);
            } else {
                outRect.set(0, dividerSize / 2, dividerSize, dividerSize / 2);
            }
        }


    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    private boolean isLastRow(int childCount, int spanSize, int position) {
        int rem = childCount % spanSize;
        int mod = childCount / spanSize;
        if (rem > 0) {
            if (position + 1 > spanSize * mod) {
                return true;
            }
        } else if (rem == 0) {
            if (position + 1 > spanSize * (mod - 1)) {
                return true;
            }
        }
        return false;
    }

    public static class Builder {
        private Context context;
        private int dividerSize;
        private int orientation;
        private Drawable drawable;

        public Builder(Context context) {
            this.context = context;
            init();
        }

        private void init() {
            orientation=VERTICAL;
            dividerSize = DensityUtil.dp2px(context, DEFAULT_DIVIDER_SIZE);
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            drawable = a.getDrawable(0);
            a.recycle();
        }

        public int getDividerSize() {
            return dividerSize;
        }

        public Builder setDividerSize(int dividerSize) {
            this.dividerSize = DensityUtil.dp2px(context, dividerSize);
            return this;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public Builder setDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public Builder setColor(int color) {
            return setDrawable(new ColorDrawable(color));
        }

        public Builder setColorResId(@ColorRes int color) {
            return setDrawable(new ColorDrawable(ContextCompat.getColor(context, color)));
        }

        public int getOrientation() {
            return orientation;
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public GridLayoutDivider build() {
            return new GridLayoutDivider(this);
        }
    }
}
