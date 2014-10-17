package com.clinicas;


import com.clinicas.fragments.MainNewsFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {
	Context context;
	Boolean loggedIn;
	public MainFragmentAdapter(FragmentManager fm, Context _c, Boolean loggedIn) {
		super(fm);
		context=_c;
		this.loggedIn = loggedIn;
		
	}

	@Override
	public Fragment getItem(int index) {
		MainNewsFragment f = new MainNewsFragment();
		Bundle args = new Bundle();
		args.putInt("type", index);
        f.setArguments(args);
        return f;
        
	}
	
	 @Override
     public CharSequence getPageTitle(int position) {
		 String[] tabNames=context.getResources().getStringArray(R.array.tab_names);
		
         return tabNames[position];
     }

	@Override
	public int getCount() {
		if(loggedIn)
			return 3;
		
		return 2;
	}

	

	
}
