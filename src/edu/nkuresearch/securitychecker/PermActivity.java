package edu.nkuresearch.securitychecker;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import edu.nkuresearch.securitychecker.fragments.AppListFrag;
import edu.nkuresearch.securitychecker.fragments.AppPermFrag;
import edu.nkuresearch.securitychecker.fragments.PermissionDescriptionFrag;

public class PermActivity extends SherlockFragmentActivity {
	
	private HashMap<String, String> perms;
	public String desc;
	
	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.perm_layout);
		
		perms = new HashMap<String, String>();
		if( getIntent().hasExtra("PACK_INFO")){
			PackageInfo packIn = (PackageInfo) getIntent().getParcelableExtra(AppListFrag.PACK_INFO);
			getSupportActionBar().setTitle(packIn.applicationInfo.loadLabel(getPackageManager()));
			
			FragmentManager fm = getSupportFragmentManager();
			Fragment listFrag = new AppPermFrag();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.list_frag, listFrag).commit();
		}
		
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(getAssets().open("permissionsList.txt")));
			String line;
			while((line = br.readLine()) != null){
				String[] splitStr = line.split(":-:");
				if(splitStr != null && splitStr.length > 1)
					perms.put(splitStr[0], splitStr[1]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startRightFrag(String title){
		String[] splitStr = title.split("\\.");
		if(splitStr.length > 0 && perms.containsKey(splitStr[splitStr.length - 1])){
			if(getResources().getBoolean(R.bool.IsTablet)){
				FragmentManager fm = getSupportFragmentManager();
				Fragment rightFrag = new PermissionDescriptionFrag();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.right_frag, rightFrag).commit();
			}
		}
		else
			Toast.makeText(this, "No Descrition Available", Toast.LENGTH_LONG).show();
	}
	
	public String getDesc(){
		return desc;
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
