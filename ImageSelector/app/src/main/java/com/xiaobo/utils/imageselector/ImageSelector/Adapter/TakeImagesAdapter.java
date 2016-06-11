package com.xiaobo.utils.imageselector.ImageSelector.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageDatabase;
import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageItem;
import com.xiaobo.utils.imageselector.ImageSelector.Utils.DisplayUtil;
import com.xiaobo.utils.imageselector.ImageSelector.Utils.FileUtils;
import com.xiaobo.utils.imageselector.R;

import java.io.File;
import java.util.List;

/**
 * Created by MrPu on 15/11/30.
 */
public class TakeImagesAdapter extends BaseAdapter {
    private List<ImageItem> dataList;
    private boolean isFull = false;
    private Context context;
    private Handler handler;

    public TakeImagesAdapter(Context context, Handler handler, List<ImageItem> dataList) {
        this.context = context;
        this.handler = handler;
        this.dataList = dataList;
    }

    public void setDataList(List<ImageItem> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_item_take_images, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置图片是否选中
        dataList.get(position).setSelected(false);
        if (ImageDatabase.tempSelectImages.size() > 0) {//关联已选中图片的显示
            for (int i = 0; i < ImageDatabase.tempSelectImages.size(); i++) {
                if (dataList.get(position).getImageId().equals(ImageDatabase.tempSelectImages.get(i).getImageId())) {
                    dataList.get(position).setSelected(true);
                    break;
                }
            }
        }

        final ImageItem item = dataList.get(position);
        String path = "";//判断图片是否存在
        if (!TextUtils.isEmpty(item.getImagePath()) && FileUtils.fileIsExists(item.getImagePath())) {
            path = item.getImagePath();
        } else {
            dataList.remove(position);
            notifyDataSetChanged();
            return convertView;
        }

        holder.iv_image.setTag(item.getImagePath());
        int size = DisplayUtil.getWindowWidth(context) / 6;
        Picasso.with(context)
                .load(new File(path))
                .placeholder(R.drawable.image_icon_default)
                .error(R.drawable.image_icon_default)
                .centerCrop()
                .resize(size, size)
                .into(holder.iv_image);

        if (item.isSelected()) {
            holder.iv_selected.setVisibility(View.VISIBLE);
        } else {
            holder.iv_selected.setVisibility(View.INVISIBLE);
        }
        holder.iv_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (ImageDatabase.tempSelectImages.size() < ImageDatabase.maxNumTake) {
                    item.setSelected(!item.isSelected());
                    if (item.isSelected()) {
                        holder.iv_selected.setVisibility(View.VISIBLE);
                        dataList.get(position).setSelected(true);
                        ImageDatabase.tempSelectImages.add(dataList.get(position));
                    } else if (!item.isSelected()) {
                        holder.iv_selected.setVisibility(View.INVISIBLE);
                        dataList.get(position).setSelected(false);
                        removePic(position);
                    }
                    msg.what = 110;
                    handler.dispatchMessage(msg);
                } else if (ImageDatabase.tempSelectImages.size() >= ImageDatabase.maxNumTake) {
                    if (item.isSelected()) {
                        item.setSelected(!item.isSelected());
                        holder.iv_selected.setVisibility(View.INVISIBLE);
                        dataList.get(position).setSelected(false);
                        removePic(position);
                        msg.what = 120;
                        handler.dispatchMessage(msg);
                    } else {
                        if (!isFull) {
                            Toast.makeText(context, "最多选择" + ImageDatabase.maxNumTake + "张图片", Toast.LENGTH_SHORT).show();
                            isFull = true;
                        }
                    }
                }
            }

        });
        return convertView;
    }

    /**
     * 对比移除选中图片
     *
     */
    private void removePic(int position) {
        for (int i = 0; i < ImageDatabase.tempSelectImages.size(); i++) {
            if (dataList.get(position).getImageId().equals(ImageDatabase.tempSelectImages.get(i).getImageId())) {
                ImageDatabase.tempSelectImages.remove(i);
                if (isFull) {
                    isFull = false;
                }
            }
        }
    }

    class ViewHolder {
        private ImageView iv_image;
        private ImageView iv_selected;

        public ViewHolder(View view) {
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            iv_selected = (ImageView) view.findViewById(R.id.iv_selected);
            iv_selected.setImageResource(R.drawable.image_item_choosed);
        }
    }
}
