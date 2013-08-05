package org.solo.waterfall;

import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

/**
 * 按顺序从左到右
 * 优点：按顺序
 * 缺点：如果某一列的图片高度不够，那么界面会很难看
 * 
 * 用法：继承WaterfallView，复写onCreateAdapterWrapper方法
 * @author hosolo
 *
 */
public class WaterfallSequenceAdapter extends BaseAdapter {

	private static final String TAG = WaterfallSequenceAdapter.class.getSimpleName();
	private static final boolean DEBUG = false;
	private ListAdapter mAdapter;
	private int mColumnCount;
	private int mColumnIndex;

	public WaterfallSequenceAdapter(ListAdapter adapter, int columnCount, int columnIndex) {
		if (adapter == null) {
			throw new IllegalArgumentException("Adapter is null");
		}
		
		if (columnCount <= 0) {
			throw new IllegalArgumentException("Column count must > 0");
		}
		
		if (columnIndex < 0) {
			throw new IllegalArgumentException("Column index must >= 0");
		}
		
		this.mAdapter = adapter;
		this.mColumnCount = columnCount;
		this.mColumnIndex = columnIndex;
	}

	@Override
	public Object getItem(int position) {
		int row = position;
		// 行 * 列数 + 第几列
		position = row * mColumnCount + mColumnIndex;
		return mAdapter.getItem(position);
	}
	
	@Override
	public int getCount() {
		// 总数 / 列数 + 余数
		int count = mAdapter.getCount() / mColumnCount;
		int residue = mAdapter.getCount() % mColumnCount;
		// 有余数，并且多出的刚好是这一列，就行数自动加一
		if (residue > 0 && residue - 1 >= mColumnIndex) {
			count++;
		}
		return count;
	}

	public boolean areAllItemsEnabled() {
		return mAdapter.areAllItemsEnabled();
	}

	public void registerDataSetObserver(DataSetObserver observer) {
		mAdapter.registerDataSetObserver(observer);
	}

	public boolean isEnabled(int position) {
		return mAdapter.isEnabled(position);
	}

	public void unregisterDataSetObserver(DataSetObserver observer) {
		mAdapter.unregisterDataSetObserver(observer);
	}

	public long getItemId(int position) {
		return mAdapter.getItemId(position);
	}

	public boolean hasStableIds() {
		return mAdapter.hasStableIds();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		int row = position;
		position = row * mColumnCount + mColumnIndex;
		if (DEBUG) {
			Log.d(TAG, position + " = " + row + " * " + mColumnCount + " + " + mColumnIndex + " " + toString());
		}
		return mAdapter.getView(position, convertView, parent);
	}

	public int getItemViewType(int position) {
		return mAdapter.getItemViewType(position);
	}

	public int getViewTypeCount() {
		return mAdapter.getViewTypeCount();
	}

	public boolean isEmpty() {
		return mAdapter.isEmpty();
	}
	
	@Override
	public String toString() {
		return TAG + " count: " + getCount()
				+ " Column index: " + mColumnIndex;
	}
}
