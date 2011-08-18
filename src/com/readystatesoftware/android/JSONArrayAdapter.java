package com.readystatesoftware.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

/**
 * An easy adapter to map JSON data to views defined in an XML layout. Basically a wrapper around
 * {@link android.widget.SimpleAdapter} that maps data from a {@link org.json.JSONArray} instance.
 * 
 * @author jgilfelt
 */
public class JSONArrayAdapter extends BaseAdapter implements Filterable {

	private JSONArray data;
	private String idField;
	private SimpleAdapter internalAdapter;

	/**
     * Constructor
     *
     * @param context The context where the View associated with this SimpleAdapter is running
     * 
     * @param data A JSONArray of JSONObjects. Each object in the array corresponds to one row in the list. The
     *        JSONObjects contain the data for each row, and should include all the entries specified in
     *        "from"
     *        
     * @param resource Resource identifier of a view layout that defines the views for this list
     *        item. The layout file should include at least those named views defined in "to"
     *        
     * @param from A list of field names that will be mapped from the JSONObject associated with each
     *        item.
     *        
     * @param to The views that should display column in the "from" parameter. These should all be
     *        TextViews. The first N views in this list are given the values of the first N columns
     *        in the from parameter.
     *        
     * @param idField The field name which contains a numeric identifier for each JSONObject.
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JSONArrayAdapter(Context context,
			JSONArray data,
			int resource, 
			String[] from,
			int[] to,
			String idField) {

		this.data = data;
		this.idField = idField;
		List simpleData = jsonToMapList(data, from);
		internalAdapter = new SimpleAdapter(context, simpleData, resource, from, to);
	}
	
	/**
     * Constructor
     *
     * @param context The context where the View associated with this SimpleAdapter is running
     * 
     * @param data A JSONArray of JSONObjects. Each object in the array corresponds to one row in the list. The
     *        JSONObjects contain the data for each row, and should include all the entries specified in
     *        "from"
     *        
     * @param resource Resource identifier of a view layout that defines the views for this list
     *        item. The layout file should include at least those named views defined in "to"
     *        
     * @param from A list of field names that will be mapped from the JSONObject associated with each
     *        item.
     *        
     * @param to The views that should display column in the "from" parameter. These should all be
     *        TextViews. The first N views in this list are given the values of the first N columns
     *        in the from parameter.
     */
	public JSONArrayAdapter(Context context,
			JSONArray data,
			int resource, 
			String[] from,
			int[] to) {
		this(context, data, resource, from, to, null);
	}
	
	@SuppressWarnings("rawtypes")
	private List jsonToMapList(JSONArray data, String[] fields) {
		
		List<Map> listData = new ArrayList<Map>();
        HashMap<String, String> item;
		
		for (int i = 0; i < data.length(); i++) {
			JSONObject o = data.optJSONObject(i);
			if (o != null) {
				item = new HashMap<String, String>();	
				for (int j = 0; j < fields.length; j++) {
					String fname = fields[j];
					item.put(fname, optString(o, fname));	
				}
				listData.add(item);
			}	
		}
		return listData;
		
	}
	
	private String optString(JSONObject o, String key) {
		String s = o.optString(key);
		if (s == null || s.equals("null")) {
			return null;
		} else {
			return s;
		}	
	}

	public int getCount() {
		return internalAdapter.getCount();
	}

	public Object getItem(int position) {
		return data.optJSONObject(position);
	}

	public long getItemId(int position) {
		if (idField != null) {
			JSONObject o = data.optJSONObject(position);
			if (o != null) {
				return o.optLong(idField, 0);
			}
		}
		return internalAdapter.getItemId(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return internalAdapter.getView(position, convertView, parent);
	}

	/**
	 * Returns the {@link ViewBinder} used to bind data to views.
	 * 
	 * @return a ViewBinder or null if the binder does not exist
	 * 
	 * @see #setViewBinder(android.widget.SimpleAdapter.ViewBinder)
	 */
	public ViewBinder getViewBinder() {
		return internalAdapter.getViewBinder();
	}

	/**
	 * Sets the binder used to bind data to views.
	 * 
	 * @param viewBinder
	 *            the binder used to bind data to views, can be null to remove
	 *            the existing binder
	 * 
	 * @see #getViewBinder()
	 */
	public void setViewBinder(ViewBinder viewBinder) {
		internalAdapter.setViewBinder(viewBinder);
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, String)}
	 * if the supplied data is an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, String)
	 */
	public void setViewImage(ImageView v, int value) {
		internalAdapter.setViewImage(v, value);
	}

	/**
	 * Called by bindView() to set the image for an ImageView but only if there
	 * is no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an ImageView.
	 * 
	 * By default, the value will be treated as an image resource. If the value
	 * cannot be used as an image resource, the value is used as an image Uri.
	 * 
	 * This method is called instead of {@link #setViewImage(ImageView, int)} if
	 * the supplied data is not an int or Integer.
	 * 
	 * @param v
	 *            ImageView to receive an image
	 * @param value
	 *            the value retrieved from the data set
	 * 
	 * @see #setViewImage(ImageView, int)
	 */
	public void setViewImage(ImageView v, String value) {
		internalAdapter.setViewImage(v, value);
	}

	/**
	 * Called by bindView() to set the text for a TextView but only if there is
	 * no existing ViewBinder or if the existing ViewBinder cannot handle
	 * binding to an TextView.
	 * 
	 * @param v
	 *            TextView to receive text
	 * @param text
	 *            the text to be set for the TextView
	 */
	public void setViewText(TextView v, String text) {
		internalAdapter.setViewText(v, text);
	}

	public Filter getFilter() {
		return internalAdapter.getFilter();
	}

}
