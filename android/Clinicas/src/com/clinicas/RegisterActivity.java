package com.clinicas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class RegisterActivity extends Activity {
	static final String URL = Constants.SERVER_URL+"/user/"; 
	EditText name, email, clinicasId;
	RadioGroup clinicasRG, diabetesRG, genderRG ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.account_info_fragment);
		
		
		name = (EditText)findViewById(R.id.info_name);
		email = (EditText)findViewById(R.id.info_email);
		final Button dobBtn = (Button)findViewById(R.id.dob_btn);
		final OnDateSetListener dobListener = new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				dobBtn.setText(monthOfYear+"/"+dayOfMonth+"/"+year);
				
			}
		};
		dobBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 final Calendar c = Calendar.getInstance();
                 int year = c.get(Calendar.YEAR);
                 int month = c.get(Calendar.MONTH) ;
                 int day = c.get(Calendar.DAY_OF_MONTH);
				new DatePickerDialog(RegisterActivity.this, dobListener, year, month, day).show();
				
			}
		});
		final LinearLayout clinicasLayout = (LinearLayout) findViewById(R.id.clinicas_group_layout);
        

		clinicasId = (EditText)findViewById(R.id.clinicas_id);
		genderRG = (RadioGroup) findViewById(R.id.gender_group);
		diabetesRG = (RadioGroup) findViewById(R.id.diabetes_group);
		clinicasRG =(RadioGroup) findViewById(R.id.clinicas_group);
        clinicasRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.clinicas_no){
					clinicasId.setVisibility(View.INVISIBLE);
				}
				else if(checkedId == R.id.clinicas_yes){
					clinicasId.setVisibility(View.VISIBLE);
				}
				
			}
		});
        
		diabetesRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				if(checkedId == R.id.diabetes_yes){
					clinicasLayout.setVisibility(View.VISIBLE);
					clinicasId.setVisibility(View.VISIBLE);
				}
				else if(checkedId == R.id.diabetes_no){
					clinicasLayout.setVisibility(View.INVISIBLE);
					clinicasId.setVisibility(View.INVISIBLE);
				}
				
			}
		});
        
        Button btn = (Button)findViewById(R.id.update_account_button);
        btn.setText("Register");
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new RegisterUser().execute();
				
			}
		});
	}
	
	
	class RegisterUser extends AsyncTask<Void, Void, Void>{

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
	    HttpPut httpput = new HttpPut(URL);

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
	        
	        nameValuePairs.add(new BasicNameValuePair("accountId", clinicasId.getText().toString()));
	        
	        httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httpput);
	        System.out.println(response);
	        
	    } catch (ClientProtocolException e) {
	        
	    } catch (IOException e) {
	        
	    }
	}

}