package com.clinicas;


import com.clinicas.fragments.MainNewsFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainFragmentAdapter extends FragmentPagerAdapter {

	public MainFragmentAdapter(FragmentManager fm) {
		super(fm);
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
        // Locale l = Locale.getDefault();
         switch (position) {
         case 0:
             return "Latest";
         case 1:
             return "Top";
         case 2:
             return "Favorites";
         }
         return null;
     }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;
	}

}
