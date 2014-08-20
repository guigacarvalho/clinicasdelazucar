package com.clinicas;


import com.clinicas.fragments.MainNewsFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {
	Context context;
	public MainFragmentAdapter(FragmentManager fm, Context _c) {
		super(fm);
		context=_c;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		MainNewsFragment f = MainNewsFragment.newInstance();
        // Supply index input as an argument.
        Bundle args = new Bundle();
		switch (index) {
        case 0:
        	
        	
            args.putString("type", "latest");
            f.setArguments(args);
            break;
        case 1:

        	
            args.putString("type", "top");
            f.setArguments(args);
            break;
        case 2:

        	args.putString("type", "favorites");
            f.setArguments(args);
            break;

        }
		System.out.println("returning null");
        
		return f;
	}
	
	 @Override
     public CharSequence getPageTitle(int position) {
		 String[] tabNames=context.getResources().getStringArray(R.array.tab_names);
		
         return tabNames[position];
     }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
