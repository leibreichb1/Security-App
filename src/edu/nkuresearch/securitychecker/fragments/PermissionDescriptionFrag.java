package edu.nkuresearch.securitychecker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import edu.nkuresearch.securitychecker.R;

public class PermissionDescriptionFrag extends SherlockFragment {
	
View vPermDesc;

 @Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	
	 	vPermDesc = inflater.inflate(R.layout.perm_description_frag, container, false);
	 
	 	return vPermDesc;
 	}
 
 
 
 
 
	
//	View permList;
//	TextView permTitle;
//	TextView permDesc;
//	@Override
//	public void onCreate(Bundle arg0) {
//		// TODO Auto-generated method stub
//		super.onCreate(arg0);
//		setContentView(R.layout.permissions_main);
//		
//		permTitle = (TextView) findViewById(R.id.permTitle);
//		permDesc = (TextView) findViewById(R.id.permDesc);
//		
//		if( getIntent().hasExtra("TITLE")){
//			permTitle.setText(getIntent().getStringExtra("TITLE"));
//		}
//	}
}