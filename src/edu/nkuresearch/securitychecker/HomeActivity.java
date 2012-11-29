package edu.nkuresearch.securitychecker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import android.os.Bundle;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.view.Window;
import com.stericson.RootTools.Command;
import com.stericson.RootTools.RootTools;

import edu.nkuresearch.securitychecker.fragments.AppListFrag;
import edu.nkuresearch.securitychecker.fragments.InstallObserverFrag;
import edu.nkuresearch.securitychecker.fragments.InstallReviewFrag;
import edu.nkuresearch.securitychecker.fragments.PermSearchFrag;
import edu.nkuresearch.securitychecker.fragments.RunObserver;

public class HomeActivity extends BaseActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
				
		if(RootTools.isAccessGiven()){
		}
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Chooser");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		Tab tab = actionBar.newTab().setText("Apps").setTabListener(new HomeTabListener<AppListFrag>(this, "Apps", AppListFrag.class));
		actionBar.addTab(tab);
		tab = actionBar.newTab().setText("Permissions").setTabListener(new HomeTabListener<PermSearchFrag>(this, "Perms", PermSearchFrag.class));
		actionBar.addTab(tab);
		tab = actionBar.newTab().setText("Observer").setTabListener(new HomeTabListener<InstallObserverFrag>(this, "Observer", InstallObserverFrag.class));
		actionBar.addTab(tab);
		
		Tab straceTab = actionBar.newTab().setText("Result").setTabListener(new HomeTabListener<InstallReviewFrag>(this, "Result", InstallReviewFrag.class));
		actionBar.addTab(straceTab);
		
		tab = actionBar.newTab().setText("App Run").setTabListener(new HomeTabListener<RunObserver>(this, "App Run", RunObserver.class));
		actionBar.addTab(tab);
		
		if(getIntent().getStringExtra("STRACE") != null)
			actionBar.selectTab(straceTab);
	}
	
	public void startProgress() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				setSupportProgressBarIndeterminate(true);
				setSupportProgressBarIndeterminateVisibility(true);
			}

		});
	}
	
	public void stopProgress() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				setSupportProgressBarIndeterminateVisibility(false);
			}

		});
	}
	
	@Override
	protected void onResume() {
		if(RunObserver.APP_RUN_STRACE){
			RunObserver.APP_RUN_STRACE = false;
			killStrace();
		}
		super.onResume();
	}
	
	private boolean killStrace(){
		
		Command com = new Command(0, 0, null) {
			
			@Override
			public void output(int id, String line) {
				
			}
		};
		
		//creating variables
		boolean running = false;
		String pid = "";
		String line;
		Process kill;
		try {
			
			//get executing processes with the name strace
			kill = Runtime.getRuntime().exec( "ps strace" );
			
			//read the stream for the strace process
			DataInputStream dIn = new DataInputStream( kill.getInputStream() );
			try {
				
				//wait for kill process to end
				kill.waitFor();
				
				//make sure process wasn't terminated
				if( kill.exitValue() != 255 ){
					try {
						
						//read all lines from stream
						while( (line = dIn.readLine()) != null ){
							
							//check if strace exists
							if( line.matches( ".*strace" )){
								
								//get processID
								String[] getPid = line.split( "\\s+" );
								pid = "" + getPid[1];
								
								//make sure we can get a process number
								if( isPID( pid ) ){
									
									//kill the strace process
									Process pr = Runtime.getRuntime().exec( "su" );
									DataOutputStream dOut = new DataOutputStream( pr.getOutputStream() );
					    			dOut.writeBytes( "kill " + pid );
					    			dOut.flush();
					    			dOut.close();
					    			
					    			//set found flag to true
					    			running = true;
								}
							}
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//return if found or not
		return running;
	}

	//check that input is a number
	private boolean isPID(String pid) {
	  	try {
	  		//validate number
	  		Integer.parseInt(pid);
	  		return true;
	  	}
	  	
	  	//not a number
	  	catch (NumberFormatException e) {
	  		// s is not numeric
	  		return false;
	  	}
	}
}
