package edu.nkuresearch.securitychecker;


import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import edu.nkuresearch.securitychecker.fragments.AppListFrag;
import edu.nkuresearch.securitychecker.fragments.AppPermFrag;
import edu.nkuresearch.securitychecker.fragments.PermissionDescriptionFrag;

public class PermActivity extends SherlockFragmentActivity {
	
	
	
	TextView permTitle;
	TextView permDesc;
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.permissions_main);
		
		permTitle = (TextView) findViewById(R.id.permTitle);
		permDesc = (TextView) findViewById(R.id.permDesc);
		
		if( getIntent().hasExtra("PACK_INFO")){
			permTitle.setText(getIntent().getStringExtra("TITLE"));
			PackageInfo packIn = (PackageInfo) getIntent().getParcelableExtra(AppListFrag.PACK_INFO);
			getSupportActionBar().setTitle(packIn.applicationInfo.loadLabel(getPackageManager()));
			
			FragmentManager fm = getSupportFragmentManager();
			Fragment listFrag = new AppPermFrag();
			Fragment descFrag = new PermissionDescriptionFrag();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(android.R.id.content, listFrag).commit();
		}
	}
	
//	@Override
//	protected void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.app_list);
//		
//		PackageInfo packIn = (PackageInfo) getIntent().getParcelableExtra(AppListFrag.PACK_INFO);
//		Log.v("Title -> ", packIn.applicationInfo.loadLabel(getPackageManager())+"");
//		getSupportActionBar().setTitle(packIn.applicationInfo.loadLabel(getPackageManager()));
//		
//		FragmentManager fm = getSupportFragmentManager();
//		Fragment listFrag = new AppPermFrag();
//		FragmentTransaction ft = fm.beginTransaction();
//		ft.replace(android.R.id.content, listFrag).commit();
//	}
//	
//	public void startRightFrag(String title){
//		//this will do map.getTitle(title);
//		//launch the right frag.
//	}

}
