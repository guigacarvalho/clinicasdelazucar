package com.clinicas.fragments;

import com.clinicas.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class HelpFaqFragment extends Fragment {
	static String aboutUs="Clinicas del Azucar is a venture-backed social enterprise (Sugar Clinics) founded by MIT and Harvard graduates experts in diabetes care and health technologies. After a long research of how diabetes care should be deliver in emerging markets, we returned to Mexico to implement our innovative model to bring diabetes care to those in need.<br/>With a comprehensive redesign of the care processes, use of cutting-edge technology, implantation of a sophisticated payment method and the creation of one-stop-shops, we reduced 70% the annual cost of care (From $1000USD to only $200USD). Clínicas del Azúcar addresses a critical socioeconomic issue, reduces inequality, and improves peoples’ quality of life.<br/>Our innovative approach  prevents more than 60% of the diabetes-related complications, like blindness, amputations or kidney failures. Impacting positively the lives of thousend of people in Mexico and soon in the entire world.";
	 static String faq = "How to find articles on diabetes?<br/>How to suggest an article?<br/>How to share an article?<br/>How to manage my user account?<br/>How to report a bug?";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.faq_about, null);
		WebView v = (WebView)rootView.findViewById(R.id.about_faq);
		Bundle args = getArguments();
		try{
			String type = args.getString("type");
		
		if(type.equals("about_us"))
			v.loadData(aboutUs, "text/html", null);
		else
			v.loadData(faq, "text/html", null);
		}
		catch(Exception e){
			
		}
		
		return rootView;
	}

	
}
