package com.xiaobo.utils.imageselector.ImageSelector.Utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {

	/**
	 * 检查文件是否存在
	 *
	 */
	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists())
				return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查存储卡是否插入
	 * @return
	 */
	public static boolean hasSdcard() {
	    String status = Environment.getExternalStorageState();
	    if (status.equals(Environment.MEDIA_MOUNTED)){
	       return true;
	    } else {
	       return false;
	    }
	}

	/**
	 * 获取文件大小
	 * 
	 * @param file 源文件
	 * 
	 * @return 文件大小，size/1024 为 KB。文件不存在或是文件夹，返回 0
	 */
	public static int getFileSize(File file) {
		if (!file.exists() || !file.isFile()) {
			return 0;
		}
		int size = 0;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			size = fis.available();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return size;
	}

}
