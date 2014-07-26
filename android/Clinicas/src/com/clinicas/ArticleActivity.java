package com.clinicas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.RatingBar.OnRatingBarChangeListener;

public class ArticleActivity extends ActionBarActivity {
	
	


	static final String URL = "http://clinicas.engr.scu.edu/index.php/clinicas_api/";
	static String article_URL = "article/articleId/";
	static String review_URL = "review";
	ImageView articleImage;
	TextView articleTitle,  articleDate;
	EditText comment;
	WebView articleContent;
	String articleId;
	RatingBar rating;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.article_item);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		articleImage = (ImageView)findViewById(R.id.article_image);
		articleTitle = (TextView) findViewById(R.id.article_title);
		articleContent = (WebView) findViewById(R.id.article_content);
		articleDate = (TextView) findViewById(R.id.article_date);
		articleId = getIntent().getExtras().getString("articleId");
		rating = (RatingBar) findViewById(R.id.article_rating);
		comment = (EditText) findViewById(R.id.article_comment);
		Button submitComments = (Button)findViewById(R.id.comment_submit);
		submitComments.setOnClickListener(new OnClickListener(
				) {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
		
		rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				System.out.println("here"+rating);
				new SendFeedback().execute();
				
			}
		});
		new LoadArticle().execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	       	finish();
	        return true;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	
	class LoadArticle extends AsyncTask<Void, Void, JSONObject>{

		
		@Override
		protected JSONObject doInBackground(Void... params) {
			InputStream is = null;
			 JSONArray jArray = null;
			 
			 JSONObject jObj = new JSONObject();
			 String json = "";
			 try {
				 
			      // defaultHttpClient
			      DefaultHttpClient httpClient = new DefaultHttpClient();
			      HttpGet httpGet = new HttpGet(URL+article_URL+articleId);
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
				    	jObj = new JSONObject(json);
				    	
			    } catch (JSONException e) {
			      
			    }
			return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
		
			try {
				articleContent.loadData(result.getString("content"), "text/html", null);;
				articleTitle.setText(result.getString("title"));
				articleDate.setText(result.getString("date"));
				new ImageDownloader().execute(result.getString("pictureUrl"));
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}
		
		
		
	}
	
	
	class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
		  

		  protected Bitmap doInBackground(String... urls) {
		      String url = urls[0];
		      Bitmap mIcon = null;
		      try {
		        InputStream in = new java.net.URL(url).openStream();
		        mIcon = BitmapFactory.decodeStream(in);
		      } catch (Exception e) {
		         // Log.e("Error", e.getMessage());
		      }
		      return mIcon;
		  }

		  protected void onPostExecute(Bitmap result) {
		      articleImage.setImageBitmap(result);
		  }
		}
	
	
	class SendFeedback extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPut httpput = new HttpPut(URL+review_URL);

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("rating", Float.toString(rating.getRating())));
		        nameValuePairs.add(new BasicNameValuePair("articleId", articleId));
		        

		        httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httpput);
		        if(response.getStatusLine().getStatusCode()==200){
		        	
		        	//return 200;
		        }
		        
		    } catch (ClientProtocolException e) {
		        
		    } catch (IOException e) {
		        
		    }
			return null;
		}
		
	}
	
	
	class SubmitComment extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPut httpput = new HttpPut(URL+review_URL);

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("comments",comment.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("articleId", articleId));
		        

		        httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httpput);
		        if(response.getStatusLine().getStatusCode()==200){
		        	
		        	//return 200;
		        }
		        
		    } catch (ClientProtocolException e) {
		        
		    } catch (IOException e) {
		        
		    }
			return null;
		}

		
		
		
		
	}
	
	
}
