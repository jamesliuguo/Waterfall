package org.solo.waterfall;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class MainActivity extends Activity implements OnItemClickListener {

	private PhotoAdapter mAdapter;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private WaterfallSmartView mWaterfall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mAdapter = new PhotoAdapter(this);
		mWaterfall = (WaterfallSmartView) findViewById(R.id.waterfall);
		mWaterfall.setAdapter(mAdapter);
		mWaterfall.setOnItemClickListener(this);
		
		mImageLoader = ImageLoaderCompat.getInstance(this);
		mOptions = ImageLoaderCompat.getOptions();

		loadUrl(urls);
	}
	
	private Handler mHandler = new Handler();
	
	private void loadUrl(String[] urls) {
		for (final String url : toList(urls)) {
			mImageLoader.loadImage(url, mImageLoadingListener);
		}
	}
	
	// Show slowly how it work
	private void loadUrlSlow(String[] urls) {
		long time = 0L;
		for (final String url : toList(urls)) {
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					mImageLoader.loadImage(url, mImageLoadingListener);
				}
			}, time);
			time += 1000L;
		}
	}
	
	ImageLoadingListener mImageLoadingListener = new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String imageUri, View view) {
		}
		
		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
		}
		
		@Override
		public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
			mAdapter.add(imageUri, loadedImage.getWidth(), loadedImage.getHeight());
		}
		
		@Override
		public void onLoadingCancelled(String imageUri, View view) {
		}
	};
	
	private List<String> toList(String[] strings) {
		List<String> list = new ArrayList<String>(strings.length);
		for (String s : strings) {
			list.add(s);
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_settings) {
			loadUrl(urls2);
			return true;
		}
		return false;
	}
	
	class PhotoAdapter extends ArrayAdapter<String> {
		private LayoutInflater inflater;

		public PhotoAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
			inflater = LayoutInflater.from(context);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.photo_item, parent, false);
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			mImageLoader.displayImage((String) getItem(position), holder.imageView, mOptions);
			return convertView;
		}
		
		public void add(String object, int weight, int height) {
			super.add(object);
			// AddItem to waterfall in same time
			mWaterfall.addItem(object, weight, height);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(this, position + ":" + mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
	}
	
	class ViewHolder {
		ImageView imageView;
	}
	
	private static String urls[] = { 
		"http://farm7.staticflickr.com/6101/6853156632_6374976d38_c.jpg",
		"http://farm8.staticflickr.com/7232/6913504132_a0fce67a0e_c.jpg",
		"http://farm5.staticflickr.com/4133/5096108108_df62764fcc_b.jpg",
		"http://farm5.staticflickr.com/4074/4789681330_2e30dfcacb_b.jpg",
		"http://farm9.staticflickr.com/8208/8219397252_a04e2184b2.jpg",
		"http://farm9.staticflickr.com/8483/8218023445_02037c8fda.jpg",
		"http://farm9.staticflickr.com/8335/8144074340_38a4c622ab.jpg",
		"http://farm9.staticflickr.com/8060/8173387478_a117990661.jpg",
		"http://farm9.staticflickr.com/8056/8144042175_28c3564cd3.jpg",
		"http://farm9.staticflickr.com/8183/8088373701_c9281fc202.jpg",
		"http://farm9.staticflickr.com/8185/8081514424_270630b7a5.jpg",
		"http://farm9.staticflickr.com/8462/8005636463_0cb4ea6be2.jpg",
		"http://farm9.staticflickr.com/8306/7987149886_6535bf7055.jpg"
	};
	
	private static String urls2[] = {
		"http://farm9.staticflickr.com/8444/7947923460_18ffdce3a5.jpg",
		"http://farm9.staticflickr.com/8182/7941954368_3c88ba4a28.jpg",
		"http://farm9.staticflickr.com/8304/7832284992_244762c43d.jpg",
		"http://farm9.staticflickr.com/8163/7709112696_3c7149a90a.jpg",
		"http://farm8.staticflickr.com/7127/7675112872_e92b1dbe35.jpg",
		"http://farm8.staticflickr.com/7111/7429651528_a23ebb0b8c.jpg",
		"http://farm9.staticflickr.com/8288/7525381378_aa2917fa0e.jpg",
		"http://farm6.staticflickr.com/5336/7384863678_5ef87814fe.jpg",
		"http://farm8.staticflickr.com/7102/7179457127_36e1cbaab7.jpg",
		"http://farm8.staticflickr.com/7086/7238812536_1334d78c05.jpg",
		"http://farm8.staticflickr.com/7243/7193236466_33a37765a4.jpg",
		"http://farm8.staticflickr.com/7251/7059629417_e0e96a4c46.jpg",
		"http://farm8.staticflickr.com/7084/6885444694_6272874cfc.jpg"
	};

}
