package com.xiaobo.utils.imageselector.ImageSelector.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageBucket;
import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageDatabase;
import com.xiaobo.utils.imageselector.ImageSelector.Activity.TakeImagesActivity;

import java.io.File;
import java.util.List;

/**
 * 图片选择器帮助器
 *
 */
public class ImageSelectorHelper {

    private static AlbumHelper helper;

    /**
     * 初始化
     *
     * @param maxNum 最大选择图片数，此参数须大于 1
     * @param clearCacheList 是否清空已经选择的图片
     */
    public static void initHelper(Context context, int maxNum, boolean clearCacheList) {
        ImageDatabase.maxNumTake = maxNum;// 设置最多图片数
        if (clearCacheList) {
            ImageDatabase.tempSelectImages.clear();
            ImageDatabase.selectImages.clear();
        }

        helper = AlbumHelper.getHelper();
        helper.init(context.getApplicationContext());
    }

    /**
     * 获取相册列表
     *
     * @param context
     */
    public static List<ImageBucket> getImagesBucketList(Context context) {
        if (null == helper) {
            helper = AlbumHelper.getHelper();
            helper.init(context.getApplicationContext());
        }
        return helper.getImagesBucketList(false);
    }

    /**
     * 选择图片
     *
     * @param activity
     */
    public static void selectImages(Activity activity) {
        Intent intent = new Intent(activity, TakeImagesActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 选择图片
     *
     * @param activity
     */
    public static void selectImages(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, TakeImagesActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 剪切图片，宽高比 1:1
     *
     * @param inputPath 源图片文件路径
     * @param outputPath 剪切后，图片保存路径
     */
    public static void cropImage(Activity activity, int requestCode, String inputPath, String outputPath) {
        if (null == inputPath || "".equals(inputPath)) {
            Toast.makeText(activity, "没有图片", Toast.LENGTH_SHORT).show();
            return;
        }
        if (null == outputPath || "".equals(outputPath)) {
            Toast.makeText(activity, "没有图片保存路径", Toast.LENGTH_SHORT).show();
            return;
        }
        File inputFile = new File(inputPath);
        if (inputFile.isDirectory() || !inputFile.exists()) {
            Toast.makeText(activity, "图片文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        Uri inputUri = Uri.fromFile(inputFile);
        intent.setDataAndType(inputUri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);//裁剪框比例
        intent.putExtra("aspectY", 1);
        File outputFile = new File(outputPath);
        Uri outputUri = Uri.fromFile(outputFile);
        intent.putExtra("output", outputUri);  //专入目标文件
        intent.putExtra("outputFormat", "JPEG"); //输入文件格式
        activity.startActivityForResult(intent, requestCode);
    }

}
