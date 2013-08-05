package org.solo.waterfall;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * TODO
 * Issue:
 * 1.Custom Adapter getView is called too much times; 
 * 
 * XXX
 * Note:
 * 1.It's use ListView's setTag method
 * 
 * @author solo, email:kjsoloho@gmail.com
 * @version 0.5
 */
public abstract class WaterfallView extends ScrollView {

	private static final String TAG = WaterfallView.class.getSimpleName();
	private static final boolean DEBUG = false;
	
	private static final int DEFAULT_COLUMN = 2;
	
	private int mColumnCount = DEFAULT_COLUMN;
	private int mColumnMargin = 0;
	private OnItemClickListener mOnItemClickListener;
	private OnItemLongClickListener mOnItemLongClickListener;
	
	private List<WaterfallView.WaterfallColumn> mColumns;
	private BaseAdapter mAdapter;
	
	public WaterfallView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (attrs != null) {
			handleAttr(context, attrs);
		}
		init();
	}
	
	public WaterfallView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WaterfallView(Context context) {
		this(context, null, 0);
	}

	private void handleAttr(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaterfallView);
		
		mColumnCount = a.getInt(R.styleable.WaterfallView_column, DEFAULT_COLUMN);
		mColumnMargin = a.getDimensionPixelSize(R.styleable.WaterfallView_itemMargin, 0);
		
		a.recycle();
	}

	private void init() {
		mColumns = new ArrayList<WaterfallView.WaterfallColumn>(mColumnCount);
	}
	
	/** Set Column Count */
	public void setColumn(Context context, int columnCount) {
		if (columnCount <= 0) {
			throw new IllegalArgumentException("Column must > 0" + columnCount);
		}
		this.mColumnCount = columnCount;
		
		BaseAdapter adapter = getAdapter();
		if (adapter != null) {
			setAdapter(adapter);
		}
	}
	
	/** Get WaterfallView adapter */
	public BaseAdapter getAdapter() {
		return mAdapter;
	}

	/** Set WaterfallView adapter */
	public void setAdapter(BaseAdapter adapter) {
		initListView();
		// 给每个ListView添加适配器
		ViewGroup layout = getFisrtLayout();
		for (int i = 0; i < layout.getChildCount(); i++) {
			View lv = layout.getChildAt(i);
			// 防止调用者在布局里面添加其他View导致强转失败
			if (lv instanceof ListView) {
				int index = (Integer) lv.getTag();
				((ListView) lv).setAdapter(onCreateAdapterWrapper(adapter, mColumnCount, index));
				((ListView) lv).setOnItemClickListener(innerOnItemClickListener);
				((ListView) lv).setOnItemLongClickListener(innerOnItemLongClickListener);
			}
		}
		mAdapter = adapter;
	}
	
	private void initListView() {
		ViewGroup layout = getFisrtLayout();
		if (layout.getChildCount() > 0) {
			layout.removeAllViews();
		}
		for (int i = 0; i < mColumnCount; i++) {
			ListView lv = createListView();
			lv.setTag(i);
			//lv.setOnHierarchyChangeListener(innerOnHierarachyChangeListener);
			ViewGroup.MarginLayoutParams lp;
			// 建议使用LinearLayout
			if (layout instanceof LinearLayout) {
				if (((LinearLayout) layout).getOrientation() != LinearLayout.HORIZONTAL) {
					((LinearLayout) layout).setOrientation(LinearLayout.HORIZONTAL);
				}
				LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, 
						ViewGroup.LayoutParams.WRAP_CONTENT);
				llp.weight = 1;
				if (i != mColumnCount - 1) {
					llp.rightMargin = mColumnMargin;
				}
				lp = llp; 
			} else {
				Log.w(TAG, "Use LinearLayout is better");
				lp = new ViewGroup.MarginLayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT, 
						ViewGroup.LayoutParams.WRAP_CONTENT);
			}
			layout.addView(lv, i, lp);
			
			// 封装
			WaterfallColumn item = new WaterfallColumn();
			item.lv = (ListView) lv;
			item.columnIndex = i;
			mColumns.add(item);
		}
	}
	
	/** Create ListView, used to waterfall's column */
	protected ListView createListView() {
		// We use AdjustListView to fix it's height in ScrollView
		return new AdaptationListView(getContext());
	}

	private ViewGroup getFisrtLayout() {
		if (getChildCount() > 1) {
			throw new IllegalStateException(TAG + " can host only one direct child");
		}
		ViewGroup layout = (ViewGroup) getChildAt(0);
		if (layout == null) {
			throw new IllegalStateException(TAG + " has no child");
		}
		return layout;
	}
	
	public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
        ViewGroup layout = getFisrtLayout();
        if (layout.getChildCount() <= 0) {
        	return;
        }
		for (int i = 0; i < layout.getChildCount(); i++) {
			View lv = layout.getChildAt(i);
			// 防止调用者在布局里面添加其他View导致强转失败
			if (lv instanceof ListView) {
				((ListView) lv).setOnItemClickListener(innerOnItemClickListener);
			}
		}
    }
	
	public void setOnItemClickListener(OnItemLongClickListener listener) {
		mOnItemLongClickListener = listener;
        ViewGroup layout = getFisrtLayout();
        if (layout.getChildCount() <= 0) {
        	return;
        }
		for (int i = 0; i < layout.getChildCount(); i++) {
			View lv = layout.getChildAt(i);
			// 防止调用者在布局里面添加其他View导致强转失败
			if (lv instanceof ListView) {
				((ListView) lv).setOnItemLongClickListener(innerOnItemLongClickListener);
			}
		}
    }
	
	private OnItemClickListener innerOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// We want the correct position
			position = WaterfallView.this.getItemPosition(parent, view, position, id);
			mOnItemClickListener.onItemClick(parent, view, position, id);
		}
		
	};
	
	private OnItemLongClickListener innerOnItemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			position = WaterfallView.this.getItemPosition(parent, view, position, id);
			return mOnItemLongClickListener.onItemLongClick(parent, view, position, id);
		}
	};
	
	/** Create adapter used to wrapper the custom adapter */
	abstract protected BaseAdapter onCreateAdapterWrapper(BaseAdapter adapter, int columnCount, int columnIndex);
	
	/** Get the item correct position */
	abstract protected int getItemPosition(AdapterView<?> parent, View view, int position, long id);
	
	// XXX 获取List高度有延迟问题，所以这个解决方案暂不使用
	@Deprecated
	private OnHierarchyChangeListener innerOnHierarachyChangeListener = new OnHierarchyChangeListener() {
		
		@Override
		public void onChildViewRemoved(View parent, View child) {
			//int columnIndex = (Integer) parent.getTag();
			//mColumns.get(columnIndex).height = parent.getHeight();
		}
		
		@Override
		public void onChildViewAdded(View parent, View child) {
			//int columnIndex = (Integer) parent.getTag();
			//mColumns.get(columnIndex).height = parent.getHeight();
			//Log.d(TAG, "添加View columnIndex: " + columnIndex + " height: " + parent.getHeight());
		}
	};
	
	public WaterfallColumn getShorterColumn() {
		WaterfallColumn shorter = null;
		for (WaterfallColumn w : mColumns) {
			// 最小原则，就近原则
			if (DEBUG) {
				if (shorter != null) {
					Log.w(TAG, "col: " + shorter.columnIndex + " height: " + shorter.height 
							+ " col: " + w.columnIndex + " height: " + w.height);
				}
			}
			
			if (shorter == null) {
				shorter = w;
			}
			if (w.height < shorter.height) {
				shorter = w;
			}
		}
		return shorter;
	}
	
	/** Get column margin*/
	public int getColumnMargin() {
		return mColumnMargin;
	}
	
	/** Get column count */
	public int getColumnCount() {
		return mColumnCount;
	}
	
	/** Get column */
	public List<WaterfallView.WaterfallColumn> getColumns() {
		return mColumns;
	}
	
	class WaterfallColumn {
		ListView lv;
		int columnIndex;
		int height;
	}
}
