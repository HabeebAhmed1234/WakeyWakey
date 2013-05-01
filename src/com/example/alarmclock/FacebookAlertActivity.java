package com.example.alarmclock;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import com.facebook.*;
import com.facebook.android.Facebook;
import com.facebook.model.*;
import android.widget.TextView;
import android.content.Intent;
import java.util.*;

public class FacebookAlertActivity extends Activity {

	// Activity code to flag an incoming activity result is due 
	// to a new permissions request
	private static final int REAUTH_ACTIVITY_CODE = 100;

	// Indicates an on-going reauthorization request
	private boolean pendingAnnounce;

	// Key used in storing the pendingAnnounce flag
	private static final String PENDING_ANNOUNCE_KEY = "pendingAnnounce";

	/// List of additional write permissions being requested
	private static final List<String> PERMISSIONS = Arrays.asList("read_stream");
	
    private static final String FQL_QUERY = "SELECT message, comments, " +
    "source_id, actor_id FROM stream WHERE filter_key IN (SELECT filter_key FROM " +
    "stream_filter WHERE uid=me() AND type='newsfeed')";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facebook_alert);
		
		// start Facebook Login
		  Session.openActiveSession(this, true, new Session.StatusCallback() {

		    // callback when session changes state
		    @Override
		    public void call(Session session, SessionState state, Exception exception) {
		    	if (session.isOpened()) {
		    		// make request to the /me API
		    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

		    		  // callback after Graph API response with user object
		    		  @Override
		    		  public void onCompleted(GraphUser user, Response response) {
		    			  if (user != null) {
		    				  TextView welcome = (TextView) findViewById(R.id.welcome);
		    				  welcome.setText("Hello " + user.getName() + "!");
		    				}
		    		  }
		    		});
		    	}
		    }
		  });
		  
		// pull from news feed
	    String fqlQuery = FQL_QUERY;
	    Bundle params = new Bundle();
        params.putString("q", fqlQuery);
        Session session = Session.getActiveSession();
        
        // get permissions to read user's facebook feed
        requestReadPermissions(session);
        
        Request request = new Request(session,
            "/fql",                         
            params,                         
            HttpMethod.GET,                 
            new Request.Callback(){         
                public void onCompleted(Response response) {
                	response.getClass();
                    Log.i("TAG", "Result: " + response.toString());
                }                  
        }); 
        Request.executeBatchAsync(request); 
	}

	private void requestReadPermissions(Session session) {
	    if (session != null) {
	        Session.NewPermissionsRequest newPermissionsRequest = 
	            new Session.NewPermissionsRequest(this, PERMISSIONS).
	                setRequestCode(REAUTH_ACTIVITY_CODE);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_facebook_alert, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
	
}
