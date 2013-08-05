package org.solo.waterfall;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class AdaptationListView extends ListView {

	public AdaptationListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AdaptationListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AdaptationListView(Context context) {
		this(context, null, 0);
	}
	
	private void init() {
		// do anything
	}

	@Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
	
}
