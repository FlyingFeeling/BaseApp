package com.feeling.mybaseapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feeling.mybaseapp.R;

import java.util.List;

/**
 * Created by 123 on 2018/1/18.
 */

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder>{

    private Context context;
    private List<String> datas;
    private LayoutInflater inflater;

    public TextAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_item,parent,false);
        TextViewHolder viewHolder=new TextViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position) {
        holder.textView.setText(position+"");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public TextViewHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text);
        }
    }
}
