package edu.nkuresearch.securitychecker.fragments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.stericson.RootTools.RootTools;

import edu.nkuresearch.securitychecker.R;

public class InstallObserverFrag extends SherlockFragment{
	
	//global variables
	Activity mActivity;
	Process strace;
	File file;
	File readFile;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.install_observe_frag, container, false);
		mActivity = getSherlockActivity();
		
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
	        file = new File( mActivity.getFilesDir(), "strace.sh" );
	        readFile = new File( mActivity.getFilesDir(), "strace.txt" );
	        
	        Button startBtn = (Button)rootView.findViewById(R.id.button1);
	        startBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onStartBtn();	
				}
			});
	        
	        Button stopBtn = (Button)rootView.findViewById(R.id.button2);
	        stopBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onCancelStrace();
				}
			});
	        
	        Button managerBtn = (Button)rootView.findViewById(R.id.button3);
	        managerBtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadManager();	
				}
			});
		}
		else{
			AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
			builder.setTitle("Root Required");
			builder.setMessage("Root permission required");
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					ActionBar bar = getSherlockActivity().getSupportActionBar(); 
					bar.selectTab(bar.getTabAt(0));
				}
			});
			builder.create().show();
		}
		return rootView;
	}
    
    //Method for when the start button was clicked
    public void onStartBtn(){
    	
    	//delete the stracefile if it exists
    	readFile.delete();
		
    	//create variables
    	String pid = "";
        String line = "";
    	try {
    		
    		//get installd daemon
    		Process ps = Runtime.getRuntime().exec( "ps" );
			DataInputStream dIn = new DataInputStream( ps.getInputStream() );
			try {
				
				//wait for the process to complete
				ps.waitFor();
				
				//make sure process didn't exit badly
				if( ps.exitValue() != 255 ){
					
					//read lines from the process stream
					while( (line = dIn.readLine()) != null ){
						
						//check and find the installd daemon
						if( line.matches( ".*installd" )){
		            		String[] getPid = line.split( "\\s+" );
							pid = "" + getPid[1];
						}
					}
				}
				
				//no installd daemon exists
				else{
					pid = "NO installd";
				}
				
				//display what the daemon's ID is
				TextView tv = (TextView) rootView.findViewById( R.id.tv );
	            tv.setText( pid );
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
    	
    	//validate that the pid is numeric
    	if( Utils.isPID( pid ) ){
            try {
            	
            	//open a writer to write the strace.sh file
				BufferedWriter bw = new BufferedWriter( new FileWriter( file ));
				bw.write( "#!/system/bin/sh\n\n" );
				bw.write( "strace -f -p " + pid + " -o " + readFile.getAbsolutePath() + "\n" );
				bw.close();
				
				//make file executable
				file.setExecutable( true );
			} 
            catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            try{
            	
            	//get root access and run the strace.sh file we created
    			strace = Runtime.getRuntime().exec( "su" );
    			DataOutputStream dOut = new DataOutputStream( strace.getOutputStream() );
    			dOut.writeBytes( file.getAbsolutePath() );
    			dOut.flush();
    			dOut.close();
    		}
    		catch( IOException e ){
    			e.printStackTrace();
    		}
        }
    }
    
    //method for when canceling strace
    public void onCancelStrace(){
		
    	//create the variables
    	String pid = "";
		String line;
		Process kill;
		
		try {
			//get the process id of strace
			kill = Runtime.getRuntime().exec( "ps" );
			
			//get the stream for reading
			BufferedReader dIn = new BufferedReader(new InputStreamReader( kill.getInputStream()));
			try {
				
				//wait for process to finish
				kill.waitFor();
				
				//verify that the process wasn't terminated
				if( kill.exitValue() != 255 ){
					try {
						//read the lines from the stream
						while( (line = dIn.readLine()) != null ){
							
							//check if strace is running
							if( line.matches( ".*strace" )){
								
								//split line and get process id for strace
								String[] getPid = line.split( "\\s+" );
								pid = "" + getPid[1];
								
								//check the the PID is valid and kill it
								if( Utils.isPID( pid ) ){
									
									//kill strace
									Process pr = Runtime.getRuntime().exec( "su" );
									DataOutputStream dOut = new DataOutputStream( pr.getOutputStream() );
					    			dOut.writeBytes( "kill " + pid );
					    			dOut.flush();
					    			dOut.close();
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
		
		//delete file
		boolean deleted = readFile.delete();
		
		//update screen to show if file and process were terminated
		TextView tv = (TextView) rootView.findViewById( R.id.tv );
		tv.setText( "" + deleted );
   }
    
    public void loadManager(){
    	Intent startManager = new Intent( android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS );
    	startActivity( startManager );
    }
}
