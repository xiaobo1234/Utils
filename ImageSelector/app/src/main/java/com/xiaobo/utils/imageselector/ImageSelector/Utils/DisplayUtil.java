package com.xiaobo.utils.imageselector.ImageSelector.Utils;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtil {
	
	public static Display getScreenDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }
	
	public static int getWindowWidth(Context context){
		return getScreenDisplay(context).getWidth();
	}
	
	public static int getWindowHeight(Context context){
	    return getScreenDisplay(context).getHeight();
	}

}
