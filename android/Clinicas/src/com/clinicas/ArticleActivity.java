package com.clinicas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
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

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.clinicas.NotifyingScrollView.OnScrollChangedListener;
import com.database.Article;
import com.database.ArticleDataSource;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ArticleActivity extends ActionBarActivity {
	
	


	static final String URL = Constants.SERVER_URL;
	static String ARTICLE_URL = "/article/articleId/";
	static String GET_REVIEW_URL = "/reviews/articleId/";
	
	static String PUT_REVIEW_URL = "/review";
	ImageView articleImage;
	TextView articleTitle,  articleDate;
	EditText comment;
	WebView articleContent, articleComments;
	String articleId;
	RatingBar rating;
	ArticleDataSource dataSource;
	NotifyingScrollView articleScrollView;
	
	private Drawable mActionBarBackgroundDrawable;
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.actionbar_bg);
		mActionBarBackgroundDrawable.setAlpha(10);
		if(android.os.Build.VERSION.SDK_INT>11){
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
			getSupportActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);
			getSupportActionBar().setTitle("");
			
		}
		setContentView(R.layout.article_item);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_previous_item);
		
		articleImage = (ImageView)findViewById(R.id.article_image);
		articleTitle = (TextView) findViewById(R.id.article_title);
		articleContent = (WebView) findViewById(R.id.article_content);
		articleDate = (TextView) findViewById(R.id.article_date);
		articleId = getIntent().getExtras().getString("articleId");
		articleComments = (WebView) findViewById(R.id.article_comments);
		rating = (RatingBar) findViewById(R.id.article_rating);
		comment = (EditText) findViewById(R.id.article_comment);
		articleScrollView = (NotifyingScrollView)findViewById(R.id.article_scrollview);
		
		articleScrollView.setOnScrollChangedListener(new OnScrollChangedListener() {
			
			@SuppressLint("NewApi")
			@Override
			public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
				if(android.os.Build.VERSION.SDK_INT>11){
				final int headerHeight = findViewById(R.id.article_image).getHeight() - getActionBar().getHeight();
	            final float ratio = (float) Math.min(Math.max(t, 0), headerHeight) / headerHeight;
	            final int newAlpha = (int) (ratio * 255);
	            
	            mActionBarBackgroundDrawable.setAlpha(newAlpha);
				}
				
			}
		});
		
		Button submitComments = (Button)findViewById(R.id.comment_submit);
		
		dataSource = new ArticleDataSource(getApplicationContext());
		
		submitComments.setOnClickListener(new OnClickListener(
				) {
			
			@Override
			public void onClick(View arg0) {
				new SubmitComment().execute();
			}
		});
		
		
		rating.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				new SendFeedback().execute();
				
			}
		});
		/*dataSource.open();
		Article article = dataSource.getArticle(articleId);
		if(article!=null){
			articleContent.loadData(article.getContent(), "text/html", null);;
			articleTitle.setText(article.getTitle());
			articleDate.setText(article.getDate());
			new ImageDownloader().execute(article.getPictureUrl());
			new GetFeedback().execute();
		}
		else
		{
			
		}
		dataSource.close();*/
		dataSource.open();
		if(dataSource.articleExists(articleId))
			loadArticleFromDB();
		else
			new LoadArticle().execute();
		
		dataSource.close();
		
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
			      HttpGet httpGet = new HttpGet(URL+ARTICLE_URL+articleId);
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
			    		dataSource.open();
			    		jArray = new JSONArray(json);
			    		
			      
				    	jObj = jArray.getJSONObject(0);
				    	//jObj = new JSONObject(json);
				    	Article a = new Article();
				    	a.setArticleId(articleId);
				    	a.setContent(jObj.getString("content"));
				    	dataSource.createArticle(a);
				    	
			    } catch (JSONException e) {
			      e.printStackTrace();
			    }
			    
			    finally{
			    	dataSource.close();
			    }
			return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
		
			loadArticleFromDB();
			super.onPostExecute(result);
		}
		
		
		
	}
	
	void loadArticleFromDB(){
		dataSource.open();
		Article article = dataSource.getArticle(articleId);
		articleContent.loadData(article.getContent(), "text/html", null);
		articleTitle.setText(article.getTitle());
		SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy-mm-dd");
		SimpleDateFormat dFormatter = new SimpleDateFormat("dd MMM, yyyy");
		articleDate.setText(article.getFormattedDate(sFormatter, dFormatter));
		
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
						.resetViewBeforeLoading(true)
						.showImageForEmptyUri(R.drawable.logo_clinicas)
						.showImageOnFail(R.drawable.logo_clinicas)
						.showImageOnLoading(R.drawable.logo_clinicas)
						.cacheOnDisk(true)
						.imageScaleType(ImageScaleType.EXACTLY)
						.build();
		
		imageLoader.displayImage(article.getPictureUrl(), articleImage, options);
		
		new GetFeedback().execute();
		dataSource.close();
	}
	
	/*class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
		  

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
		}*/
	
	class GetFeedback extends AsyncTask<Void, Void, JSONArray> {
		  

		  protected JSONArray doInBackground(Void... urls) {
			  InputStream is = null;
				 JSONArray jArray = null;
				 
				// JSONObject jObj = new JSONObject();
				 String json = "";
				 try {
					 
				      // defaultHttpClient
				      DefaultHttpClient httpClient = new DefaultHttpClient();
				      HttpGet httpGet = new HttpGet(URL+GET_REVIEW_URL+articleId);
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
				      
				     
					    	
					    	
				    } catch (JSONException e) {
				      
				    }
				//return jObj;
					return jArray;
		  }

		  protected void onPostExecute(JSONArray result) {
		     // articleImage.setImageBitmap(result);
			  try {
			  float userRating = 0;
			  JSONObject jObj = new JSONObject();
			  String comments="";
			  for(int i=0;i<result.length();i++){
		    	  jObj = result.getJSONObject(i);
		    	  if(jObj.getString("comments").length()!=0 && jObj.getString("articleId").equals(articleId)){
		    		  comments+="<b>"+jObj.getString("comments")+"</b><br/>";
		    	  }
		    	  if(jObj.get("userId").toString().equals("0") && jObj.getString("articleId").equals(articleId))
		    		  userRating = Float.parseFloat(jObj.getString("rating").toString());
		    	  else
		    		  jObj=null;
		    	  
		    	  
		      }
			  if(jObj!=null){
				  
					rating.setRating(userRating);
				
			  }
			  articleComments.loadData(comments, "text/html", null);
			  
			  } catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		  }
		}
	
	
	class SendFeedback extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPut httpput = new HttpPut(URL+PUT_REVIEW_URL);

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("rating", Float.toString(rating.getRating())));
		        nameValuePairs.add(new BasicNameValuePair("articleId", articleId));
		        nameValuePairs.add(new BasicNameValuePair("userId", "0"));


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
	
	
	class SubmitComment extends AsyncTask<Void, Void, Integer>{

		@Override
		protected Integer doInBackground(Void... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPut httpput = new HttpPut(URL+PUT_REVIEW_URL);

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("comments",comment.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("articleId", articleId));
		        nameValuePairs.add(new BasicNameValuePair("userId", "0"));

		        httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httpput);
		        if(response.getStatusLine().getStatusCode()==200){
		        	return 200;
		        	//return 200;
		        }
		        
		    } catch (ClientProtocolException e) {
		        
		    } catch (IOException e) {
		        
		    }
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result==200){
				Toast.makeText(getApplicationContext(), "Comment submitted", Toast.LENGTH_SHORT).show();
	        	comment.setText("");
	        	
				super.onPostExecute(result);
			}
			super.onPostExecute(result);
		}

		
		
		
		
	}
	
	
}
