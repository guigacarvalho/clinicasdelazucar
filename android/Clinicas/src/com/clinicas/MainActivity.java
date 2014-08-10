package com.clinicas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity {
	public static final String PREFS_NAME = "ClinicasPrefs";
	static final String URL = "http://clinicas.engr.scu.edu/index.php/clinicas_api/categories/";
	public static final String KEY_ID = "categoryId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "description";
    public static final String KEY_PIC = "picture";
	
	private String[] drawerMenuTitles;
    private DrawerLayout mDrawerLayout;
   // private ListView mDrawerList;
    private ExpandableListView mDrawerExpList;
    ActionBarDrawerToggle mDrawerToggle;
    
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    Fragment fragment;
    
    
    List<String> listDataHeader;
    // hash for storing categories from the servers
    HashMap<String, List<String>> listDataChild;
    ArrayList<HashMap<String, String>> categoriesMapArray;
    


	DrawerListAdapter drawerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		drawerMenuTitles = getResources().getStringArray(R.array.drawer_array);
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       // mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerExpList = (ExpandableListView) findViewById(R.id.expandableListView1);
        prepareListData();
        drawerAdapter = new DrawerListAdapter(getApplicationContext(),listDataHeader, listDataChild);
        mDrawerExpList.setAdapter(drawerAdapter);
       /* mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, drawerMenuTitles));*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //mDrawerList.setOnItemClickListener(new NavDrawerClickListener());
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        categoriesMapArray = new ArrayList<HashMap<String,String>>();
         mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_navigation_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); 
            }

            public void onDrawerOpened(View drawerView) {
            	getSupportActionBar().setTitle(mDrawerTitle);
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
				System.out.println(categoriesMapArray.get(childPosition).get(KEY_ID));
				selectCategory(childPosition);
				return false;
			}

			
		});
        selectItem(0);
        
        
        DisplayImageOptions options = new DisplayImageOptions.Builder().
        		cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options).
        									diskCacheSize(100*1024*1024).build();
        ImageLoader.getInstance().init(config);
        
	}
	
	
	
	private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
 
        // Adding child data
        listDataHeader.add(drawerMenuTitles[0]);
        listDataHeader.add(drawerMenuTitles[1]);
        listDataHeader.add(drawerMenuTitles[2]);
        listDataHeader.add(drawerMenuTitles[3]);
        listDataHeader.add(drawerMenuTitles[4]);
 
        // Adding child data
        List<String> categories = new ArrayList<String>();
       // categories.add("Diet");
        //categories.add("Health");
        new LoadCategories().execute();
 
        List<String> home = new ArrayList<String>();
        List<String> feedback = new ArrayList<String>();
        List<String> account_info = new ArrayList<String>();
        List<String> help = new ArrayList<String>();
        
        
 
        listDataChild.put(listDataHeader.get(0), home); // Header, Child data
        listDataChild.put(listDataHeader.get(1), categories);
        listDataChild.put(listDataHeader.get(2), feedback);
        listDataChild.put(listDataHeader.get(3), account_info); // Header, Child data
        listDataChild.put(listDataHeader.get(4), help);
        
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
		//getMenuInflater().inflate(R.menu.main, menu);
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
			//System.out.println(pos);
		}

	}
	
	private void selectCategory(int childPosition) {
		fragment = new CategoryNewsFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("CATEGORY_ID", categoriesMapArray.get(childPosition).get(KEY_ID));
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.content_frame, fragment).commit();
        //fragmentManager.beginTransaction().remove(fragment).commit();
        //fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        setTitle(categoriesMapArray.get(childPosition).get(KEY_TITLE));
        mDrawerLayout.closeDrawer(mDrawerExpList);
        
	}
	
	private void selectItem(int position) {
        // update the main content by replacing fragments
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
        	fragment = new AccountInfoFragment();
            Bundle args = new Bundle();
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            
            args.putString("userId", settings.getString("userId", ""));
            fragment.setArguments(args);
            
            
        }
		break;
		case 4:
		{
        	fragment = new HelpFragment();
            Bundle args = new Bundle();
            
            fragment.setArguments(args);

            
            
        }
		break;
		default:
			break;
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
        //ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.content_frame, fragment).commit();
        
        // update selected item and title, then close the drawer
        //mDrawerList.setItemChecked(position, true);
       // mDrawerExpList.setItemChecked(position, true);
		if(position==0)
			setTitle("Clinicas");
		else
			setTitle(drawerMenuTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerExpList);
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
				    	System.out.println(jObj.getString(KEY_TITLE));
				    	map.put(KEY_PIC, jObj.getString(KEY_PIC));
				    	map.put(KEY_ID, jObj.getString(KEY_ID));
				    	categoriesMapArray.add(map);
				    }
			    } catch (JSONException e) {
			      
			    }
			   
			    return categoriesMapArray;
		}

		@Override
		protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
			ArrayList<String> categories = new ArrayList<String>();
			for (HashMap<String, String> hashMap : result) {
				categories.add(hashMap.get(KEY_TITLE));
			}
			 listDataChild.put(listDataHeader.get(1), categories);
			 drawerAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
    	
    	
    	
    	
    	
    }
}
