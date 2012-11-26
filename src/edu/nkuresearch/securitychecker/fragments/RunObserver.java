package edu.nkuresearch.securitychecker.fragments;

import java.util.List;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import edu.nkuresearch.securitychecker.AppListActivity;
import edu.nkuresearch.securitychecker.HomeActivity;
import edu.nkuresearch.securitychecker.PermActivity;
import edu.nkuresearch.securitychecker.R;
import edu.nkuresearch.securitychecker.SearchResultActivity;
import edu.nkuresearch.securitychecker.fragments.AppListFrag.ListApps;
import edu.nkuresearch.securitychecker.fragments.Utils.PackInCompWord;

public class RunObserver extends SherlockFragment implements OnItemClickListener{
	
	private TreeSet<PackageInfo> appinstall;
	private LayoutInflater mInflater;
	private ListView mListView;
	private Button mByWord;
	private Button mByAsc;
	private Button mByDsc;
	private PackageManager mPackMan;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mInflater = inflater;
		View v = inflater.inflate(R.layout.app_list, container, false);
		mListView = (ListView) v.findViewById(R.id.applist);
		mPackMan = getSherlockActivity().getPackageManager();
		Activity activ = getSherlockActivity();
		if(!(activ instanceof SearchResultActivity) && !(activ instanceof AppListActivity)){
			mByWord = (Button) v.findViewById(R.id.wordBtn);
			mByAsc = (Button) v.findViewById(R.id.numAsc);
			mByDsc = (Button) v.findViewById(R.id.numDsc);
			appinstall = new TreeSet<PackageInfo>(new PackInCompWord(mPackMan));
			((HomeActivity) getSherlockActivity()).startProgress();
			new ListApps().execute((Void) null);
		}
		else{
			LinearLayout ll = (LinearLayout) v.findViewById(R.id.buttons);
			ll.setVisibility(View.GONE);
			if(activ instanceof SearchResultActivity)
				appinstall = ((SearchResultActivity) activ).getApps();
			else
				appinstall = (TreeSet<PackageInfo>) activ.getIntent().getSerializableExtra("APPS");
			mListView.setAdapter(new AppListAdapter(appinstall, mInflater, mPackMan));
			if(appinstall == null || appinstall.size() == 0){
				mListView.setVisibility(View.GONE);
				((TextView)v.findViewById(R.id.emptyText)).setVisibility(View.VISIBLE);
			}
		}
		return v;
	}

	class ListApps extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) { 
	        List<PackageInfo> tempList = mPackMan.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS);
	        for( PackageInfo a : tempList) {
	            if( getSherlockActivity() != null && ((String) a.applicationInfo.loadLabel( getSherlockActivity().getPackageManager())).matches( "^[^\\.]+$" ) ) {
	            	if(!appinstall.contains(a))
	            		appinstall.add(a);
	            }
	        }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			mListView.setAdapter(new AppListAdapter(appinstall, mInflater, mPackMan));
			if(!(getSherlockActivity() instanceof SearchResultActivity) && !(getSherlockActivity() instanceof AppListActivity)){
				mByWord.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TreeSet<PackageInfo> tempTree = new TreeSet<PackageInfo>(new Utils.PackInCompWord(getSherlockActivity().getPackageManager()));
						tempTree.addAll(appinstall);
						appinstall = tempTree;
						mListView.setAdapter(new AppListAdapter(appinstall, mInflater, mPackMan));
					}
				});
				mByAsc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TreeSet<PackageInfo> tempTree;
						tempTree = new TreeSet<PackageInfo>(new Utils.PackInCompNumAsc(getSherlockActivity().getPackageManager()));
						tempTree.addAll(appinstall);
						appinstall = tempTree;
						mListView.setAdapter(new AppListAdapter(appinstall, mInflater, mPackMan));
					}
				});
				mByDsc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TreeSet<PackageInfo> tempTree;
						tempTree = new TreeSet<PackageInfo>(new Utils.PackInCompNumDes(getSherlockActivity().getPackageManager()));
						tempTree.addAll(appinstall);
						appinstall = tempTree;
						mListView.setAdapter(new AppListAdapter(appinstall, mInflater, mPackMan));
					}
				});
			}
			mListView.setOnItemClickListener(RunObserver.this);
			if(getSherlockActivity() != null)
				((HomeActivity) getSherlockActivity()).stopProgress();
			super.onPostExecute(result);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Toast.makeText(getSherlockActivity(), "TEST", Toast.LENGTH_LONG).show();
	}
}
