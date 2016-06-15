package com.xiaobo.utils.imageselector.ImageSelector.Activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageDatabase;
import com.xiaobo.utils.imageselector.ImageSelector.View.TouchImageView;
import com.xiaobo.utils.imageselector.ImageSelector.Utils.DisplayUtil;
import com.xiaobo.utils.imageselector.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 提供外部看大图模式，并提供按钮 删除指定选中图片
 *
 */
public class EditSelectedActivity extends Activity implements ViewPager.OnPageChangeListener {

    private TextView text1;
    private TextView text2;
    private ViewPager pager_images;

    private int currentItem = 0;
    private List<View> lists = new ArrayList<View>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity_edit_selected);

        initViews();

        if (ImageDatabase.selectImages.size() <= 0) {
            finish();
        }

        // 适配
        lists = new ArrayList<View>();
        for (int i = 0; i < ImageDatabase.selectImages.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.image_item_photo_view, null);
            TouchImageView photo = (TouchImageView) view.findViewById(R.id.detail_image);
            int width = DisplayUtil.getWindowWidth(this);
            int height = DisplayUtil.getWindowHeight(this);
            Picasso.with(this)
                    .load(new File(ImageDatabase.selectImages.get(i).getImagePath()))
                    .placeholder(R.drawable.image_icon_default)
                    .error(R.drawable.image_icon_default)
                    .centerInside()
                    .resize(width, height)
                    .into(photo);
            lists.add(view);
        }
        adapter = new MyAdapter(this, lists);
        pager_images.setAdapter(adapter);
        pager_images.setOnPageChangeListener(this);

        // index 显示
        text2.setText(lists.size() + "");
        currentItem = getIntent().getIntExtra("current_index", 0);
        if (currentItem < lists.size()) {
            pager_images.setCurrentItem(currentItem);
            text1.setText((currentItem + 1) + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        text1 = (TextView) findViewById(R.id.text1);
        text2 = (TextView) findViewById(R.id.text2);
        pager_images = (ViewPager) findViewById(R.id.pager_images);

        // 点击事件设置
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.delete_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentItem < 0 || currentItem > lists.size()) {
                    return;
                }
                lists.remove(currentItem);
                ImageDatabase.selectImages.remove(currentItem);
                if (lists.size() <= 0) {
                    finish();
                    return;
                }
                adapter = new MyAdapter(EditSelectedActivity.this, lists);
                pager_images.setAdapter(adapter);
                currentItem = currentItem - 1 < lists.size() && currentItem - 1 >= 0 ? currentItem - 1 : 0;
                text2.setText(lists.size() + "");
                pager_images.setCurrentItem(currentItem);
                text1.setText((currentItem + 1) + "");
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        text1.setText((currentItem + 1) + "");
    }

    @Override
    public void onPageScrollStateChanged(int state) {
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
            // TODO Auto-generated method stub
            return null == list ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            container.addView(list.get(position));
            return list.get(position);
        }
    }
}
