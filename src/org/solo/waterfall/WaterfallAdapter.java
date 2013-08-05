package org.solo.waterfall;


public interface WaterfallAdapter<T> {

	public void addIndexItem(IndexItem<T> object);
	
	public IndexItem<T> getIndexItem(int position);
	
}
