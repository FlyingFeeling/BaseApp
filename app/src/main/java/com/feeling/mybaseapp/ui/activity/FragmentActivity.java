package com.feeling.mybaseapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.ui.fragment.FirstFragment;
import com.feeling.mybaseapp.ui.fragment.SecondFragment;
import com.feeling.mybaseapp.ui.fragment.ThirdFragment;

public class FragmentActivity extends AppCompatActivity {

    private String fragmentTag="";
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private ThirdFragment thirdFragment;
    private FragmentManager fragmentManager;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        if(savedInstanceState!=null){
            firstFragment = (FirstFragment) getSupportFragmentManager().findFragmentByTag("FirstFragment");
            secondFragment = (SecondFragment) getSupportFragmentManager().findFragmentByTag("SecondFragment");
            thirdFragment = (ThirdFragment) getSupportFragmentManager().findFragmentByTag("ThirdFragment");
            fragmentTag = savedInstanceState.getString("Tag");
        }
        initView();
        initData(savedInstanceState);
    }

    protected void initView() {
        radioGroup=findViewById(R.id.tab_group);
        fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        if(firstFragment==null){
            firstFragment=FirstFragment.newInstance("FirstFragment");
            secondFragment=SecondFragment.newInstance("SecondFragment");
            thirdFragment=ThirdFragment.newInstance("ThirdFragment");
            transaction.add(R.id.content_view,firstFragment,"FirstFragment");
            transaction.add(R.id.content_view,secondFragment,"SecondFragment");
            transaction.add(R.id.content_view,thirdFragment,"ThirdFragment");
        }
        transaction.hide(firstFragment);
        transaction.hide(secondFragment);
        transaction.hide(thirdFragment);
        transaction.commit();
    }

    protected void initData(Bundle savedInstanceState) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                switch (checkedId){
                    case R.id.first:
                        transaction.show(firstFragment);
                        transaction.hide(secondFragment);
                        transaction.hide(thirdFragment);
                        fragmentTag="FirstFragment";
                        break;
                    case R.id.second:
                        transaction.show(secondFragment);
                        transaction.hide(firstFragment);
                        transaction.hide(thirdFragment);
                        fragmentTag="SecondFragment";
                        break;
                    case R.id.third:
                        transaction.show(thirdFragment);
                        transaction.hide(firstFragment);
                        transaction.hide(secondFragment);
                        fragmentTag="ThirdFragment";
                        break;
                    default:
                        break;
                }
                transaction.commit();
            }
        });

        switch (fragmentTag){
            case "FirstFragment":
                radioGroup.check(R.id.first);
                break;
            case "SecondFragment":
                radioGroup.check(R.id.second);
                break;
            case "ThirdFragment":
                radioGroup.check(R.id.third);
                break;
            default:
                radioGroup.check(R.id.first);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Tag",fragmentTag);
    }
}
