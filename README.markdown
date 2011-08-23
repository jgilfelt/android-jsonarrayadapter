# JSONArrayAdapter

An easy adapter to map JSON data to views defined in an XML layout. 

Basically a drop-in replacement for and wrapper around [android.widget.SimpleAdapter](http://developer.android.com/reference/android/widget/SimpleAdapter.html) that maps data from a [org.json.JSONArray](http://developer.android.com/reference/org/json/JSONArray.html) instance.

**Usage:**

    ListAdapter adapter = new JSONArrayAdapter(this,
  	                data,													// JSONArray data							
		    		android.R.layout.simple_list_item_2, 					// a layout resource to display a row
		    		new String[] {"from_user", "text"},						// field names from JSONObjects
		    		new int[] {android.R.id.text1, android.R.id.text2},		// corresponding View ids to map field names to 
		    		"id"													// id field from JSONObjects (optional)
    );
    
    getListView().setAdapter(adapter);

The code in this project is licensed under the Apache Software License 2.0. 

Copyright (c) 2011 readyState Software Ltd.