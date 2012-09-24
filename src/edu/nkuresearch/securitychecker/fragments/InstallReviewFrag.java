package edu.nkuresearch.securitychecker.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import edu.nkuresearch.securitychecker.R;

public class InstallReviewFrag extends SherlockFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.metrics, container, false);
		String text = null;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File( Environment.getExternalStorageDirectory(), "parsed.txt" )));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//get parsed text and set it into the textview
        TextView tv = (TextView) v.findViewById( R.id.straceView );
        if( text != null )
        	tv.setText( text );
    	else
    		tv.setText( "No Reviewed App" );
		return v;
	}
}
