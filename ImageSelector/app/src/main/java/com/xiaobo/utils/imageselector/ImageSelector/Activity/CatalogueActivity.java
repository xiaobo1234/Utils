package com.xiaobo.utils.imageselector.ImageSelector.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageBucket;
import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageItem;
import com.xiaobo.utils.imageselector.ImageSelector.Utils.DisplayUtil;
import com.xiaobo.utils.imageselector.ImageSelector.Utils.ImageSelectorHelper;
import com.xiaobo.utils.imageselector.R;

import java.io.File;
import java.util.List;

public class CatalogueActivity extends Activity {

    private TextView cancel;
    private ListView cata_lv;

    private List<ImageBucket> contentList;
    public static List<ImageItem> dataList = null;
    public static String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity_catalogue);

        contentList = ImageSelectorHelper.getImagesBucketList(this);

        intView();
        setView();
    }

    private void intView() {
        cancel = (TextView) findViewById(R.id.cancel);
        cata_lv = (ListView) findViewById(R.id.lv_catalogue);

        cata_lv.setAdapter(new MyAdapter());
    }

    private void setView() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cata_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dataList = contentList.get(position).imageList;
                name = contentList.get(position).bucketName;
                Intent intent = new Intent();
                intent.putExtra("flag" ,true);
                setResult(66 ,intent);
                finish();
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contentList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(CatalogueActivity.this).inflate(R.layout.image_item_catalogue_img,null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }else {
                holder = (ViewHolder) view.getTag();
            }

            int size = DisplayUtil.getWindowWidth(CatalogueActivity.this) / 6;
            Picasso.with(CatalogueActivity.this)
                    .load(new File(contentList.get(position).imageList.get(0).getImagePath()))
                    .placeholder(R.drawable.image_icon_default)
                    .error(R.drawable.image_icon_default)
                    .resize(size, size)
                    .centerCrop()
                    .into(holder.img);
            holder.tv.setText(contentList.get(position).bucketName + "(" + contentList.get(position).count + ")");

            return view;
        }

        class ViewHolder {

            ImageView img;
            TextView tv;

            public ViewHolder(View view) {
                img = (ImageView) view.findViewById(R.id.item_img);
                tv = (TextView) view.findViewById(R.id.item_tv);
            }
        }
    }
}
