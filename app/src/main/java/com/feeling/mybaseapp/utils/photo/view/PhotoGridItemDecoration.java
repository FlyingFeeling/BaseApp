package com.feeling.mybaseapp.utils.photo.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 图片分割线
 * Created by Administrator on 2016/11/26.
 */

public class PhotoGridItemDecoration extends RecyclerView.ItemDecoration {
    private Paint mPaint;
    public PhotoGridItemDecoration() {
        super();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
         /*设置填充*/
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        drawable(c,parent);
    }

    private void drawable(Canvas c, RecyclerView parent) {
        final View child = parent.getChildAt( 0 ) ;
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        final int left = parent.getPaddingLeft()+layoutParams.leftMargin;
        final int right = parent.getPaddingRight()+layoutParams.rightMargin;
        final int bottom = parent.getPaddingBottom()+layoutParams.bottomMargin;
        final int top = parent.getPaddingTop()+layoutParams.topMargin;
        final int childSize = parent.getChildCount();
        c.drawRect(5,5,5,5,mPaint);
        for(int i = 0 ; i < childSize ; i ++){

        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(3,3,3,3);
    }
}
