package com.clinicas;


import com.clinicas.fragments.MainNewsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HelpFragmentAdapter extends FragmentPagerAdapter {

	public HelpFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
        case 0:

            return new MainNewsFragment();
        case 1:

            return new MainNewsFragment();
        
        }
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
