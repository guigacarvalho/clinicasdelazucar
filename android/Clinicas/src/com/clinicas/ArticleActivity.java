package com.clinicas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.database.LoginDataSource;
import com.database.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ArticleActivity extends ActionBarActivity {
	

	public static final String PREFS_NAME = "ClinicasPrefs";
	static final String URL = Constants.SERVER_URL;
	static String ARTICLE_URL = "/article/articleId/";
	static String GET_REVIEW_URL = "/reviews/articleId/";
	static String ARTICLE_FAV_URL = Constants.SERVER_URL+"/favorite/";
	 
	static String PUT_REVIEW_URL = "/review";
	String userId="", auth_token="";
	ImageView articleImage;
	TextView articleTitle,  articleDate;
	EditText comment;
	WebView articleContent, articleComments;
	String articleId;
	RatingBar rating;
	ArticleDataSource dataSource;
	LoginDataSource loginDataSource;
	NotifyingScrollView articleScrollView;
	Menu menu;
	boolean favArticle;
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
		favArticle = getIntent().getExtras().getBoolean("favorite");
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
		
		//dataSource = new ArticleDataSource(getApplicationContext());
		
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
		/*dataSource.open();
		if(dataSource.articleExists(articleId))
			loadArticleFromDB();
		else*/
			new LoadArticle().execute();
			new GetFeedback().execute();
			
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		    boolean loggedIn = settings.getBoolean("loggedIn", false);
		    if(!loggedIn){
		    	rating.setVisibility(View.INVISIBLE);
		    	comment.setVisibility(View.INVISIBLE);
		    	submitComments.setVisibility(View.INVISIBLE);
		    	
		    }
		    else
		    {
		    	loginDataSource = new LoginDataSource(getApplicationContext());
		    	loginDataSource.open();
	            User user = loginDataSource.getUser();
	            loginDataSource.close();
		    	userId = user.getUserID();
		    	auth_token = user.getAuthToken();
		    }
		//dataSource.close();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	       	finish();
	        return true;
	    case R.id.action_fav:
	    	// mark this as favorite for the logged in user
	    	if(!favArticle){
	    		menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_rating_important));
	    		new Favorite().execute(true);
	    		favArticle = true;
	    	}
	    	else{
	    		menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_rating_not_important));
	    		favArticle = false;
	    		new Favorite().execute(false);
	    	}
	    	break;
	    	
	    case R.id.action_refresh:
	    	//deleteArticleFromDB();
	    	new LoadArticle().execute();
	    	break;
	    }
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater(); 
	    inflater.inflate(R.menu.article, menu);
	    this.menu = menu;
	    if(favArticle)
	    	menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_rating_important));
    	
	    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean loggedIn = settings.getBoolean("loggedIn", false);
	    if(!loggedIn){
	    	MenuItem item = menu.findItem(R.id.action_fav);
	    	item.setVisible(false);
	    }
	    	
	    return true;
	}
	
	class Favorite extends AsyncTask<Boolean, Void, Integer>{

		@Override
		protected Integer doInBackground(Boolean... params) {
			HttpClient httpclient = new DefaultHttpClient();
			boolean addFav = params[0];
			
		    //System.out.println(articleId);
		    try {
		        // Add your data
		    	if(addFav){
					HttpPut httpput = new HttpPut(ARTICLE_FAV_URL+"userId/"+userId+"/authToken/"+auth_token);
				
			        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			        
			        nameValuePairs.add(new BasicNameValuePair("articleId", articleId));
			        nameValuePairs.add(new BasicNameValuePair("userId", userId));
			        nameValuePairs.add(new BasicNameValuePair("authToken", auth_token));
			        //nameValuePairs.add(new BasicNameValuePair("userId", "4"));
	
	
			        httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
			        // Execute HTTP Post Request
			        //System.out.println(httpput.getURI());
			        HttpResponse response = httpclient.execute(httpput);
			        //System.out.println(response.getStatusLine().getStatusCode());
			        if(response.getStatusLine().getStatusCode()==200){
			        	
			        	return 200;
			        }
		    	}
		    	else{
		    		//HttpDeleteWithBody httpDel = new HttpDeleteWithBody(ARTICLE_FAV_URL+"userId/"+userId+"/authToken/"+auth_token);
		    		 	HttpDelete httpDel = new HttpDelete(ARTICLE_FAV_URL+"userId/"+userId+"/authToken/"+auth_token+"/articleId/"+articleId);

		
				        // Execute HTTP Post Request
				        HttpResponse response = httpclient.execute(httpDel);
				        if(response.getStatusLine().getStatusCode()==200){
				        	
				        	return 200;
				        }
		    	}
		        
		    } catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			//if(result==200)
			//Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
		
		
		
	}
	
		
	
	class LoadArticle extends AsyncTask<Void, Void, Article>{

		
		@Override
		protected Article doInBackground(Void... params) {
			InputStream is = null;
			Article a = new Article();
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
			    		//dataSource.open();
			    		jObj = new JSONObject(json);
			    		
			      
				    	//jObj = new JSONObject(json);
				    	
				    	a.setArticleId(articleId);
				    	a.setContent(jObj.getString("content"));
				    	a.setCategoryId(jObj.getString("categoryId"));
				    	a.setPictureUrl(jObj.getString("pictureUrl"));
				    	a.setDate(jObj.getString("date"));
				    	a.setTitle(jObj.getString("title"));
				    	//dataSource.createArticle(a);
				    	
			    } catch (JSONException e) {
			      e.printStackTrace();
			    }
			    
			    
			return a;
		}

		@Override
		protected void onPostExecute(Article result) {
		
			loadArticle(result);
			super.onPostExecute(result);
		}
		
		
		
	}
	
	void deleteArticleFromDB(){
		dataSource.open();
		dataSource.deleteArticle(articleId);
		dataSource.close();
	}
	void loadArticle(Article article){
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
				      e.printStackTrace();
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
			  if(result!=null)
			  for(int i=0;i<result.length();i++){
		    	  jObj = result.getJSONObject(i);
		    	  if(jObj.getString("comments").length()!=0 && jObj.getString("articleId").equals(articleId)){
		    		  comments+="<b>"+jObj.getString("name")+":</b>";
		    		  comments+=jObj.getString("comments")+"<hr/>";
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
		    HttpPut httpput = new HttpPut(URL+PUT_REVIEW_URL+"/userId/"+userId+"/authToken/"+auth_token);

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("rating", Float.toString(rating.getRating())));
		        nameValuePairs.add(new BasicNameValuePair("articleId", articleId));
		        nameValuePairs.add(new BasicNameValuePair("userId", userId));
		        nameValuePairs.add(new BasicNameValuePair("authToken", auth_token));


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
		    HttpPut httpput = new HttpPut(URL+PUT_REVIEW_URL+"/userId/"+userId+"/authToken/"+auth_token);

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("comments",comment.getText().toString()));
		        nameValuePairs.add(new BasicNameValuePair("articleId", articleId));
		        nameValuePairs.add(new BasicNameValuePair("userId", userId));

		        httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		        System.out.println(httpput.getURI());
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
	        	new GetFeedback().execute();
				super.onPostExecute(result);
			}
			super.onPostExecute(result);
		}

		
	}
	
	
}
