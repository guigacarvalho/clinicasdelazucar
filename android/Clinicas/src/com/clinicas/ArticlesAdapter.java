package com.clinicas;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;

import com.clinicas.fragments.MainNewsFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArticlesAdapter extends BaseAdapter {

	
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    ArrayList<HashMap<String, String>> categoriesMapArray;
    
    
	public ArticlesAdapter(Activity activity,
			ArrayList<HashMap<String, String>> data) {
		super();
		this.activity = activity;
		this.data = data;
		inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		categoriesMapArray = ((MainActivity)activity).getCategoriesMapArray();
	}

	
	public void setData(ArrayList<HashMap<String, String>> data) {
		this.data = data;
	}


	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		View v = convertView;
		if(convertView==null)
			v = inflater.inflate(R.layout.article_list_item, null);
		
		TextView title = (TextView)v.findViewById(R.id.article_title);
		TextView date = (TextView)v.findViewById(R.id.article_date);
		TextView category = (TextView)v.findViewById(R.id.article_category);
		TextView text = (TextView)v.findViewById(R.id.article_text);
		ImageView img = (ImageView)v.findViewById(R.id.article_image);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		DisplayImageOptions options = new DisplayImageOptions.Builder()
						.resetViewBeforeLoading(true)
						.showImageForEmptyUri(R.drawable.logo_clinicas)
						.showImageOnFail(R.drawable.logo_clinicas)
						.showImageOnLoading(R.drawable.logo_clinicas)
						.cacheOnDisk(true)
						.imageScaleType(ImageScaleType.EXACTLY)
						.displayer(new RoundedBitmapDisplayer (20)).build();
		HashMap<String, String> article = new HashMap<String, String>();
		article = data.get(position);
		imageLoader.displayImage(article.get(MainNewsFragment.KEY_PIC), img, options);
		title.setText(article.get(MainNewsFragment.KEY_TITLE));
		date.setText(article.get(MainNewsFragment.KEY_DATE));
		for (HashMap<String, String> map : categoriesMapArray) {
			
			if(map.get(MainNewsFragment.KEY_CAT).equals(article.get(MainNewsFragment.KEY_CAT))){
				category.setText(map.get(MainNewsFragment.KEY_TITLE));
				//System.out.println("category="+map.get(MainNewsFragment.KEY_TITLE));
				break;
			}
		}
		
		text.setText(Jsoup.parse(article.get(MainNewsFragment.KEY_TEXT).substring(0, 30)).text());
		return v;
	}

}
