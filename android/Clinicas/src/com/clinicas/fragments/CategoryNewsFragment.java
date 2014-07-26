package com.clinicas.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.clinicas.ArticleActivity;
import com.clinicas.ArticlesAdapter;
import com.clinicas.R;

public class CategoryNewsFragment extends Fragment {

	ArticlesAdapter adapter;
	static final String URL = "http://clinicas.engr.scu.edu/index.php/clinicas_api/articles/filter/";
    // XML node keys
    static String articleType ="";
     public static final String KEY_ID = "articleId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_TEXT = "content";
    public static final String KEY_DATE = "date";
    public static final String KEY_CAT = "categoryId"; 
    public static final String KEY_PIC = "pictureUrl";
	
    ArrayList<HashMap<String, String>> articleList = new ArrayList<HashMap<String,String>>();
    ListView articleListView;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.latest_news_fragment, container,false);
		articleList = new ArrayList<HashMap<String,String>>();
		adapter = new ArticlesAdapter(getActivity(), articleList);
		
		articleListView = (ListView) rootView.findViewById(R.id.articles_list);
		articleListView.setAdapter(adapter);
		ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		Bundle args = getArguments();
		String type = args.getString("type");
		articleType = type;
		articleListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				
				Intent i = new Intent(getActivity().getApplicationContext(), ArticleActivity.class);
				i.putExtra("articleId", articleList.get(position).get(KEY_ID));
				startActivity(i);
				
			}
		});
		new LoadArticles().execute();
		
		return rootView;
	}
	 
	class LoadArticles extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			InputStream is = null;
			 JSONArray jArray = null;
			 String json = "";
			 try {
				 
			      // defaultHttpClient
			      DefaultHttpClient httpClient = new DefaultHttpClient();
			      HttpGet httpGet = new HttpGet(URL+articleType);
			      HttpResponse httpResponse = httpClient.execute(httpGet);
			      HttpEntity httpEntity = httpResponse.getEntity();
			      is = httpEntity.getContent();
			    } catch (UnsupportedEncodingException e) {
			      e.printStackTrace();
			    } catch (ClientProtocolException e) {
			      e.printStackTrace();
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			    try {
			      BufferedReader reader = new BufferedReader(new InputStreamReader(
			          is), 8);
			      StringBuilder sb = new StringBuilder();
			      String line = null;
			      while ((line = reader.readLine()) != null) {
			        sb.append(line + "n");
			      }
			      is.close();
			      json = sb.toString();
			    } catch (Exception e) {
			      
			    }
			    // try parse the string to a JSON object
			    try {
			      jArray = new JSONArray(json);
			      
			      for(int i=0;i<jArray.length();i++){
				    	JSONObject jObj = jArray.getJSONObject(i);
				    	HashMap<String, String> map = new HashMap<String, String>();
				    	map.put(KEY_TITLE, jObj.getString(KEY_TITLE));
				    	map.put(KEY_CAT, jObj.getString(KEY_CAT));
				    	map.put(KEY_DATE, jObj.getString(KEY_DATE));
				    	map.put(KEY_TEXT, jObj.getString(KEY_TEXT));
				    	map.put(KEY_PIC, jObj.getString(KEY_PIC));
				    	map.put(KEY_ID, jObj.getString(KEY_ID));
				    	articleList.add(map);
				    }
			    } catch (JSONException e) {
			      
			    }
			   
			    return null;
			    
		}

		@Override
		protected void onPostExecute(Void result) {
			adapter.setData(articleList);
			adapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
		
		
		
		
	}

}
