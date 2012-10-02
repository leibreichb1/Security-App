package edu.nkuresearch.securitychecker.fragments;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
		String text = "";
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File( Environment.getExternalStorageDirectory(), "parsed.txt" )));
			String line;
			while((line = br.readLine()) != null)
				text += line + "\n"; 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//get parsed text and set it into the textview
        TextView tv = (TextView) v.findViewById( R.id.straceView );
        if( !text.equals("") )
        	tv.setText( text );
    	else
    		tv.setText( "No Reviewed App" );
		return v;
	}
}
