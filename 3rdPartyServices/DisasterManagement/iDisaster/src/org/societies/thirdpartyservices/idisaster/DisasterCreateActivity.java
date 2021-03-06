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
package org.societies.thirdpartyservices.idisaster;

import org.societies.thirdpartyservices.idisaster.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

/**
 * Activity for creating a new disaster team (community).
 * 
 * @author Jacqueline.Floch@sintef.no
 *
 */
public class DisasterCreateActivity extends Activity implements OnClickListener {

	private EditText disasterNameView;
	private EditText disasterDescriptionView;
	private String disasterName;
	private String disasterDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.disaster_create_layout);

		// Get editable fields
		disasterNameView = (EditText) findViewById(R.id.editDisasterCreateName);
		disasterDescriptionView = (EditText) findViewById(R.id.editDisasterCreateDescription);

    	// Add click listener to button
    	final Button button = (Button) findViewById(R.id.disasterCreateButton);
    	button.setOnClickListener(this);

//	    Test dialog
//    	iDisasterApplication.getInstance().showDialog (this, getString(R.string.DisasterCreateTestDialog), getString(R.string.dialogOK));

    }


/**
 * onClick is called when button is clicked because
 * the OnClickListener is assigned to the button
 * */

	public void onClick(View view) {

    	if (disasterNameView.getText().length() == 0) {					// check input for disaster name

    		// Hide the soft keyboard otherwise the toast message does appear more clearly.
    	    InputMethodManager mgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
    	    mgr.hideSoftInputFromWindow(disasterNameView.getWindowToken(), 0);
	    
    		Toast.makeText(this, getString(R.string.toastDisasterName), 
    				Toast.LENGTH_LONG).show();
    		return;

    	} else if (disasterDescriptionView.getText().length() == 0) {	// check input for description (or any obligatory field)

    		// Hide the soft keyboard otherwise the toast message does appear more clearly.
    	    InputMethodManager mgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
    	    mgr.hideSoftInputFromWindow(disasterDescriptionView.getWindowToken(), 0);

    	    Toast.makeText(this, getString(R.string.toastDisasterDescription), 
	    			Toast.LENGTH_LONG).show();
	    	return;

    	} else {														// add disaster to directory

    		disasterName = disasterNameView.getText().toString();
    		disasterDescription = disasterDescriptionView.getText().toString();
    		
//TODO: Add call to the Social Provider
    		
    		boolean disasterCreationCode = false;	// TODO: replace by code returned by Societes API
    			    		
    		// Create dialog for error
    		if (disasterCreationCode) { 							
    			AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
    			alertBuilder.setMessage(getString(R.string.disasterCreateDialog))
    				.setCancelable(false)
    				.setPositiveButton (getString(R.string.dialogOK), new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int id) {
    						disasterNameView.setText(getString(R.string.emptyText));
    						disasterNameView.setHint(getString(R.string.loginUserNameHint));
	    		           return;
    					}
    				});
	    		AlertDialog alert = alertBuilder.create();
	    		alert.show();
	    		return;
	   		}
	
    		// Test case: Refresh list of disasters for display in the DisasterListActivity
    		if (iDisasterApplication.testDataUsed) {
    	   	    iDisasterApplication.getInstance().disasterNameList.add(disasterName);
        	    // report data change to adapter
        	    iDisasterApplication.getInstance().disasterAdapter.notifyDataSetChanged();
   			}
     		
// TODO: Remove code for testing the correct setting of preferences 
    	    Toast.makeText(this, "Debug: "  + disasterName + " " + disasterDescription, 
    			Toast.LENGTH_LONG).show();

    	    // Hide the soft keyboard:
			// - the soft keyboard will not appear on next activity window!
    	    InputMethodManager mgr = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
    	    mgr.hideSoftInputFromWindow(disasterNameView.getWindowToken(), 0);

	    	finish();
    	    // Go back to the previous activity
	    }
    }

}
