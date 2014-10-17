package com.clinicas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import com.clinicas.fragments.AccountInfoFragment;
import com.clinicas.fragments.CategoryNewsFragment;
import com.clinicas.fragments.FeedbackFragment;
import com.clinicas.fragments.HelpFragment;
import com.clinicas.fragments.MainFragment;
import com.database.LoginDataSource;
import com.database.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {
	public static final String PREFS_NAME = "ClinicasPrefs";
	static final String URL = Constants.SERVER_URL+"/categories/";
	public static final String KEY_ID = "categoryId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "description";
    public static final String KEY_PIC = "picture";
    public static final String KEY_LOCALE = "languageId";
	public static int LOGIN_CODE = 110;
	private String[] drawerMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerExpList;
    ActionBarDrawerToggle mDrawerToggle;
    
    private CharSequence mTitle;
    Fragment fragment;
    
    
    int currentGroupPos, currentChildPos;
    
    List<String> listDataHeader;
    // hash for storing categories from the servers
    HashMap<String, List<String>> listDataChild;
    ArrayList<HashMap<String, String>> categoriesMapArray;
    
    LoginDataSource dataSource;

	DrawerListAdapter drawerAdapter;
	boolean reloadMain;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		currentGroupPos = -1;
		currentChildPos = -1;
		drawerMenuTitles = getResources().getStringArray(R.array.drawer_array);
		mTitle =  getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerExpList = (ExpandableListView) findViewById(R.id.expandableListView1);
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        drawerAdapter = new DrawerListAdapter(getApplicationContext(),listDataHeader, listDataChild);
        prepareListData();
        new LoadCategories().execute();
        
        mDrawerExpList.setAdapter(drawerAdapter);
        
        
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       categoriesMapArray = new ArrayList<HashMap<String,String>>();
         mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_navigation_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	supportInvalidateOptionsMenu(); 
            }

            public void onDrawerOpened(View drawerView) {
            	supportInvalidateOptionsMenu(); 
            }
                
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerExpList.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				if(groupPosition!=1)
				selectItem(groupPosition);
				return false;
			}
		});
        
        mDrawerExpList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
					int childPosition, long id) {
				selectCategory(childPosition);
				return false;
			}

			
		});
        selectItem(0);
        
        
        dataSource = new LoginDataSource(getApplicationContext());	
        
        DisplayImageOptions options = new DisplayImageOptions.Builder().
        		cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options).
        									diskCacheSize(100*1024*1024).build();
        ImageLoader.getInstance().init(config);
        reloadMain=false;
	}
	
	
	
	
	@Override
	protected void onPostResume() {
		
		super.onPostResume();
		
		
		
	}




	public int GetPixelFromDips(float pixels) {
	    // Get the screen's density scale 
	    final float scale = getResources().getDisplayMetrics().density;
	    // Convert the dps to pixels, based on density scale
	    return (int) (pixels * scale + 0.5f);
	}
	
	private void prepareListData() {
        listDataChild.clear();
        listDataHeader.clear();
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean loggedIn = settings.getBoolean("loggedIn", false);
	    if(loggedIn){
			listDataHeader.add(drawerMenuTitles[0]);
	        listDataHeader.add(drawerMenuTitles[1]);
	        listDataHeader.add(drawerMenuTitles[2]);
	        listDataHeader.add(drawerMenuTitles[3]);
	        listDataHeader.add(drawerMenuTitles[4]);
			listDataHeader.add("Logout");
		}
		else{
			listDataHeader.add(drawerMenuTitles[0]);
	        listDataHeader.add(drawerMenuTitles[1]);
	        listDataHeader.add(drawerMenuTitles[2]);
	        listDataHeader.add(drawerMenuTitles[4]);
			listDataHeader.add(drawerMenuTitles[5]);
		}
        
 
        // Adding child data
        List<String> categories = new ArrayList<String>();

        
        List<String> home = new ArrayList<String>();
        List<String> feedback = new ArrayList<String>();
        List<String> account_info = new ArrayList<String>();
        List<String> help = new ArrayList<String>();
        List<String> login = new ArrayList<String>();
        List<String> logout = new ArrayList<String>();
 
        
        
        if(loggedIn){
        	
        	listDataChild.put(listDataHeader.get(0), home); // Header, Child data
             listDataChild.put(listDataHeader.get(1), categories);
	         listDataChild.put(listDataHeader.get(2), feedback);
	         listDataChild.put(listDataHeader.get(3), account_info); // Header, Child data
	         listDataChild.put(listDataHeader.get(4), help);
        	listDataChild.put(listDataHeader.get(5), logout);
        	
        }
        else{
        	listDataChild.put(listDataHeader.get(0), home); // Header, Child data
            listDataChild.put(listDataHeader.get(1), categories);
             listDataChild.put(listDataHeader.get(2), feedback);
            listDataChild.put(listDataHeader.get(3), help);
        	listDataChild.put(listDataHeader.get(4), login);
        }
        
    }
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	
	
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	public ArrayList<HashMap<String, String>> getCategoriesMapArray() {
		return categoriesMapArray;
	}
	
	public class NavDrawerClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			selectItem(pos);
			
		}

	}
	
	private void selectCategory(int childPosition) {
		fragment = new CategoryNewsFragment();
        // Supply index input as an argument.
		mDrawerLayout.closeDrawer(mDrawerExpList);
		if(currentChildPos==childPosition)
			return;
		currentChildPos = childPosition;
		currentGroupPos = -1;
		Bundle args = new Bundle();
        args.putString("CATEGORY_ID", categoriesMapArray.get(childPosition).get(KEY_ID));
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).commit();
        setTitle(categoriesMapArray.get(childPosition).get(KEY_TITLE));
    }
	
	private void selectItem(int position) {
		mDrawerLayout.closeDrawer(mDrawerExpList);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    boolean loggedIn = settings.getBoolean("loggedIn", false);
        // update the main content by replacing fragments
		if(currentGroupPos==position && position!=5)
			return;
		currentGroupPos=position;
		currentChildPos = -1;
		switch (position) {
		case 0:
		{
			fragment = new MainFragment();
	        Bundle args = new Bundle();
	        
	        fragment.setArguments(args);

        
        }
		break;
		case 2:
		{
        	fragment = new FeedbackFragment();
            Bundle args = new Bundle();
            
            fragment.setArguments(args);

            
        }
		break;
		case 3:
		{
			if(loggedIn){
        	fragment = new AccountInfoFragment();
            Bundle args = new Bundle();
            dataSource.open();
            User user = dataSource.getUser();
            dataSource.close();
            
            args.putString("userId", user.getUserID());
            args.putString(LoginActivity.KEY_AUTH_TOKEN, user.getAuthToken());
            fragment.setArguments(args);
			}
			else{
				fragment = new HelpFragment();
	            Bundle args = new Bundle();
	            
	            fragment.setArguments(args);
			}
            
        }
		break;
		case 4:
		{
			if(!loggedIn){
					startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), LOGIN_CODE );
				}
			else{
			fragment = new HelpFragment();
            Bundle args = new Bundle();
            
            fragment.setArguments(args);
			}
            
            
        }
		
		break;
		case 5:
		{
			
			if(!loggedIn){
					startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), LOGIN_CODE );
				}
				else{
					SharedPreferences.Editor editor = settings.edit();
				    editor.putBoolean("loggedIn", false);
				    dataSource.open();
				    dataSource.deleteLogin();
				    dataSource.close();
				    editor.commit();
				    prepareListData();
				    new LoadCategories().execute();
				    selectItem(0);
				    
				}
			
			
		}
		break;

		default:
			break;
		}
		if(fragment!=null){
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
	        ft.replace(R.id.content_frame, fragment).commit();
		}
        if(position==0||position==5||position==6)
			setTitle("Clinicas");
		else
			setTitle(drawerMenuTitles[position]);
		
       
    }

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    
	    
	        if (resultCode == RESULT_OK) {
	        	prepareListData();
	        	new LoadCategories().execute();
	        	reloadMain=true;
	        	
	        }
	   
	}
	
	
	
	
	
    @Override
	protected void onResume() {
		if(reloadMain){
			selectItem(0);
			reloadMain=false;
		}
		
		super.onResume();
	}




	@Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
    
    
    class LoadCategories extends AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>{

		@Override
		protected  ArrayList<HashMap<String, String>> doInBackground(Void... params) {
			InputStream is = null;
			 JSONArray jArray = null;
			 HashMap<String, String> map;
			 categoriesMapArray = new ArrayList<HashMap<String,String>>();
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
			    } catch (Exception e) {
			      
			    }
			    // try parse the string to a JSON object
			    try {
			      jArray = new JSONArray(json);
			      
			      for(int i=0;i<jArray.length();i++){
				    	JSONObject jObj = jArray.getJSONObject(i);
				    	map = new HashMap<String, String>();
				    	map.put(KEY_TITLE, jObj.getString(KEY_TITLE));
				    	map.put(KEY_PIC, jObj.getString(KEY_PIC));
				    	map.put(KEY_ID, jObj.getString(KEY_ID));
				    	map.put(KEY_LOCALE, jObj.getString(KEY_LOCALE));
				    	categoriesMapArray.add(map);
				    }
			    } catch (JSONException e) {
			      
			    }
			   
			    return categoriesMapArray;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			ArrayList<String> categories = new ArrayList<String>();
			Locale current = getResources().getConfiguration().locale;
			for (HashMap<String, String> hashMap : result) {
				if(current.getLanguage().equals(hashMap.get(KEY_LOCALE)))
				categories.add(hashMap.get(KEY_TITLE));
			}
			listDataChild.put(listDataHeader.get(1), categories);
				
			 drawerAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		
		super.onConfigurationChanged(newConfig);
	}
	
	
    
    
    
}
