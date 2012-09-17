package edu.nkuresearch.securitychecker.fragments;

import android.os.Bundle;
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
		//get parsed text and set it into the textview
        String text = getSherlockActivity().getIntent().getStringExtra( "STRACE" );
        TextView tv = (TextView) v.findViewById( R.id.straceView );
        if( text != null )
        	tv.setText( text );
    	else
    		tv.setText( "FAIL" );
		return v;
	}
}
