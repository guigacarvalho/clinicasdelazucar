package com.clinicas.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.clinicas.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class FeedbackFragment extends Fragment {

	static String URL = "http://clinicas.engr.scu.edu/index.php/clinicas_api/feedback/";
	Spinner feedbackTypes;
	EditText url, msg;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.feedback_fragment, container, false);
		feedbackTypes = (Spinner)rootView.findViewById(R.id.feedback_spinner);
		url = (EditText)rootView.findViewById(R.id.feedback_url);
		msg = (EditText)rootView.findViewById(R.id.feedback_msg);
		
		List<String> list = new ArrayList<String>();
		list.add("Article suggestion");
		list.add("Errors");
		list.add("Bug reports");
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
			android.R.layout.simple_spinner_item, list);
		ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		feedbackTypes.setAdapter(dataAdapter);
		
		Button submit = (Button)rootView.findViewById(R.id.feedback_submit);
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new SubmitFeedback().execute();
			}
		});
		
		
		return rootView;
	}

	
	
	class SubmitFeedback extends AsyncTask<Void, Void, Void>{

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
	        nameValuePairs.add(new BasicNameValuePair("type", Integer.toString(feedbackTypes.getSelectedItemPosition()+1)));
	        nameValuePairs.add(new BasicNameValuePair("message", msg.getText().toString()));
	        nameValuePairs.add(new BasicNameValuePair("url", url.getText().toString()));

	        httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httpput);
	        System.out.println(response);
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	}
	
	
}
