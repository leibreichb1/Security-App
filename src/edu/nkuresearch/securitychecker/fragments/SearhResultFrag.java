package edu.nkuresearch.securitychecker.fragments;

import java.util.ArrayList;
import java.util.LinkedList;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import edu.nkuresearch.securitychecker.R;
import edu.nkuresearch.securitychecker.SearchResultActivity;

public class SearhResultFrag extends SherlockFragment{

	private ListView lv;
	private ArrayList<String> mPerms;
	private LayoutInflater mInflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mPerms = ((SearchResultActivity) getSherlockActivity()).getPerms();
		View v = inflater.inflate(R.layout.search_list, container, false);
		lv = (ListView) v.findViewById(R.id.searchList);
		lv.setAdapter(new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_list_item_1, mPerms));
		return v;
	}
}
