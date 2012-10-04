package edu.nkuresearch.securitychecker;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import edu.nkuresearch.securitychecker.fragments.SearhResultFrag;

public class SearchResultActivity extends SherlockFragmentActivity{
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.search_res);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment leftFrag = new SearhResultFrag();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.search_list_frag, leftFrag).commit();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getPerms(){
		return ((ArrayList<String>) getIntent().getSerializableExtra("LIST"));
	}
}
