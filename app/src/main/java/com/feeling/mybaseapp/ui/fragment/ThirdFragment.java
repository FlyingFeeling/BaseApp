package com.feeling.mybaseapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.feeling.mybaseapp.R;
import com.feeling.mybaseapp.adapter.TextAdapter;
import com.feeling.mybaseapp.base.BaseFragment;
import com.feeling.mybaseapp.ui.view.GridLayoutDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private String mParam1;

    private ImageView imageView;

    public ThirdFragment() {
        // Required empty public constructor
    }

    public static ThirdFragment newInstance(String param1) {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_third;
    }

    @Override
    protected void initView() {
        unbinder = ButterKnife.bind(this, parentView);
    }

    @Override
    protected void initData() {
        List<String> datas=new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("item"+i);
        }
        TextAdapter adapter=new TextAdapter(getContext(),datas);
        GridLayoutManager manager=new GridLayoutManager(getContext(),4);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new GridLayoutDivider.Builder(getContext())
                .setColorResId(R.color.colorAccent)
                .setOrientation(GridLayoutDivider.VERTICAL)
                .setDividerSize(10).build());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(Object object, int flag) {

    }

    @Override
    public void onError(Throwable e, int code, int flag) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
