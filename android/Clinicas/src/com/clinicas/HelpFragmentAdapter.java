package com.clinicas;


import com.clinicas.fragments.HelpFaqFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HelpFragmentAdapter extends FragmentPagerAdapter {
	Context context;
	public HelpFragmentAdapter(FragmentManager fm, Context _c) {
		super(fm);
		context = _c;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		Bundle args = new Bundle();
		HelpFaqFragment f = new HelpFaqFragment();
		switch (index) {
        case 0:
        	args.putString("type", "about_us");
            f.setArguments(args);
            return f;
        case 1:
        	args.putString("type", "faq");
            f.setArguments(args);
            return f;
        
        }
		return null;
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
       // Locale l = Locale.getDefault();
        switch (position) {
        case 0:
            return  context.getResources().getString(R.string.about);
        case 1:
            return context.getResources().getString(R.string.faqs);
        
        }
        return null;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
