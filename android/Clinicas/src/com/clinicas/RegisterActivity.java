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
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	static final String URL = Constants.SERVER_URL+"/user/"; 
	EditText name, email, clinicasId, password, passwordConfirm;
	RadioGroup clinicasRG, diabetesRG, genderRG ;
	String dob="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.account_info_fragment);
		
		
		name = (EditText)findViewById(R.id.info_name);
		email = (EditText)findViewById(R.id.info_email);
		password = (EditText) findViewById(R.id.info_password);
		passwordConfirm = (EditText) findViewById(R.id.info_repeat_password);
		clinicasId = (EditText) findViewById(R.id.clinicas_id);
		
		
		name.addTextChangedListener(new CustomTextWatcher(name));
		email.addTextChangedListener(new CustomTextWatcher(email));
		password.addTextChangedListener(new CustomTextWatcher(password));
		passwordConfirm.addTextChangedListener(new CustomTextWatcher(passwordConfirm));
		clinicasId.addTextChangedListener(new CustomTextWatcher(clinicasId));
		final Button dobBtn = (Button)findViewById(R.id.dob_btn);
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
					//clinicasId.setVisibility(View.VISIBLE);
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
				Boolean validationError=false;
				if(name.getText().length()==0){
					name.setError("Name cannot be empty");
					validationError=true;
				}
				if(email.getText().length()==0){
					email.setError("Email cannot be empty");
					validationError=true;
				}
				if(password.getText().length()==0||passwordConfirm.getText().length()==0){
					password.setError("password can't be empty");
					passwordConfirm.setError("Password confirm can't be empty");
					validationError=true;
				}
				if(dob.length()==0){
					Toast.makeText(getApplicationContext(), "Date of birth required", Toast.LENGTH_SHORT).show();
					validationError=true;
				}
				if(!password.getText().toString().equals(passwordConfirm.getText().toString())){
					Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_SHORT).show();
					validationError=true;
				}
				if(diabetesRG.getCheckedRadioButtonId()==R.id.diabetes_yes && clinicasRG.getCheckedRadioButtonId()==R.id.clinicas_yes && clinicasId.getText().length()==0 ){
					clinicasId.setError("ID can't be empty if you are clinicas member");
					validationError=true;
				}
				if(validationError)
					return;
				else
				{
					name.setError(null);
					email.setError(null);
					password.setError(null);
					passwordConfirm.setError(null);
					clinicasId.setError(null);
				}
				
				new RegisterUser().execute();
				
			}
		});
	}
	
	
	class RegisterUser extends AsyncTask<Void, Void, Integer>{
		ProgressDialog progress;
		
		
		
		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(RegisterActivity.this);
			progress.setTitle("Registering");
			progress.setMessage("Please wait...");
			progress.show();
			super.onPreExecute();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			
			
			
			return Integer.valueOf(postData());
		}

		@Override
		protected void onPostExecute(Integer result) {
			
			progress.cancel();
			if(result.intValue()==200){
				Intent resultIntent = new Intent();
				resultIntent.putExtra(LoginActivity.KEY_EMAIL, email.getText().toString());
				resultIntent.putExtra(LoginActivity.KEY_PASSWORD, password.getText().toString());
				setResult(RESULT_OK, resultIntent);
				finish();
			}
			else 
				Toast.makeText(getApplicationContext(), "Register Failed", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
		
	}
	
	public int postData() {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPut httpput = new HttpPut(URL);
	    int status=-1;
	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("name", name.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("age", dob));
	        nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
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
	        //System.out.println(response.getStatusLine().getStatusCode());
	        status = response.getStatusLine().getStatusCode();
	        
	    } catch (ClientProtocolException e) {
	        
	    } catch (IOException e) {
	        
	    }
	    return status;
	}

	
	private class CustomTextWatcher implements TextWatcher {
	    private EditText mEditText;

	    public CustomTextWatcher(EditText e) { 
	        mEditText = e;
	    }

		@Override
		public void afterTextChanged(Editable s) {
			if(mEditText.getText().length()!=0)
				mEditText.setError(null);
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
		}
	}
}
