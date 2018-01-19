package com.feeling.mybaseapp.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.adapter.FragmentViewPagerStateAdapter;
import com.feeling.mybaseapp.base.BaseActivity;
import com.feeling.mybaseapp.ui.fragment.LazyFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentLazyActivity extends BaseActivity {

    private SlidingTabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private FragmentViewPagerStateAdapter pagerAdapter;
    private String[] titles = {"first","second","third","forth","fifth"};
    @Override
    protected void initView() {
        setContentView(R.layout.activity_fragment_lazy);
        tabLayout=findViewById(R.id.tab_layout);
        viewPager=findViewById(R.id.view_pager);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        if(fragments==null){
            fragments=new ArrayList<>();
            LazyFragment first = LazyFragment.newInstance("first");
            LazyFragment second = LazyFragment.newInstance("second");
            LazyFragment third = LazyFragment.newInstance("third");
            LazyFragment forth = LazyFragment.newInstance("forth");
            LazyFragment fifth = LazyFragment.newInstance("fifth");
            fragments.add(first);
            fragments.add(second);
            fragments.add(third);
            fragments.add(forth);
            fragments.add(fifth);
        }

        if(pagerAdapter==null){
            pagerAdapter = new FragmentViewPagerStateAdapter(getSupportFragmentManager(),fragments);
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setViewPager(viewPager,titles);
        }
    }

    @Override
    public void onSuccess(Object object, int flag) {

    }

    @Override
    public void onError(Throwable e, int code, int flag) {

    }
}
