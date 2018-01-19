package com.feeling.mybaseapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.feeling.mybaseapp.R;


/**
 * Created by 123 on 2017/8/14.
 */

public class LoadingDialog extends Dialog {
    TextView loadingText;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.project_fabu_dialog_style);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        View view= LayoutInflater.from(context).inflate(R.layout.layout_progress_loading,null);
        TextView tvLoading= (TextView) view.findViewById(R.id.loading_text);
        setContentView(view);
        this.setCanceledOnTouchOutside(false);
    }

    public LoadingDialog setLoadingText(TextView loadingText) {
        this.loadingText = loadingText;
        return this;
    }
}
