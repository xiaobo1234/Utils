package com.xiaobo.utils.imageselector.ImageSelector.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageBucket;
import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageDatabase;
import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageItem;
import com.xiaobo.utils.imageselector.ImageSelector.Dialog.ShowBigImageDialog;
import com.xiaobo.utils.imageselector.ImageSelector.Adapter.TakeImagesAdapter;
import com.xiaobo.utils.imageselector.ImageSelector.Utils.ImageSelectorHelper;
import com.xiaobo.utils.imageselector.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取指定数量的图片
 */
public class TakeImagesActivity extends Activity implements View.OnClickListener, Serializable {
    private TextView picture;
    private TextView title;
    private TextView cancel;
    private TextView look;
    private TextView certain;
    private GridView pic_grid;

    public static List<ImageBucket> contentList = null;
    private List<ImageItem> dataList;
    private TakeImagesAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (ImageDatabase.maxNumTake == 1) {
                certain.setText("确定");
            }else {
                switch (msg.what) {
                    case 110:// 未选满图片时，响应刷新
                        certain.setText("确定" + "(" + ImageDatabase.tempSelectImages.size() + "/" + ImageDatabase.maxNumTake + ")");
                        break;
                    case 120:// 选满图片时，减少图片，响应刷新
                        certain.setText("确定" + "(" + ImageDatabase.tempSelectImages.size() + "/" + ImageDatabase.maxNumTake + ")");
                        break;
                    default:
                        break;
                }
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity_take_images);

        if (ImageDatabase.maxNumTake <= 0) {
            finish();
            return;
        }

        contentList = ImageSelectorHelper.getImagesBucketList(this);
        dataList = new ArrayList<ImageItem>();
        for (int i = 0; i < contentList.size(); i++) {
            dataList.addAll(contentList.get(i).imageList);
        }
        ImageDatabase.tempSelectImages.clear();
        ImageDatabase.tempSelectImages.addAll(ImageDatabase.selectImages);

        ImageBucket bucket = new ImageBucket();
        bucket.bucketName = "所有图片";
        bucket.imageList = dataList;
        bucket.count = bucket.imageList.size();
        contentList.add(0, bucket);

        inView();
        if (ImageDatabase.maxNumTake == 1) {
            certain.setText("确定");
        }else {
            certain.setText("确定" + "(" + ImageDatabase.tempSelectImages.size() + "/" + ImageDatabase.maxNumTake + ")");
        }

        setView();
    }

    private void inView() {
        picture = (TextView) findViewById(R.id.picture);
        title = (TextView) findViewById(R.id.title);
        cancel = (TextView) findViewById(R.id.cancel);
        look = (TextView) findViewById(R.id.look);
        certain = (TextView) findViewById(R.id.certain);
        pic_grid = (GridView) findViewById(R.id.pic_grid);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 66) {
            if (CatalogueActivity.dataList != null) {
                dataList = CatalogueActivity.dataList;
                title.setText(CatalogueActivity.name);
                adapter.setDataList(dataList);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageDatabase.tempSelectImages.clear();//清空临时选择图片列表
    }

    private void setView() {
        picture.setOnClickListener(this);
        cancel.setOnClickListener(this);
        look.setOnClickListener(this);
        certain.setOnClickListener(this);

        adapter = new TakeImagesAdapter(this, handler, dataList);
        pic_grid.setAdapter(adapter);
        pic_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.picture:
                intent = new Intent(this, CatalogueActivity.class);
                startActivityForResult(intent, 66);
                break;
            case R.id.cancel:
                finish();
                break;
            case R.id.look:
                if (ImageDatabase.tempSelectImages.size() > 0) {
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < ImageDatabase.tempSelectImages.size(); i++) {
                        list.add(ImageDatabase.tempSelectImages.get(i).getImagePath());
                    }
                    ShowBigImageDialog dialog = new ShowBigImageDialog();
                    dialog.setOnView(this, list);
                    dialog.show();
                } else {
                    Toast.makeText(this, "请选择图片!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.certain:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImageDatabase.selectImages.clear();
                        if (ImageDatabase.tempSelectImages.size() > 0) {
                            ImageDatabase.selectImages.addAll(ImageDatabase.tempSelectImages);
                            ImageDatabase.tempSelectImages.clear();
                        }
                    }
                }).start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setResult(RESULT_OK);
                        finish();
                    }
                }, 100);
                break;
            default:
                break;
        }
    }
}
