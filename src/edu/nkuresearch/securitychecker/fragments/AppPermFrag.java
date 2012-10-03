package edu.nkuresearch.securitychecker.fragments;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import edu.nkuresearch.securitychecker.PermActivity;
import edu.nkuresearch.securitychecker.R;


public class AppPermFrag extends SherlockFragment implements OnItemClickListener{

	private String[] mPerms;
	private ListView mListView;
	private LayoutInflater mInflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		PackageInfo packIn = (PackageInfo) getSherlockActivity().getIntent().getParcelableExtra(AppListFrag.PACK_INFO);
		mInflater = inflater;
		mPerms = packIn.requestedPermissions;
		View v = inflater.inflate(R.layout.perm_list, container, false);
		mListView = (ListView) v.findViewById(R.id.permlist);
		mListView.setOnItemClickListener(this);
		if(mPerms != null)
			mListView.setAdapter(new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_list_item_1, mPerms));
		return v;
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		((PermActivity) getSherlockActivity()).startRightFrag(mPerms[position]);
	}

}
