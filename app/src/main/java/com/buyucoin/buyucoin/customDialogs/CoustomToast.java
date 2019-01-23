package com.buyucoin.buyucoin.customDialogs;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buyucoin.buyucoin.R;

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

    static final int ICON_DANGER = R.drawable.toast_danger;
    static final int ICON_PENDING = R.drawable.toast_pending;
    static final int ICON_SUCCESS = R.drawable.toast_success;
    static final int ICON_NORMAL = R.drawable.ic_mood_black_24dp;

    private String msg;
    private Context context;
    private LayoutInflater layoutInflater;

    Toast toast;
    TextView text;
    ImageView icon;
    LinearLayout layout;

    public CoustomToast(Context context,Activity activity, String msg,String type) {
        this.context = context;
        this.layoutInflater = activity.getLayoutInflater();
        this.msg = msg;
        this.toast = new Toast(context);
        View view = layoutInflater.inflate(R.layout.coustom_toast,null);
        text = view.findViewById(R.id.toast_msg);
        icon = view.findViewById(R.id.toast_img);
        layout = view.findViewById(R.id.toast_background);
        text.setText(msg);
        toast.setGravity(Gravity.CENTER,0,0);
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
                icon.setImageDrawable(context.getDrawable(ICON_DANGER));
                text.setTextColor(context.getResources().getColor(NORMAL));
                break;
            case TYPE_PENDING:
                layout.getBackground().setLevel(2);
                icon.setImageDrawable(context.getDrawable(ICON_PENDING));
                text.setTextColor(context.getResources().getColor(NORMAL));
                break;
            case TYPE_SUCCESS:
                layout.getBackground().setLevel(3);
                icon.setImageDrawable(context.getDrawable(ICON_SUCCESS));
                text.setTextColor(context.getResources().getColor(NORMAL));
                break;
            case TYPE_NORMAL:
                layout.getBackground().setLevel(0);
                icon.setImageDrawable(context.getDrawable(ICON_NORMAL));
                text.setTextColor(context.getResources().getColor(BLACK));

                break;
            default:
                layout.getBackground().setLevel(0);
                icon.setImageDrawable(context.getDrawable(ICON_NORMAL));
                text.setTextColor(context.getResources().getColor(BLACK));

                break;
        }

    }








}
