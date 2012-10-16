package edu.nkuresearch.securitychecker.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import edu.nkuresearch.securitychecker.R;
import edu.nkuresearch.securitychecker.SearchResultActivity;

public class SearhResultFrag extends SherlockFragment implements OnItemClickListener{

	private ListView lv;
	private ArrayList<String> mPerms;
	HashMap<String, ArrayList<PackageInfo>> appMap;
	LayoutInflater mInflater;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mInflater = inflater;
		mPerms = ((SearchResultActivity) getSherlockActivity()).getPerms();
		View v = inflater.inflate(R.layout.search_list, container, false);
		lv = (ListView) v.findViewById(R.id.searchList);
		loadMap();
		lv.setAdapter(new permAdapter());
		lv.setOnItemClickListener(this);
		return v;
	}
	
	private void loadMap(){
		appMap = new HashMap<String, ArrayList<PackageInfo>>();
		PackageManager mPackMan = getSherlockActivity().getPackageManager(); 
        List<PackageInfo> tempList = mPackMan.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS);
        for(String perm : mPerms){
        	ArrayList<PackageInfo> foundApps = new ArrayList<PackageInfo>();
	        for(PackageInfo a : tempList) {
	            if(((String) a.applicationInfo.loadLabel( getActivity().getPackageManager())).matches( "^[^\\.]+$" )) {
	            	String[] appPerms = a.requestedPermissions;
	            	if(appPerms != null){
	            		boolean found = false;
	            		for(int i = 0; i < appPerms.length && !found; i++){
	            			if(appPerms[i].contains(perm)){
	            				Log.d("PERMS", appPerms[i].toString());
	            				foundApps.add(a);
	            				found = true;
	            			}
	            		}
	            	}
	            }
	        }
	        appMap.put(perm, foundApps);
            if(foundApps.size() > 0)
            	Log.d("PERMS", foundApps.toString());
        }
	}
	
	private class permAdapter implements ListAdapter{
		@Override
		public int getCount() {
			return mPerms.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if( convertView == null)
				convertView = mInflater.inflate(R.layout.applistitem, null, false);
			ArrayList<PackageInfo> foundApps = appMap.get(mPerms.get(position));
			((ImageView)convertView.findViewById(R.id.icon)).setVisibility(View.GONE);
			
			((TextView)convertView.findViewById(R.id.appName)).setText(mPerms.get(position) + " (" + foundApps.size() + ")");
			return convertView;

		}

		@Override
		public int getViewTypeCount() {
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isEmpty() {
			return mPerms.isEmpty();
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			
		}

		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return true;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ArrayList<PackageInfo> list = appMap.get(mPerms.get(position));
		((SearchResultActivity) getSherlockActivity()).setApps(list);
		((SearchResultActivity) getSherlockActivity()).startRightFrag();
	}
}
