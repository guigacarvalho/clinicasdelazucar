package com.clinicas;

import java.util.HashMap;
import java.util.List;



import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class DrawerListAdapter extends BaseExpandableListAdapter {

	Context context;
	List <String> groupHeader;
	HashMap<String, List<String>> groupChildren;
	
	
	public DrawerListAdapter(Context context, List<String> groupHeader,
			HashMap<String, List<String>> groupChildren) {
		super();
		this.context = context;
		this.groupHeader = groupHeader;
		this.groupChildren = groupChildren;
	}

	@Override
	public Object getChild(int grpPos, int childPos) {
		return this.groupChildren.get(this.groupHeader.get(grpPos)).get(childPos);
	}

	@Override
	public long getChildId(int grpPos, int childPos) {
		return childPos;
	}

	@Override
	public View getChildView(int grpPos, int childPos, boolean isLastChild, View convertView,
			ViewGroup parentView) {
		
		
		final String childText = (String) getChild(grpPos, childPos);
		 
		if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_item, null);
        }
 
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.article_title);
 
        txtListChild.setText(childText);
        return convertView;
	}

	@Override
	public int getChildrenCount(int grpPos) {
		return this.groupChildren.get(this.groupHeader.get(grpPos))
                .size();
	}

	@Override
	public Object getGroup(int  grpPos) {
		return this.groupHeader.get(grpPos);
	}

	@Override
	public int getGroupCount() {
		return this.groupHeader.size();
	}

	@Override
	public long getGroupId(int grpPos) {
		return grpPos;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.drawer_list_item, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.article_title);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}
}
