package edu.nkuresearch.securitychecker.fragments;

import java.util.LinkedList;
import java.util.List;

import android.content.pm.ApplicationInfo;
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

public class AppPermissionFrag extends SherlockFragment implements OnItemClickListener{
	
	private List<PackageInfo> appinstall;
	private LayoutInflater mInflater;
	private ListView mListView;
	PackageManager mPackMan;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View v = inflater.inflate(R.layout.app_list, container, false);
		appinstall = new LinkedList<PackageInfo>();
		mPackMan = getSherlockActivity().getPackageManager(); 
        List<PackageInfo> tempList = mPackMan.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS);
        for( PackageInfo a : tempList) {
            if( ((String) a.applicationInfo.loadLabel( getActivity().getPackageManager())).matches( "^[^\\.]+$" ) ) {
                appinstall.add(a);
            }
        }
        	
        mListView = (ListView) v.findViewById(R.id.applist);
        mListView.setAdapter(new AppListAdapter());
		return v;
	}

	private class AppListAdapter implements ListAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return appinstall.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if( convertView == null)
				convertView = mInflater.inflate(R.layout.applistitem, null, false);
			ApplicationInfo ai = appinstall.get(position).applicationInfo;
			((ImageView)convertView.findViewById(R.id.icon)).setImageDrawable(ai.loadIcon(mPackMan));
			((TextView)convertView.findViewById(R.id.appName)).setText(ai.loadLabel(mPackMan));
			return convertView;

		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return appinstall.isEmpty();
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return true;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		Log.v("APP NAME: ", ""+appinstall.get(position).packageName);
		
	}
	
}

