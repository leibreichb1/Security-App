package edu.nkuresearch.securitychecker;


import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import edu.nkuresearch.securitychecker.fragments.AppListFrag;
import edu.nkuresearch.securitychecker.fragments.AppPermFrag;

public class PermActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		PackageInfo packIn = (PackageInfo) getIntent().getParcelableExtra(AppListFrag.PACK_INFO);
		getSupportActionBar().setTitle(packIn.applicationInfo.loadLabel(getPackageManager()));
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment listFrag = new AppPermFrag();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(android.R.id.content, listFrag).commit();
	}

}
