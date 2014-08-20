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
import android.widget.Toast;

import com.clinicas.Constants;
import com.clinicas.R;

public class FeedbackFragment extends Fragment {

	static String URL = Constants.SERVER_URL+"/clinicas_api/feedback/";
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
		list.add(getResources().getString(R.string.article_suggestion));
		list.add(getResources().getString(R.string.error));
		list.add(getResources().getString(R.string.bug_report));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
			R.layout.spinner_list_item, list);
		ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
		actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		dataAdapter.setDropDownViewResource(R.layout.spinner_list_item);
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

	
	
	class SubmitFeedback extends AsyncTask<Void, Void, Integer>{

		@Override
		protected Integer doInBackground(Void... params) {
			
			return postData();
		}

		@Override
		protected void onPostExecute(Integer result) {
		
			if(result==200){
				Toast.makeText(getActivity().getApplicationContext(), "Feedback submitted", Toast.LENGTH_SHORT).show();
	        	url.setText("");
	        	msg.setText("");
				super.onPostExecute(result);
			}
		}
		
		
		
		
	}
	
	
	public int postData() {
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
	        if(response.getStatusLine().getStatusCode()==200){
	        	
	        	return 200;
	        }
	        
	    } catch (ClientProtocolException e) {
	        
	    } catch (IOException e) {
	        
	    }
	    return -1;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	
	
	
}
