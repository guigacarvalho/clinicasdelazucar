package com.clinicas.fragments;

import com.clinicas.MainFragmentAdapter;
import com.clinicas.R;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class MainFragment extends Fragment {

	ViewPager mViewPager;
	PagerTabStrip tabStrip;
	 MainFragmentAdapter fragAdapter;
	 String[] tabNames;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.main_fragment, container, false);
		tabNames = getResources().getStringArray(R.array.tab_names);
		
		mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        tabStrip = (PagerTabStrip) rootView.findViewById(R.id.pager_title_strip);
        tabStrip.setTabIndicatorColor(Color.parseColor("#827d77"));
		fragAdapter = new MainFragmentAdapter(getChildFragmentManager(), getActivity().getApplicationContext());
        
        mViewPager.setAdapter(fragAdapter);
        
        
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        //actionBar.removeAllTabs();
        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        TabListener tabListener = new TabListener() {
			
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				
				
			}
			
			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				
				mViewPager.setCurrentItem(tab.getPosition());
			}
			
			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {
				
				
			}
		};
		
		/*new/*new ActionBar.TabListener() {


			@Override
			public void onTabReselected(
					android.support.v7.app.ActionBar.Tab arg0,
					android.support.v4.app.FragmentTransaction arg1) {
				
				
			}

			@Override
			public void onTabSelected(
					android.support.v7.app.ActionBar.Tab arg0,
					android.support.v4.app.FragmentTransaction arg1) {
				
				
			}

			@Override
			public void onTabUnselected(
					android.support.v7.app.ActionBar.Tab tab,
					android.support.v4.app.FragmentTransaction arg1) {
				
				mViewPager.setCurrentItem(tab.getPosition());
			}
        };
*/        
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
        	@Override
            public void onPageSelected(int position) {
                // When swiping between pages, select the
                // corresponding tab.
        		//((ActionBarActivity)getActivity()).getSupportActionBar().setSelectedNavigationItem(position);
            }
        });

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 3; i++) {
            
        	switch(i){
			case 0:
        	actionBar.addTab(actionBar.newTab()
                        .setText(tabNames[0])
                        .setTabListener(tabListener));
        	break;
			case 1:
		    	actionBar.addTab(actionBar.newTab()
                        .setText(tabNames[1])
                        .setTabListener(tabListener));
		    	break;
			case 2:
		    	actionBar.addTab(actionBar.newTab()
                        .setText(tabNames[2])
                        .setTabListener(tabListener));
		    	break;
        	}
        }
        
        if(savedInstanceState != null) {
            int index = savedInstanceState.getInt("index");
           // System.out.println("Index of the selected tab:"+index);
          //  ((ActionBarActivity)getActivity()).getSupportActionBar().setSelectedNavigationItem(index);
            mViewPager.setCurrentItem(index);
        }
		return rootView;
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		
		int i =mViewPager.getCurrentItem();
		//System.out.println("Index of the selected tab:"+i);
	    outState.putInt("index", i);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	
	
}
