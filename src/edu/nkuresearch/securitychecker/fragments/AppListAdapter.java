package edu.nkuresearch.securitychecker.fragments;

import java.util.TreeSet;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import edu.nkuresearch.securitychecker.R;

public class AppListAdapter implements ListAdapter{
	
	private PackageInfo[] apps;
	private LayoutInflater mInflater;
	private PackageManager mPackMan;
	
	public AppListAdapter(TreeSet<PackageInfo> appinstall, LayoutInflater inflater, PackageManager packageManager){
		apps = appinstall.toArray(new PackageInfo[appinstall.size()]);
		mInflater = inflater;
		mPackMan = packageManager;
	}
	
	@Override
	public int getCount() {
		return (apps == null) ? 0 : apps.length;
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
		ApplicationInfo ai = apps[position].applicationInfo;
		String[] perms = apps[position].requestedPermissions;
		int count = perms != null ? perms.length : 0;
		if(ai != null){
			((ImageView)convertView.findViewById(R.id.icon)).setImageDrawable(ai.loadIcon(mPackMan));
			((TextView)convertView.findViewById(R.id.appName)).setText(ai.loadLabel(mPackMan));
			((TextView)convertView.findViewById(R.id.appPermCount)).setText("(" + count + ")");
		}
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
		return (apps != null) ? apps.length == 0 : true;
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
