package com.example.che.myapplication.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.example.che.myapplication.util.MLogUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Che on 2016/04/15.
 */
public class ApplicationEx extends Application {

    public static class ClientConstant {
        //图片文件缓存
        public static String FILE_TEMP;
        //数据库路径
        public static String DATABASENAME;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //应用超过65K限制
        MultiDex.install(this);
    }

    //所有的activity列表
    private List<Activity> activities;

    public List<Activity> getActivities() {
        return activities;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        /****初始化start****/


        // 初始化activity列表
        activities = new CopyOnWriteArrayList<>();

        ClientConstant.FILE_TEMP = getExternalCacheDir().getPath() + "/imageTemp/";

        initImageLoader();

        /****初始化end****/
    }


    /**
     * 图片管理器加载
     */
    private void initImageLoader() {
        MLogUtil.e("图片加载器初始化");
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.EXACTLY)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.ARGB_8888)// 设置图片的解码类型
                .build(); // 创建配置过得DisplayImageOption对象
        File cacheDir = new File(ApplicationEx.ClientConstant.FILE_TEMP);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).threadPoolSize(3)// 线程池内加载的数目
                .threadPriority(Thread.NORM_PRIORITY - 2).diskCache(new UnlimitedDiskCache(cacheDir)) // 自定义缓存路径
                .memoryCache(new WeakMemoryCache()).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时�?的URI名称用MD5
                .tasksProcessingOrder(QueueProcessingType.LIFO).defaultDisplayImageOptions(options).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    public void exit() {
        for (Activity activity : activities) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
        System.exit(0);
    }
}
