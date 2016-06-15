package com.xiaobo.utils.imageselector;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.xiaobo.utils.imageselector.ImageSelector.Data.ImageDatabase;
import com.xiaobo.utils.imageselector.ImageSelector.Utils.ImageSelectorHelper;
import com.xiaobo.utils.imageselector.ImageSelector.Dialog.EnterDialog;

import java.io.File;


public class TestMainActivity extends Activity {

    private static final int SELECT_IMG_REQUEST_CODE = 10;
    private static final int CAMERA_REQUEST_CODE = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_IMG_REQUEST_CODE:
                    Toast.makeText(this, "list_length=" + ImageDatabase.selectImages.size(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public void goTestOne(View view) {
        showDialog();
    }

    public void goTestMore(View view) {
        showDialogMore();
    }

    /**
     * 选择图片Dialog
     *
     */
    private void showDialog() {
        EnterDialog dialog = new EnterDialog(this) {

            @Override
            public void doGoToImg() {
                ImageSelectorHelper.initHelper(TestMainActivity.this, 1, true);
                ImageSelectorHelper.selectImages(TestMainActivity.this, SELECT_IMG_REQUEST_CODE);
            }

            @Override
            public void doGoToPhone() {
                Toast.makeText(TestMainActivity.this, "打开相机", Toast.LENGTH_SHORT).show();
//                openCamera();
            }
        };
        dialog.show();
    }

    /**
     * 选择图片Dialog
     *
     */
    private void showDialogMore() {
        EnterDialog dialog = new EnterDialog(this) {

            @Override
            public void doGoToImg() {
                ImageSelectorHelper.initHelper(TestMainActivity.this, 9, true);
                ImageSelectorHelper.selectImages(TestMainActivity.this, SELECT_IMG_REQUEST_CODE);
            }

            @Override
            public void doGoToPhone() {
                Toast.makeText(TestMainActivity.this, "打开相机", Toast.LENGTH_SHORT).show();
//                openCamera();
            }
        };
        dialog.show();
    }

    /**
     * 打开相机
     *
     */
    private void openCamera() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File("");
                Uri u = Uri.fromFile(f);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "相机打开失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "手机未安装SD卡", Toast.LENGTH_SHORT).show();
        }
    }

}
