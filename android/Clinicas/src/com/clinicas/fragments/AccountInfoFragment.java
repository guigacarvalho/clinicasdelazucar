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
import android.widget.Toast;

import com.clinicas.Constants;
import com.clinicas.R;
import com.database.LoginDataSource;
import com.database.User;


public class AccountInfoFragment extends Fragment {
	static final String URL = Constants.SERVER_URL+"/user/";
	static String userId = "", authToken="";
	public static final String PREFS_NAME = "ClinicasPrefs";
	EditText clinicasID,password, rptPassword, email, name;
	RadioGroup clinicasRG, diabetesRG, genderRG ;
	Button dobBtn;
	LinearLayout clinicasLayout ;
	String dob="";
	LoginDataSource dataSource;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		
		View rootView = inflater.inflate(R.layout.account_info_fragment, container, false);
		ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		clinicasID = (EditText)rootView.findViewById(R.id.clinicas_id);
		password = (EditText)rootView.findViewById(R.id.info_password);
		rptPassword = (EditText)rootView.findViewById(R.id.info_repeat_password);
		LinearLayout infoRoot = (LinearLayout)rootView.findViewById(R.id.info_root);
		infoRoot.removeView(rptPassword);
		email = (EditText)rootView.findViewById(R.id.info_email);
		name = (EditText)rootView.findViewById(R.id.info_name);
		dobBtn = (Button)rootView.findViewById(R.id.dob_btn);
		clinicasLayout = (LinearLayout) rootView.findViewById(R.id.clinicas_group_layout);
		final OnDateSetListener dobListener = new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				dobBtn.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
				dob = year+"-"+monthOfYear+"-"+dayOfMonth;
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
		dataSource = new LoginDataSource(getActivity().getApplicationContext());
		dataSource.open();
		User user = dataSource.getUser();
		userId = user.getUserID();
		authToken = user.getAuthToken();
		dataSource.close();
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
        diabetesRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				if(checkedId == R.id.diabetes_yes){
					clinicasLayout.setVisibility(View.VISIBLE);
					clinicasID.setVisibility(View.VISIBLE);
				}
				else if(checkedId == R.id.diabetes_no){
					clinicasLayout.setVisibility(View.INVISIBLE);
					clinicasID.setVisibility(View.INVISIBLE);
				}
				
			}
		});
        Button btn = (Button)rootView.findViewById(R.id.update_account_button);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Boolean validationError=false;
				
				if(name.getText().length()==0){
					name.setError("Name cannot be empty");
					validationError=true;
				}
				if(email.getText().length()==0){
					email.setError("Email cannot be empty");
					validationError=true;
				}
				
				if(dob.length()==0){
					Toast.makeText(getActivity().getApplicationContext(), "Date of birth required", Toast.LENGTH_SHORT).show();
					validationError=true;
				}
				
				if(diabetesRG.getCheckedRadioButtonId()==R.id.diabetes_yes && clinicasRG.getCheckedRadioButtonId()==R.id.clinicas_yes && clinicasID.getText().length()==0 ){
					clinicasID.setError("ID can't be empty if you are clinicas member");
					validationError=true;
				}
				if(validationError)
					return;
				else
				{
					name.setError(null);
					email.setError(null);
					password.setError(null);
					
					clinicasID.setError(null);
				}
				new UpdateUser().execute();
				
			}
		});
        new LoadUser().execute();
		return rootView;
	}
	
	
	
	
	class UpdateUser extends AsyncTask<Void, Void, Integer>{

		@Override
		protected Integer doInBackground(Void... params) {
			return Integer.valueOf(postData());
			
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == 200){
				Toast.makeText(getActivity().getApplicationContext(), "User updated", Toast.LENGTH_SHORT).show();;
			}
			else
				Toast.makeText(getActivity().getApplicationContext(), "Failed to update", Toast.LENGTH_SHORT).show();;
			super.onPostExecute(result);
		}
		
	}
	
	public int postData() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(URL+"userId/"+userId+"/authToken/"+authToken);
	    int status=-1;
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("userId", userId));
	        nameValuePairs.add(new BasicNameValuePair("authToken", authToken));
	        nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("age", dob));
	        if(password.getText().toString().length()!=0)
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
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
	        System.out.println("Params:"+httppost.getEntity().toString());
	        System.out.println(httppost.getURI());
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        status = response.getStatusLine().getStatusCode();
	        System.out.println("Reason:"+status+":"+response.getStatusLine().getReasonPhrase());
	        
	    } catch (ClientProtocolException e) {
	       
	    } catch (IOException e) {
	      
	    }
	    return status;
	}
	
	class LoadUser extends AsyncTask<Void, Void, JSONObject>{

		@Override
		protected  JSONObject doInBackground(Void... params) {
			InputStream is = null;
			 //JSONArray jArray = null;
			 
			 JSONObject jObj = new JSONObject();
			 String json = "";
			 try {
				 
			      // defaultHttpClient
			      DefaultHttpClient httpClient = new DefaultHttpClient();
			      HttpGet httpGet = new HttpGet(URL+"userId/"+userId+"/authToken/"+authToken);
			      System.out.println(httpGet.getURI());
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
			      jObj =new JSONObject(json);
				    	
				  
			    } catch (JSONException e) {
			      
			    }
			   
			    return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				name.setText(result.getString("name"));
				email.setText(result.getString("email"));
				dobBtn.setText(result.getString("age").split(" ")[0]);
				dob = result.getString("age").split(" ")[0];
				if(result.getString("hasDiabetes").equals("1")){
					diabetesRG.check(R.id.diabetes_yes);
					clinicasLayout.setVisibility(View.VISIBLE);
					clinicasID.setVisibility(View.VISIBLE);
					if(result.getString("isClinicasMember").equals("1")){
						clinicasRG.check(R.id.clinicas_yes);
						clinicasID.setVisibility(View.VISIBLE);
						
						clinicasID.setText(result.getString("accountId"));
					}
					else{
						clinicasRG.check(R.id.clinicas_no);
						clinicasID.setVisibility(View.INVISIBLE);
					}
				}
				else{
					diabetesRG.check(R.id.diabetes_no);
					clinicasLayout.setVisibility(View.INVISIBLE);
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	
	
}
