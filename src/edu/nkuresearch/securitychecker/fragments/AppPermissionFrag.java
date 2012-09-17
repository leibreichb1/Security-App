package edu.nkuresearch.securitychecker.fragments;

import java.util.List;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

public class AppPermissionFrag extends SherlockFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//View v = inflater.inflate(R.layout.ds, container, false);
		PackageManager p = getSherlockActivity().getPackageManager(); 
        final List<PackageInfo> appinstall = p.getInstalledPackages(PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS);
        for(PackageInfo info : appinstall){
        	Log.d("APP", "APP: " + info.packageName);
        	String[] perms = info.requestedPermissions;
        	if(perms != null){
	        	for(int i = 0; i < perms.length; i++){
	        		Log.d("PERMS", "PERM: " + perms[i]);
	        	}
        	}
        }
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
