package com.feeling.mybaseapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class CustomListViewForScrollView extends ListView {

  public CustomListViewForScrollView(Context context) {
    super(context);

  }

  public CustomListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);

  }

  public CustomListViewForScrollView(Context context, AttributeSet attrs) {
    super(context, attrs);

  }

  @Override
  public void setAdapter(ListAdapter adapter) {
    // TODO Auto-generated method stub
    super.setAdapter(adapter);
//    setListViewHeightBasedOnChildren();
  }
  
  /*
   * 动态设置ListView组建的高度
   */
  private void setListViewHeightBasedOnChildren() {
    ListAdapter listAdapter = getAdapter();
    if (listAdapter == null) {
      return;
    }
    int totalHeight = 0;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, this);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }
    ViewGroup.LayoutParams params = getLayoutParams();
    params.height = totalHeight + (getDividerHeight() * (listAdapter.getCount() - 1));
    // params.height += 5;// if without this statement,the listview will be
    // a
    // little short
    // listView.getDividerHeight()获取子项间分隔符占用的高度
    // params.height最后得到整个ListView完整显示需要的高度
    setLayoutParams(params);
  }
  
  /** 
   * 设置不滚动 
   */  
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
  {  
          int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                          MeasureSpec.AT_MOST);
          super.onMeasure(widthMeasureSpec, expandSpec);  

  }  

  

//  @Override
//  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//    super.onMeasure(widthMeasureSpec, expandSpec);
//  }


}
