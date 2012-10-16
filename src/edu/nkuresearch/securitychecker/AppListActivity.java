package edu.nkuresearch.securitychecker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import edu.nkuresearch.securitychecker.fragments.AppListFrag;

public class AppListActivity extends SherlockFragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perm_layout);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment listFrag = new AppListFrag();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.list_frag, listFrag).commit();
	}
}
