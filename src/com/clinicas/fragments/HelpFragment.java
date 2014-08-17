package com.clinicas.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinicas.HelpFragmentAdapter;
import com.clinicas.R;

public class HelpFragment extends Fragment {
	ViewPager mViewPager;
	 HelpFragmentAdapter fragAdapter;
	 	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.help_fragment, container, false);
		
		mViewPager = (ViewPager) rootView.findViewById(R.id.help_pager);
        fragAdapter = new HelpFragmentAdapter(getChildFragmentManager());
        
        mViewPager.setAdapter(fragAdapter);
        
        
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
        	@Override
            public void onPageSelected(int position) {
                // When swiping between pages, select the
                // corresponding tab.
        		((ActionBarActivity)getActivity()).getSupportActionBar().setSelectedNavigationItem(position);
            }
        });

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 2; i++) {
            
        	switch(i){
			case 0:
        	actionBar.addTab(actionBar.newTab()
                        .setText(getResources().getString(R.string.about))
                        .setTabListener(tabListener));
        	break;
			case 1:
		    	actionBar.addTab(actionBar.newTab()
                        .setText(getResources().getString(R.string.faqs))
                        .setTabListener(tabListener));
		    	break;
			
        	}
        }
		return rootView;
	}
	@Override
	public void onDestroyView() {
		
		super.onDestroyView();
	}
	
	
	
	

}
