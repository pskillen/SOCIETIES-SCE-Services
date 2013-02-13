/**
 * Copyright (c) 2011, SOCIETIES Consortium (WATERFORD INSTITUTE OF TECHNOLOGY (TSSG), HERIOT-WATT UNIVERSITY (HWU), SOLUTA.NET 
 * (SN), GERMAN AEROSPACE CENTRE (Deutsches Zentrum fuer Luft- und Raumfahrt e.V.) (DLR), Zavod za varnostne tehnologije
 * informacijske družbe in elektronsko poslovanje (SETCCE), INSTITUTE OF COMMUNICATION AND COMPUTER SYSTEMS (ICCS), LAKE
 * COMMUNICATIONS (LAKE), INTEL PERFORMANCE LEARNING SOLUTIONS LTD (INTEL), PORTUGAL TELECOM INOVAÇÃO, SA (PTIN), IBM Corp., 
 * INSTITUT TELECOM (ITSUD), AMITEC DIACHYTI EFYIA PLIROFORIKI KAI EPIKINONIES ETERIA PERIORISMENIS EFTHINIS (AMITEC), TELECOM 
 * ITALIA S.p.a.(TI),  TRIALOG (TRIALOG), Stiftelsen SINTEF (SINTEF), NEC EUROPE LTD (NEC))
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.disaster.idisaster;

import com.disaster.idisaster.R;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.disaster.idisaster.SocialContract;
import android.net.Uri;
import android.content.ContentValues;
import android.content.ContentResolver;
import android.database.Cursor;


/**
 * This is the activity that starts when you first click on the icon in Android.
 * 
 * It selects the next activities.
 * See the method onClick for more details.
 * 
 * @author Jacqueline.Floch@sintef.no
 * 
 *
 */

// TODO: will be extended to retrieve the user data in the SocialProvider

public class StartActivity extends Activity implements OnClickListener {

	Boolean loggedIn = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
    				
	    setContentView (R.layout.start_layout);	// create GUI
   	    
	    // Add click listener to button
	    final Button button = (Button) findViewById(R.id.startButton);
	    button.setOnClickListener(this);

    }

/**
 * onResume: Things to do when the activity becomes visible.
 * - check the ContentProvider is answering
 * - check if the user is logged in
 * 
 */
	@Override
    public void onResume () {
    	
    	super.onResume();
    	
    
    	loggedIn = false;
    		
    	// check is the user is logged in
    	// if logged in display the user name
    	// if not tell the user he is not logged in.
		TextView startView = (TextView) findViewById(R.id.startInfo);

		
		if (iDisasterApplication.testDataUsed) {			// Test data
			startView.setText(getString(R.string.startInfoNotLogged));			
		} else {														// Fetch data from SocialProvider
			String userName = getUserIdentity();
			if (userName == null) {											// No data returned by Social provider
				iDisasterApplication.getInstance().showDialog (this,
						"Unable to retrieve user information from SocialProvider",
						 getString(R.string.dialogOK));
			} else if (userName == "Your Name") {							// User is not identified
				startView.setText(getString(R.string.startInfoNotLogged));
			
			} else {														// User is identified
				startView.setText(getString(R.string.startInfoLogged) + userName);
				loggedIn = true;
			}
		}
	}

/**
 * onClick is called when button is clicked because
 * the OnClickListener is assigned to the button
 */
	 public void onClick (View view) {
		
		if (!loggedIn) {		// no user name
    		startActivity(new Intent(StartActivity.this, LoginActivity.class));
    		return;			
		} else {
	    	startActivity(new Intent(StartActivity.this, DisasterActivity.class));
	    	return;
		}
   	}


/**
 * getUserIdentity is to add the user information in SocialProvider
 */

	private String getUserIdentity () {
		String displayName = null;
		
		Uri uri = SocialContract.Me.CONTENT_URI;

		//What to get:
		String[] projection = new String [] {
				SocialContract.Me.GLOBAL_ID,
				SocialContract.Me.NAME,
				SocialContract.Me.DISPLAY_NAME
			};
		
//		String selection = "";
		String selection = SocialContract.Me._ID + " = 1";

		String[] selectionArgs = null;
		String sortOrder = null;
		
		Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
		
		if (cursor == null) {
			iDisasterApplication.getInstance().showDialog (this,
					"Unable to retrieve user information from SocialProvider",
					 getString(R.string.dialogOK));
		} else {
// TODO: Add a check? : Should it be a single user (stored in Me).
//			int i= cursor.getCount(); was used for debug
			if (cursor.moveToFirst()){
				displayName = cursor.getString(cursor
						.getColumnIndex(SocialContract.Me.DISPLAY_NAME));			}
		}
		return displayName;
	}

	
/**
 * getPreferences retrieves the preferences stored in the preferences file.
 */
// All information is fetched from the Social Provider. Preferences are no longer used.
//
//    private void getPreferences () {
//
//    	userName = iDisasterApplication.getInstance().getUserName ();
//    	disasterTeamName = iDisasterApplication.getInstance().getDisasterTeamName ();
//	}
		
/**
 * startNextActivity is called when to select the next activity.
 * 
 * If the user is not registered, it starts the LoginActivity,
 * otherwise if no disaster is selected, it starts the DisasterActivity
 * otherwise it starts the HomeActivity.
 */
//	private void startNextActivity () {
//		
//    	if (userName == getString(R.string.noPreference)) {							// no user name (no password)
//    		startActivity(new Intent(StartActivity.this, LoginActivity.class));
//    		return;
//    	} else if (disasterTeamName == getString(R.string.noPreference)) {			// no disaster selected
//    		startActivity(new Intent(StartActivity.this, DisasterListActivity.class));
//    		return;
//    	} else {   		
//    		startActivity(new Intent(StartActivity.this, DisasterActivity.class));
//    		return;
//    	}
//    }


/**
 * onCreateOptionsMenu creates the activity menu.
 */
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.clear();
		getMenuInflater().inflate(R.menu.start_menu, menu);

//		It is possible to set up a variable menu		
//			menu.findItem (R.id....).setVisible(true);

		return true;
	}


/**
  * onOptionsItemSelected handles the selection of an item in the activity menu.
  */

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
    	case R.id.startMenuLogoff:
//TODO: Call the Social Provider
// reset user preferences - not used anymore
//    		iDisasterApplication.getInstance().setDisasterTeamName // reset user preferences
//    		(getString(R.string.noPreference));					

    		break;
    		
    	default:
    		break;
    	}
    	return true;
    }

}