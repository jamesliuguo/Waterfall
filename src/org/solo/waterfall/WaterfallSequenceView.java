package org.solo.waterfall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

public class WaterfallSequenceView extends WaterfallView {
	
	public WaterfallSequenceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public WaterfallSequenceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WaterfallSequenceView(Context context) {
		super(context);
	}

	@Override
	protected BaseAdapter onCreateAdapterWrapper(BaseAdapter adapter, int columnCount, int columnIndex) {
		return new WaterfallSequenceAdapter(adapter, columnCount, columnIndex);
	}

	@Override
	protected int getItemPosition(AdapterView<?> parent, View view, int position, long id) {
		int row = position;
		int columnIndex = (Integer) parent.getTag();
		position = row * getColumnCount() + columnIndex;
		return position;
	}

}
