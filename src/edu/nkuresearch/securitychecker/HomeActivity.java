package edu.nkuresearch.securitychecker;

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
			Command com = new Command(0, "ls /") {
				
				@Override
				public void output(int id, String line) {
					Log.d("LS", line);
				}
			};
			try {
				RootTools.getShell(true).add(com).waitForFinish();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TimeoutException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
}
