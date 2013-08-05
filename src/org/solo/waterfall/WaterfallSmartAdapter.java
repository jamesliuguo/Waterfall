package org.solo.waterfall;

import java.util.ArrayList;
import java.util.List;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 配合WaterfallView的addItem
 * 使Waterfall的item变得错落有致
 * 
 * 有点：界面好看
 * 缺点：只能一个个的添加，界面的item排序是乱序，但点击事件不是乱序
 * @author hosolo
 * 
 */
public class WaterfallSmartAdapter<T> extends BaseAdapter {

	private BaseAdapter mAdapter;
	private List<IndexItem<T>> mObjects = new ArrayList<IndexItem<T>>();
	private final Object mLock = new Object();

	public WaterfallSmartAdapter(BaseAdapter adapter) {
		mAdapter = adapter;
	}
	
	public void addIndexItem(IndexItem<T> object) {
		synchronized (mLock) {
			mObjects.add(object);
        }
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mObjects == null ? 0 : mObjects.size();
	}

	@Override
	public T getItem(int position) {
		return mObjects.get(position).object;
	}
	
	public IndexItem<T> getIndexItem(int position) {
		return mObjects.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		position = mObjects.get(position).index;
		return mAdapter.getView(position, convertView, parent);
	}
	
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		mAdapter.registerDataSetObserver(observer);
	}
	
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		mAdapter.unregisterDataSetObserver(observer);
	}

	public void notifyDataSetChanged() {
		mAdapter.notifyDataSetChanged();
	}

	public void notifyDataSetInvalidated() {
		mAdapter.notifyDataSetInvalidated();
	}

	public boolean isEnabled(int position) {
		return mAdapter.isEnabled(position);
	}

	public boolean isEmpty() {
		return mAdapter.isEmpty();
	}

	@Override
	public long getItemId(int position) {
		return mObjects.get(position).index;
	}

}
