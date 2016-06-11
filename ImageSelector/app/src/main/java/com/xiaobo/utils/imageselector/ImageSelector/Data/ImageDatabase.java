package com.xiaobo.utils.imageselector.ImageSelector.Data;

import java.util.ArrayList;
import java.util.List;

public class ImageDatabase {

	/**
	 * TakeImagesActivity 专用参数，用于设定选择图片张数
	 */
	public static int maxNumTake = 0;

	/**
	 * 已经选择的图片
	 */
	public static List<ImageItem> selectImages = new ArrayList<ImageItem>();

	/**
	 * 选择的图片的临时列表
	 */
	public static List<ImageItem> tempSelectImages = new ArrayList<ImageItem>();
}
