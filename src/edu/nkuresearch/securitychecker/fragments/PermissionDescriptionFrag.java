package edu.nkuresearch.securitychecker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import edu.nkuresearch.securitychecker.PermActivity;
import edu.nkuresearch.securitychecker.R;

public class PermissionDescriptionFrag extends SherlockFragment {
	
	View vPermDesc;
	
	 @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	
	 	vPermDesc = inflater.inflate(R.layout.perm_description_frag, container, false);
	 	TextView tv = (TextView) vPermDesc.findViewById(R.id.permDesc);
	 	tv.setText(((PermActivity) getSherlockActivity()).getDesc());
	 	return vPermDesc;
 	}
}