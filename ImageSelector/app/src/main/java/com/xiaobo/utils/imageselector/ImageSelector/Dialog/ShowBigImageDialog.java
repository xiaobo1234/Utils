package com.xiaobo.utils.imageselector.ImageSelector.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaobo.utils.imageselector.ImageSelector.Utils.DisplayUtil;
import com.xiaobo.utils.imageselector.ImageSelector.View.TouchImageView;
import com.xiaobo.utils.imageselector.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ShowBigImageDialog implements ViewPager.OnPageChangeListener {

    private RelativeLayout relate;
    private Dialog mDialog;
    private View view;
    private ViewPager pager;
    private TextView text1;
    private TextView text2;
    private LinearLayout back_layout;
    private List<View> lists = new ArrayList<View>();

    public void setOnView(Context context, List<String> list) {
        view = LayoutInflater.from(context).inflate(R.layout.image_dialog_show_big_image, null);
        relate = (RelativeLayout) view.findViewById(R.id.relate);
        pager = (ViewPager) view.findViewById(R.id.pager);
        text1 = (TextView) view.findViewById(R.id.text1);
        text2 = (TextView) view.findViewById(R.id.text2);
        back_layout = (LinearLayout) view.findViewById(R.id.back_layout);

        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.image_item_photo_view, null);
            TouchImageView photo = (TouchImageView) view.findViewById(R.id.detail_image);
            int width = DisplayUtil.getWindowWidth(context);
            int height = DisplayUtil.getWindowHeight(context);
            Picasso.with(context)
                    .load(new File(list.get(i)))
                    .placeholder(R.drawable.image_icon_default)
                    .error(R.drawable.image_icon_default)
                    .centerInside()
                    .resize(width, height)
                    .into(photo);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            lists.add(view);
        }

        MyAdapter adapter = new MyAdapter(context, lists);
        pager.setAdapter(adapter);

        relate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }

        });
        back_layout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }

        });
        pager.setOnPageChangeListener(this);
        text2.setText(list.size() + "");

        mDialog = new Dialog(context, R.style.DialogFullScreen);
        mDialog.setContentView(view);
    }

    public void show() {
        if (mDialog != null)
            mDialog.show();
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int i) {
        text1.setText(i + 1 + "");
    }

    class MyAdapter extends PagerAdapter {

        private Context mContext;
        private List<View> list;

        public MyAdapter(Context mContext, List<View> list) {
            super();
            this.mContext = mContext;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }
}
