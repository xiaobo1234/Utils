package com.xiaobo.utils.imageselector.ImageSelector.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.xiaobo.utils.imageselector.R;

public abstract class EnterDialog {

    private Dialog dialog;
    private View view;

    public EnterDialog(Context context){
        view = LayoutInflater.from(context).inflate(R.layout.image_dialog_enter, null);
        setClickListener();
        dialog = new Dialog(context, R.style.DialogFullScreen);
        dialog.setContentView(view);
    }

    private void setClickListener() {
        view.findViewById(R.id.item_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                doGoToPhone();
            }
        });
        view.findViewById(R.id.item_Photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                doGoToImg();
            }
        });
        view.findViewById(R.id.item_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.parent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void show()  {
        if (dialog != null)
            dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public abstract void doGoToImg();

    public abstract void doGoToPhone();

}
