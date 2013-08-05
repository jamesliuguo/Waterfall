package org.solo.waterfall;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

// 
/**
 * TODO How to fix the Generics, but I don't want it look like WaterfallSmartView<T>
 * 
 * @author solo, email: kjsoloho@gmail.com
 *
 */
public class WaterfallSmartView extends WaterfallView {

	private static final boolean DEBUG = false;
	private static final String TAG = WaterfallSmartView.class.getSimpleName();

	private int fakeListViewWeight;
	
	public WaterfallSmartView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public WaterfallSmartView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WaterfallSmartView(Context context) {
		this(context, null, 0);
	}
	
	private void init() {
		int padding = getPaddingLeft() + getPaddingRight();
		int margin = getColumnMargin() * getColumnCount() - 1;
		int availableWidth = getResources().getDisplayMetrics().widthPixels - margin - padding;
		fakeListViewWeight = availableWidth / getColumnCount();
	}

	// Use with WaterfallColumnAdapter
	public void addItem(Object object, int weight, int height) {
		height = height * fakeListViewWeight / weight;
		
		WaterfallColumn shorter = getShorterColumn();
		WaterfallSmartAdapter adapter = (WaterfallSmartAdapter) shorter.lv.getAdapter();
		IndexItem item = new IndexItem();
		item.index = getAdapter().getCount() - 1;
		item.object = object;
		adapter.addIndexItem(item);
		if (DEBUG) {
			Log.d(TAG, "Shoter column: " + shorter.columnIndex + " height: " 
					+ shorter.height + " 原index: " + item.index);
		}
		
		shorter.height += height;
	}

	@Override
	protected BaseAdapter onCreateAdapterWrapper(BaseAdapter adapter, int columnCount, int columnIndex) {
		return new WaterfallSmartAdapter(adapter);
	}

	@Override
	protected int getItemPosition(AdapterView<?> parent, View view, int position, long id) {
		int columnIndex = (Integer) parent.getTag();
		position = ((WaterfallSmartAdapter) getColumns().get(columnIndex).lv.getAdapter())
				.getIndexItem(position).index;
		return position;
	}
}
