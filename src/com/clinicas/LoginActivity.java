package com.clinicas;


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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {
	public static final String PREFS_NAME = "ClinicasPrefs";
	static final String URL = "http://clinicas.engr.scu.edu/index.php/clinicas_api/users/";
	public static final String KEY_ID = "userId";
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    
	Button loginBtn, regBtn;
	EditText loginTxt, passwordTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		loginBtn = (Button)findViewById(R.id.login_button);
		regBtn = (Button)findViewById(R.id.register_button);
		loginTxt = (EditText) findViewById(R.id.login_email);
		passwordTxt = (EditText) findViewById(R.id.login_password);
		loginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new LoadAndCheckUser().execute();
				
			}
		});
		regBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
				
			}
		});
		
	}
	
	
	

	@Override
	protected void onPostResume() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean loggedIn = settings.getBoolean("loggedIn", false);
	       //setSilent(silent);
		if(loggedIn)
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
		super.onPostResume();
	}




	class LoadAndCheckUser extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>{

		@Override
		protected ArrayList<HashMap<String, String>> doInBackground(Void... arg0) {
			
			InputStream is = null;
			 JSONArray jArray = null;
			 HashMap<String, String> map;
			 ArrayList<HashMap<String, String>> mapArray = new ArrayList<HashMap<String,String>>();
			 String json = "";
			try {
				 
			      // defaultHttpClient
			      DefaultHttpClient httpClient = new DefaultHttpClient();
			      HttpGet httpGet = new HttpGet(URL);
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
			      System.out.println(json);
			    } catch (Exception e) {
			      
			    }
			    // try parse the string to a JSON object
			    try {
			      jArray = new JSONArray(json);
			      
			      for(int i=0;i<jArray.length();i++){
				    	JSONObject jObj = jArray.getJSONObject(i);
				    	map = new HashMap<String, String>();
				    	map.put(KEY_NAME, jObj.getString(KEY_NAME));
				    	map.put(KEY_PASSWORD, jObj.getString(KEY_PASSWORD));
				    	map.put(KEY_ID, jObj.getString(KEY_ID));
				    	mapArray.add(map);
				    }
			    } catch (JSONException e) {
			      
			    }
			return mapArray;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			
			for (HashMap<String, String> hashMap : result) {
				
				if(hashMap.get(KEY_NAME).equals(loginTxt.getText().toString()) && hashMap.get(KEY_PASSWORD).equals(passwordTxt.getText().toString())){
					startActivity(new Intent(getApplicationContext(), MainActivity.class));
					setLogin(hashMap);
				}
				
			}
			Toast.makeText(getApplicationContext(), "Username/password incorrect", Toast.LENGTH_SHORT).show();
			
			super.onPostExecute(result);
		}
		
	}

	public void setLogin(HashMap<String, String> hashMap) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("loggedIn", true);
	    editor.putString("userName", hashMap.get(KEY_NAME));
	    editor.putString("userId", hashMap.get(KEY_ID));
		editor.commit();
	}
	
	
	
}
