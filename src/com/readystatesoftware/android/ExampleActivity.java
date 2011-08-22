package com.readystatesoftware.android;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

/**
 * @author jgilfelt
 */
public class ExampleActivity extends ListActivity implements OnItemClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new SearchTwitterTask().execute("#AndroidDev");
	}

	class SearchTwitterTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... arg0) {
			String url = "http://search.twitter.com/search.json?q=" + URLEncoder.encode(arg0[0]);
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet(url);
				HttpResponse response = client.execute(request);
				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				String json = "";
				while ((line = rd.readLine()) != null) {
					json += line;
				}
				rd.close();
				return json;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			try {
				showData(result);
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(ExampleActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
			}	
		}

	}

	private void showData(String json) throws JSONException {
		JSONObject o = new JSONObject(json);
		JSONArray data = o.getJSONArray("results");

		ListAdapter adapter = new JSONArrayAdapter(this,
				data,													// JSONArray data							
				android.R.layout.simple_list_item_2, 					// a layout resource to display a row
				new String[] {"from_user", "text"},						// field names from JSONObjects
				new int[] {android.R.id.text1, android.R.id.text2},		// corresponding View ids to map field names to 
				"id"													// id field from JSONObjects (optional)
		);

		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Toast.makeText(ExampleActivity.this, "clicked tweet id " + id, Toast.LENGTH_SHORT).show();
	}

}