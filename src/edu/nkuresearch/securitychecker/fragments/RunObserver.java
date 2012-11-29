package edu.nkuresearch.securitychecker.fragments;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.stericson.RootTools.CommandCapture;
import com.stericson.RootTools.RootTools;

import edu.nkuresearch.securitychecker.AppListActivity;
import edu.nkuresearch.securitychecker.HomeActivity;
import edu.nkuresearch.securitychecker.R;
import edu.nkuresearch.securitychecker.SearchResultActivity;
import edu.nkuresearch.securitychecker.fragments.Utils.PackInCompWord;

public class RunObserver extends SherlockFragment implements OnItemClickListener{
	
	private TreeSet<PackageInfo> appinstall;
	PackageInfo[] apps;
	private LayoutInflater mInflater;
	private ListView mListView;
	private Button mByWord;
	private Button mByAsc;
	private Button mByDsc;
	private PackageManager mPackMan;
	private Activity mActivity;
	private File file;
	private File readFile;
	
	public static boolean APP_RUN_STRACE = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mActivity = getSherlockActivity();
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
			apps = appinstall.toArray(new PackageInfo[appinstall.size()]);
			mListView.setAdapter(new Utils.AppListAdapter(appinstall, mInflater, mPackMan));
			if(appinstall == null || appinstall.size() == 0){
				mListView.setVisibility(View.GONE);
				((TextView)v.findViewById(R.id.emptyText)).setVisibility(View.VISIBLE);
			}
		}
		if(RootTools.isAccessGiven()){
	        //copy over strace executables to files dir for running
	        AssetManager am = mActivity.getAssets();
	        try {
	        	File straceFile = new File(mActivity.getFilesDir() + "/strace");
	        	if(!straceFile.exists()){
	        		InputStream is = am.open("strace");
					OutputStream out = mActivity.openFileOutput("strace", mActivity.MODE_WORLD_READABLE);
					byte[] buffer = new byte[1024];
					int count = 0;
					while((count = is.read(buffer)) > 0){
						out.write(buffer, 0, count);
					}
					straceFile.setExecutable(true);
					is.close();
					out.close();
	        	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        //create the files
	        file = new File( mActivity.getFilesDir(), "straceRun.sh" );
	        readFile = new File( Environment.getExternalStorageDirectory(), "straceRun.txt" );
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
			builder.setTitle("Root Required");
			builder.setMessage("Root permission required");
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					ActionBar bar = getSherlockActivity().getSupportActionBar(); 
					bar.selectTab(bar.getTabAt(0));
				}
			});
			builder.create().show();
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
			apps = appinstall.toArray(new PackageInfo[appinstall.size()]);
			mListView.setAdapter(new Utils.AppListAdapter(appinstall, mInflater, mPackMan));
			if(!(getSherlockActivity() instanceof SearchResultActivity) && !(getSherlockActivity() instanceof AppListActivity)){
				mByWord.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TreeSet<PackageInfo> tempTree = new TreeSet<PackageInfo>(new Utils.PackInCompWord(getSherlockActivity().getPackageManager()));
						tempTree.addAll(appinstall);
						appinstall = tempTree;
						apps = appinstall.toArray(new PackageInfo[appinstall.size()]);
						mListView.setAdapter(new Utils.AppListAdapter(appinstall, mInflater, mPackMan));
					}
				});
				mByAsc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TreeSet<PackageInfo> tempTree;
						tempTree = new TreeSet<PackageInfo>(new Utils.PackInCompNumAsc(getSherlockActivity().getPackageManager()));
						tempTree.addAll(appinstall);
						appinstall = tempTree;
						apps = appinstall.toArray(new PackageInfo[appinstall.size()]);
						mListView.setAdapter(new Utils.AppListAdapter(appinstall, mInflater, mPackMan));
					}
				});
				mByDsc.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TreeSet<PackageInfo> tempTree;
						tempTree = new TreeSet<PackageInfo>(new Utils.PackInCompNumDes(getSherlockActivity().getPackageManager()));
						tempTree.addAll(appinstall);
						appinstall = tempTree;
						apps = appinstall.toArray(new PackageInfo[appinstall.size()]);
						mListView.setAdapter(new Utils.AppListAdapter(appinstall, mInflater, mPackMan));
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
	public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
		Toast.makeText(getSherlockActivity(), "TEST", Toast.LENGTH_LONG).show();
		
		String pid = "NA";
		
		//delete the stracefile if it exists
    	readFile.delete();
    	
        String line = "";
    	pid = runPS(position);
    	//validate that the pid is numeric
    	if( Utils.isPID( pid ) ){
    		Log.d("PID", pid + " first");
        	APP_RUN_STRACE = true;
//             	CommandCapture command = new CommandCapture(0, "strace -f -p " + pid + " -o " + readFile.getAbsolutePath());
//             	RootTools.getShell(true).add(command).waitForFinish();
         	String packName = apps[position].packageName;
    		Intent intent = mPackMan.getLaunchIntentForPackage(packName);
    		final String finPid = pid;
    		if(intent != null){
    			Thread tr = new Thread(new Runnable(){

					@Override
					public void run() {
	    				APP_RUN_STRACE = true;
	    				CommandCapture command = new CommandCapture(0, "strace -f -p " + finPid + " -o " + readFile.getAbsolutePath());
	                 	try {
							RootTools.getShell(true).add(command);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
    			});
    			startActivity(intent);
    			tr.start();
    		}
        }
    	else{
    		Intent intent = null;
    		String packName = apps[position].packageName;
    		intent = mPackMan.getLaunchIntentForPackage(packName);
    		if(intent != null){
    			Thread tr = new Thread(new Runnable(){

					@Override
					public void run() {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						final String finPid = runPS(position);
						Log.d("PID", finPid + " Thread");
		    			if( Utils.isPID( finPid ) ){
		    				APP_RUN_STRACE = true;
		    				CommandCapture command = new CommandCapture(0, "strace -f -p " + finPid + " -o " + readFile.getAbsolutePath());
		                 	try {
								RootTools.getShell(true).add(command).waitForFinish();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (TimeoutException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		    			}
					}
    				
    			});
    			startActivity(intent);
    			tr.start();
    		}
    	}
	}
	
	private String runPS(int position){
		String pid = "NA";
		try{
			//get installd daemon
			Process ps = Runtime.getRuntime().exec( "ps" );
			DataInputStream dIn = new DataInputStream( ps.getInputStream() );
			try {
				
				//wait for the process to complete
				ps.waitFor();
				
				//make sure process didn't exit badly
				if( ps.exitValue() != 255 ){
					
					//read lines from the process stream
					String line;
					while( (line = dIn.readLine()) != null ){
						
						String packName = apps[position].packageName.replace(".", "\\.");
						//check and find the installd daemon
						//Log.d("PACK", packName);
						if( line.matches( ".*" + packName + ".*" )){
		            		String[] getPid = line.split( "\\s+" );
							pid = "" + getPid[1];
							Log.d("FOUND", packName);
						}
					}
				}
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//close the stream reader if it exists
			if( dIn != null)
				dIn.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pid;
	}
}
