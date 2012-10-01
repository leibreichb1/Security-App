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
		View v = inflater.inflate(R.layout.app_list, container, false);
		mListView = (ListView) v.findViewById(R.id.applist);
		mListView.setOnItemClickListener(this);
		if(mPerms != null)
			mListView.setAdapter(new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_list_item_1, mPerms));
		return v;
		
	}
	
//	private class AppListAdapter implements ListAdapter{
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return mPerms.length;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return position;
//		}
//
//		@Override
//		public int getItemViewType(int position) {
//			// TODO Auto-generated method stub
//			return 1;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			
//			if( convertView == null)
//				convertView = mInflater.inflate(android.R.layout.simple_list_item_1, null, false);
//			convertView.findViewById(android.R.id.s)
//		
//			return convertView;
//
//		}
//
//		@Override
//		public int getViewTypeCount() {
//			// TODO Auto-generated method stub
//			return 1;
//		}
//
//		@Override
//		public boolean hasStableIds() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isEmpty() {
//			// TODO Auto-generated method stub
//			return mPerms.length == 0 ? true : false;
//		}
//
//		@Override
//		public void registerDataSetObserver(DataSetObserver observer) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void unregisterDataSetObserver(DataSetObserver observer) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public boolean areAllItemsEnabled() {
//			// TODO Auto-generated method stub
//			return false;
//		}
//
//		@Override
//		public boolean isEnabled(int position) {
//			// TODO Auto-generated method stub
//			return true;
//		}
//	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
	}

}
