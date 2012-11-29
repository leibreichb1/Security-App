package edu.nkuresearch.securitychecker.fragments;

import java.util.Comparator;
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

import com.actionbarsherlock.app.SherlockFragment;

import edu.nkuresearch.securitychecker.R;

public class Utils{

	public static class PackInCompWord implements Comparator<PackageInfo>{
		
		private PackageManager mPackMan;
		
		public PackInCompWord(PackageManager packMan){
			mPackMan = packMan;
		}
		
		@Override
		public int compare(PackageInfo lhs, PackageInfo rhs) {
			String lhsName = (String) lhs.applicationInfo.loadLabel(mPackMan);
			String rhsName = (String) rhs.applicationInfo.loadLabel(mPackMan);
			return lhsName.compareToIgnoreCase(rhsName);
		}
	}
	
	public static class PackInCompNumAsc implements Comparator<PackageInfo>{
		
		private PackageManager mPackMan;
		
		public PackInCompNumAsc(PackageManager packMan){
			mPackMan = packMan;
		}
		
		@Override
		public int compare(PackageInfo lhs, PackageInfo rhs) {
			String[] permsL = lhs.requestedPermissions;
			String[] permsR = rhs.requestedPermissions;
			if(permsL != null && permsR != null)
				if(permsL.length != permsR.length)
					return permsL.length - permsR.length;
				else{
					String lhsName = (String) lhs.applicationInfo.loadLabel(mPackMan);
					String rhsName = (String) rhs.applicationInfo.loadLabel(mPackMan);
					return lhsName.compareToIgnoreCase(rhsName);
				}			
			else if(permsL == null && permsR == null){
				String lhsName = (String) lhs.applicationInfo.loadLabel(mPackMan);
				String rhsName = (String) rhs.applicationInfo.loadLabel(mPackMan);
				return lhsName.compareToIgnoreCase(rhsName);
			}
			else if(permsL == null)
				return -1;
			else
				return 1;
		}
	}
	
	public static class PackInCompNumDes implements Comparator<PackageInfo>{
	
		private PackageManager mPackMan;
	
		public PackInCompNumDes(PackageManager packMan){
			mPackMan = packMan;
		}
	
		@Override
		public int compare(PackageInfo lhs, PackageInfo rhs) {
			String[] permsL = lhs.requestedPermissions;
			String[] permsR = rhs.requestedPermissions;
			if(permsL != null && permsR != null){
				if(permsR.length != permsL.length)
					return permsR.length - permsL.length;
				else{
					String lhsName = (String) lhs.applicationInfo.loadLabel(mPackMan);
					String rhsName = (String) rhs.applicationInfo.loadLabel(mPackMan);
					return lhsName.compareToIgnoreCase(rhsName);
				}
			}
			else if(permsL == null && permsR == null){
				String lhsName = (String) lhs.applicationInfo.loadLabel(mPackMan);
				String rhsName = (String) rhs.applicationInfo.loadLabel(mPackMan);
				return lhsName.compareToIgnoreCase(rhsName);
			}
			else if(permsL == null)
				return 1;
			else
				return -1;
		}
	}
	
	public static class AppListAdapter implements ListAdapter{
		
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
	
	//check if the PID is numeric
   public static boolean isPID(String pid) {
	   	try {
	   		Integer.parseInt(pid);
	   		return true;
	   	}
	   	catch (NumberFormatException e) {
	   		// s is not numeric
	   		return false;
	   	}
	}
}
