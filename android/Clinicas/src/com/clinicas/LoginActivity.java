package com.clinicas;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.database.LoginDataSource;
import com.database.User;

import android.app.ProgressDialog;
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
	static final String URL = Constants.SERVER_URL+"/authenticate/";
	public static final String KEY_ID = "userId";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_AUTH_TOKEN = "authToken";
    public static final int REG_CODE = 100;
	Button loginBtn, regBtn;
	EditText loginTxt, passwordTxt;
	String userEmail, userPass;
	LoginDataSource dataSource;
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
				//new LoadAndCheckUser().execute();
				userEmail = loginTxt.getText().toString();
				userPass =  passwordTxt.getText().toString();
				new LoginUser().execute();
			}
		});
		regBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getApplicationContext(), RegisterActivity.class), REG_CODE );
			}
		});
		
		dataSource = new LoginDataSource(getApplicationContext());
		
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    
	    
	        if (resultCode == RESULT_OK) {
	        	userEmail = data.getStringExtra(KEY_EMAIL);
				userPass =  data.getStringExtra(KEY_PASSWORD);
	            new LoginUser().execute();
	        }
	   
	}
	
	
	class LoginUser extends AsyncTask<Void, Void, JSONObject>{
		ProgressDialog progress;
		
		
		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(LoginActivity.this);
			progress.setTitle("Logging in");
			progress.setMessage("Please wait...");
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			
			
			InputStream is = null;
			 JSONObject jObject = null;
			 String json = "";
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        
		        nameValuePairs.add(new BasicNameValuePair(KEY_EMAIL, userEmail));
		        nameValuePairs.add(new BasicNameValuePair(KEY_PASSWORD, userPass));
			      // defaultHttpClient
			      DefaultHttpClient httpClient = new DefaultHttpClient();
			      HttpPost httpPost = new HttpPost(URL);
			      httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			      HttpResponse httpResponse = httpClient.execute(httpPost);
			      
			      HttpEntity httpEntity = httpResponse.getEntity();
			      is = httpEntity.getContent();
			      if(httpResponse.getStatusLine().getStatusCode()==200){
			      BufferedReader reader = new BufferedReader(new InputStreamReader(
				          is), 8);
				      StringBuilder sb = new StringBuilder();
				      String line = null;
				      while ((line = reader.readLine()) != null) {
				        sb.append(line + "n");
				      }
				      is.close();
				      json = sb.toString();
				      
			      }
			      else
			    	 ;
			    	  //Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
			    } catch (UnsupportedEncodingException e) {
			      e.printStackTrace();
			    } catch (ClientProtocolException e) {
			      e.printStackTrace();
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			    catch (Exception e) {
			      e.printStackTrace();
			    }
			    // try parse the string to a JSON object
			    try {
			      jObject = new JSONObject(json);
			      

			    } catch (JSONException e) {
			      
			    }
			return jObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			progress.cancel();
			if(result!=null){
				try{
				HashMap<String, String> h = new HashMap<String, String>();
				h.put(KEY_NAME,userEmail );
				h.put(KEY_ID,result.getString(KEY_ID) );
				h.put(KEY_AUTH_TOKEN, result.getString(KEY_AUTH_TOKEN));

				setLogin(h);
				}
				catch(Exception e){
					
				}
				
				Intent resultIntent = new Intent();
				resultIntent.putExtra(LoginActivity.KEY_EMAIL, userEmail);
				setResult(RESULT_OK, resultIntent);
				finish();

			}else
			{
				Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
			}
			
			super.onPostExecute(result);
		}
		
		
		
		
	}
	



/*
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
			      System.out.println("	result json: "+json);
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
			
					HashMap<String, String> h = new HashMap<String, String>();
					h.put(KEY_NAME,loginTxt.getText().toString() );
					h.put(KEY_ID,"1" );
					

					setLogin(h);
				
			Toast.makeText(getApplicationContext(), "Username/password incorrect", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
			finish();
			super.onPostExecute(result);
		}
		
	}
*/
	public void setLogin(HashMap<String, String> hashMap) {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putBoolean("loggedIn", true);
	    User user = new User();
	    user.setAuthToken(hashMap.get(KEY_AUTH_TOKEN));
	    user.setUserID(hashMap.get(KEY_ID));
	    user.setUserName(hashMap.get(KEY_NAME));
	    dataSource.open();
	    dataSource.storeLogin(user);
	    dataSource.close();
	   // editor.putString("userName", hashMap.get(KEY_NAME));
	   // editor.putString("userId", hashMap.get(KEY_ID));
	   // editor.putString(KEY_AUTH_TOKEN, hashMap.get(KEY_AUTH_TOKEN));
		editor.commit();
	}
	
	@Override
	protected void onPostResume() {
		
		super.onPostResume();
		
	}
	
	
	
}
