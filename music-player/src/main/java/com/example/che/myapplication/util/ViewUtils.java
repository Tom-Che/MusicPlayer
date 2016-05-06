package com.example.che.myapplication.util;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewUtils {
	public static void initListView(Context paramContext, ListView paramListView, String paramString, int paramInt1, int paramInt2) {
		String[] arrayOfString = new String[paramInt1];
		for (int i = 0;; ++i) {
			if (i >= arrayOfString.length) {
				paramListView.setAdapter(new ArrayAdapter<String>(paramContext, paramInt2, arrayOfString));
				paramListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
						Context localContext = paramView.getContext();
						String str = "item[" + paramInt + "]=" + paramAdapterView.getItemAtPosition(paramInt);
						Toast.makeText(localContext, str, Toast.LENGTH_SHORT).show();
						System.out.println(str);
					}
				});
				return;
			}
			arrayOfString[i] = (paramString + (i + 1));
		}
	}

	public static void printView(String paramString, View paramView) {
		System.out.println(paramString + "=" + paramView);
		if (paramView == null)
			return;
		System.out.print("[" + paramView.getLeft());
		System.out.print(", " + paramView.getTop());
		System.out.print(", w=" + paramView.getWidth());
		System.out.println(", h=" + paramView.getHeight() + "]");
		System.out.println("mw=" + paramView.getMeasuredWidth() + ", mh=" + paramView.getMeasuredHeight());
		System.out.println("scroll [" + paramView.getScrollX() + "," + paramView.getScrollY() + "]");
	}

	public static void setViewWidths(View paramView, View[] paramArrayOfView) {
		int i = paramView.getWidth();
		int j = paramView.getHeight();
		for (int k = 0;; ++k) {
			if (k >= paramArrayOfView.length)
				return;
			View localView = paramArrayOfView[k];
			localView.layout(i * (k + 1), 0, i * (k + 2), j);
			printView("view[" + k + "]", localView);
		}
	}

	/**
	 * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp
	 */
	public static float scale = -1;

	public static float px2dipf(Context context, float pxValue) {
		if (scale == -1) {
			scale = context.getResources().getDisplayMetrics().density;
		}
		return px2dipf(scale, pxValue);
	}

	/**
	 * 根据手机的分辨率�? dp 的单�? 转成�? px(像素)
	 */
	public static float dip2pxf(Context context, float dpValue) {
		if (scale == -1) {
			scale = context.getResources().getDisplayMetrics().density;
		}
		return dip2pxf(scale, dpValue);
	}

	public static float px2dipf(float scale, float pxValue) {
		return (pxValue / scale + 0.5f);
	}

	public static float dip2pxf(float scale, float dpValue) {
		return (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率�? px(像素) 的单�? 转成�? dp
	 */
	public static int px2dipi(Context context, float pxValue) {
		if (scale == -1) {
			scale = context.getResources().getDisplayMetrics().density;
		}
		return px2dipi(scale, pxValue);
	}

	/**
	 * 根据手机的分辨率�? dp 的单�? 转成�? px(像素)
	 */
	public static int dip2pxi(Context context, float dpValue) {
		if (scale == -1) {
			scale = context.getResources().getDisplayMetrics().density;
		}
		return dip2pxi(scale, dpValue);
	}

	public static int px2dipi(float scale, float pxValue) {
		return (int) (pxValue / scale + 0.5f);
	}

	public static int dip2pxi(float scale, float dpValue) {
		return (int) (dpValue * scale + 0.5f);
	}

    public static int getTypedValueResourceId(int resid,Context context){
        TypedValue tv=new TypedValue();
        context.getTheme().resolveAttribute(resid, tv, true);
        return tv.resourceId;
    }
}
