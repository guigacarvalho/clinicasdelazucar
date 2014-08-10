package com.clinicas.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.clinicas.R;


public class AccountInfoFragment extends Fragment {
	static final String URL = "http://clinicas.engr.scu.edu/index.php/clinicas_api/user/";
	static String userId = "";
	public static final String PREFS_NAME = "ClinicasPrefs";
	EditText clinicasID,rptPassword, email, name;
	TextView dob;
	RadioGroup clinicasRG, diabetesRG, genderRG ;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View rootView = inflater.inflate(R.layout.account_info_fragment, container, false);
		ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		clinicasID = (EditText)rootView.findViewById(R.id.clinicas_id);
		//EditText password = (EditText)rootView.findViewById(R.id.info_password);
		rptPassword = (EditText)rootView.findViewById(R.id.info_repeat_password);
		LinearLayout infoRoot = (LinearLayout)rootView.findViewById(R.id.info_root);
		infoRoot.removeView(rptPassword);
		email = (EditText)rootView.findViewById(R.id.info_email);
		name = (EditText)rootView.findViewById(R.id.info_name);
		dob = (TextView)rootView.findViewById(R.id.info_dob);
		Button dobBtn = (Button)rootView.findViewById(R.id.dob_btn);
		final OnDateSetListener dobListener = new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				dob.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
				
			}
		};
		dobBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 final Calendar c = Calendar.getInstance();
                 int year = c.get(Calendar.YEAR);
                 int month = c.get(Calendar.MONTH) ;
                 int day = c.get(Calendar.DAY_OF_MONTH);
				new DatePickerDialog(getActivity(), dobListener, year, month, day).show();
				
			}
		});
		
		diabetesRG = (RadioGroup)rootView.findViewById(R.id.diabetes_group);
		Bundle args = getArguments();
		userId = "userId/"+args.getString("userId");
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        clinicasRG = (RadioGroup)rootView.findViewById(R.id.clinicas_group);
        genderRG = (RadioGroup) rootView.findViewById(R.id.gender_group);
        clinicasRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.clinicas_no){
					clinicasID.setVisibility(View.INVISIBLE);
				}
				else if(checkedId == R.id.clinicas_yes){
					clinicasID.setVisibility(View.VISIBLE);
				}
				
			}
		});
        Button btn = (Button)rootView.findViewById(R.id.update_account_button);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new UpdateUser().execute();
				
			}
		});
        new LoadUser().execute();
		return rootView;
	}
	
	
	
	
	class UpdateUser extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			postData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			
			super.onPostExecute(result);
		}
		
	}
	
	public void postData() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(URL);

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("age", "25"));
	        nameValuePairs.add(new BasicNameValuePair("password", "test"));
	        nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
	        if(genderRG.getCheckedRadioButtonId()==R.id.male)
	        	nameValuePairs.add(new BasicNameValuePair("gender", "1"));
	        else
	        	nameValuePairs.add(new BasicNameValuePair("gender", "0"));
	        
	        if(diabetesRG.getCheckedRadioButtonId()==R.id.diabetes_yes)
	        	nameValuePairs.add(new BasicNameValuePair("hasDiabetes", "1"));
	        else
	        	nameValuePairs.add(new BasicNameValuePair("hasDiabetes", "0"));
	        
	        
	        if(clinicasRG.getCheckedRadioButtonId()==R.id.clinicas_yes)
	        	nameValuePairs.add(new BasicNameValuePair("isClinicasMember", "1"));
	        else
	        	nameValuePairs.add(new BasicNameValuePair("isClinicasMember", "0"));
	        
	        nameValuePairs.add(new BasicNameValuePair("accountId", clinicasID.getText().toString()));
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        System.out.println(response.toString());
	        
	    } catch (ClientProtocolException e) {
	       
	    } catch (IOException e) {
	      
	    }
	}
	
	class LoadUser extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected  JSONObject doInBackground(Void... params) {
			InputStream is = null;
			 JSONArray jArray = null;
			 
			 JSONObject jObj = new JSONObject();
			 String json = "";
			 try {
				 
			      // defaultHttpClient
			      DefaultHttpClient httpClient = new DefaultHttpClient();
			      HttpGet httpGet = new HttpGet(URL+userId);
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
			      
			     
				    	jObj = jArray.getJSONObject(0);
				    	
				  
			    } catch (JSONException e) {
			      
			    }
			   
			    return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				name.setText(result.getString("name"));
				email.setText(result.getString("email"));
				dob.setText(result.getString("age"));
				
				if(result.getString("hasDiabetes").equals("1"))
					diabetesRG.check(R.id.diabetes_yes);
				else
					diabetesRG.check(R.id.diabetes_no);
					
				
				if(result.getString("isClinicasMember").equals("1")){
					clinicasRG.check(R.id.clinicas_yes);
					clinicasID.setVisibility(View.VISIBLE);
					
					clinicasID.setText(result.getString("accountId"));
				}
				else{
					clinicasRG.check(R.id.clinicas_no);
					clinicasID.setVisibility(View.INVISIBLE);
				}
				if(result.getString("gender").equals("1"))
					genderRG.check(R.id.male);
				
				else
					genderRG.check(R.id.female);
				
				
				
				
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
    	
    	
    	
    	
    	
    }
	
}
