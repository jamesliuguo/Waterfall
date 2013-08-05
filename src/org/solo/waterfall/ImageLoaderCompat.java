package org.solo.waterfall;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderCompat {
	
	private static ImageLoader sInstance;
	
	public static ImageLoader getInstance(Context context) {
		if (sInstance == null) {
			File cacheDir = getOwnCacheDirectory(context.getApplicationContext(), "image");
			sInstance = ImageLoader.getInstance();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context.getApplicationContext())
			            .memoryCacheExtraOptions(480, 800) // max width, max height
			            .denyCacheImageMultipleSizesInMemory()
			            .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation
			            .discCache(new UnlimitedDiscCache(cacheDir)) // You can pass your own disc cache implementation
			            .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
			            .build();
			sInstance.init(config);
		}
		return sInstance;
	}
	
	public static File getOwnCacheDirectory(Context context, String name) {
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			appCacheDir = new File(context.getExternalCacheDir(), name);
		}
		if(appCacheDir == null || (!appCacheDir.exists() && !appCacheDir.mkdirs())){
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}
	
	public static DisplayImageOptions getOptions(){
		return new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.image_holder_color)
			.showImageForEmptyUri(R.drawable.image_holder_color)
			.cacheInMemory()
			.cacheOnDisc()
			.build();
	}
}
