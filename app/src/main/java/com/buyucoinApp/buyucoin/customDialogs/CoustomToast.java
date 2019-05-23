package com.buyucoinApp.buyucoin.customDialogs;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoinApp.buyucoin.R;

public class CoustomToast{

    static final int DANGER = R.color.colorRed;
    static final int PENDING = R.color.colorPending;
    static final int SUCCESS = R.color.g1b67e8;
    static final int NORMAL = R.color.colorWhite;
    static final int BLACK = R.color.colorBlack;

    public static final String TYPE_DANGER = "DANGER";
    public static final String TYPE_PENDING = "PENDING";
    public static final String TYPE_SUCCESS = "SUCCESS";
    public static final String TYPE_NORMAL = "NORMAL";


    private Context context;

    private Toast toast;
    private TextView text;
    private LinearLayout layout;

    public CoustomToast(Context context,String message,String type) {
        this.context = context;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        this.toast = new Toast(context);
        View view = layoutInflater.inflate(R.layout.coustom_toast,null);
        text = view.findViewById(R.id.toast_msg);
        layout = view.findViewById(R.id.toast_background);
        text.setText(message);
        toast.setGravity(Gravity.FILL | Gravity.TOP, 0, 60);

        toast.setDuration(Toast.LENGTH_LONG);
        switchIconAndColor(type);
        toast.setView(view);
    }

    public void showToast(){
        this.toast.show();
    }

    public void switchIconAndColor(String type){
        switch (type){
            case TYPE_DANGER:
                layout.getBackground().setLevel(1);
                text.setTextColor(context.getResources().getColor(NORMAL));
                break;
            case TYPE_PENDING:
                layout.getBackground().setLevel(2);
                text.setTextColor(context.getResources().getColor(NORMAL));
                break;
            case TYPE_SUCCESS:
                layout.getBackground().setLevel(3);
                text.setTextColor(context.getResources().getColor(NORMAL));
                break;
            case TYPE_NORMAL:
                layout.getBackground().setLevel(0);
                text.setTextColor(context.getResources().getColor(NORMAL));
                break;
            default:
                layout.getBackground().setLevel(0);
                text.setTextColor(context.getResources().getColor(NORMAL));
                break;
        }

    }








}
