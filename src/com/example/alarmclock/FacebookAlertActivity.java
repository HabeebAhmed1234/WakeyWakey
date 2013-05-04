package com.example.alarmclock;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.facebook.*;
import com.facebook.android.Facebook;
import com.facebook.model.*;
import android.widget.TextView;
import android.content.Intent;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FacebookAlertActivity extends Activity {

    public ArrayList<fbPost> newsFeed = new ArrayList<fbPost>();
	
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
    "source_id, actor_id, attachment FROM stream WHERE filter_key IN (SELECT filter_key FROM " +
    "stream_filter WHERE uid=me() AND type='newsfeed')";
    
    private static final String FQL_ID_TO_NAME_QUERY = "SELECT name FROM user WHERE uid = ";
    
    private final ReentrantLock lock = new ReentrantLock();
    
    public String nameFromID;
	
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
	    Bundle params = new Bundle();
        params.putString("q", FQL_QUERY);
        Session session = Session.getActiveSession();
        
        // get permissions to read user's facebook feed
        requestReadPermissions(session);
            
        // apparently a bad practice...
        sketchyCode();
        
        Request request = new Request(session,
            "/fql",                         
            params,                         
            HttpMethod.GET,                 
            new Request.Callback(){
                public void onCompleted(Response response) {
                	//response.getClass();
                	GraphObject feed = response.getGraphObject();
                	
                	if (feed != null){
                		JSONObject jsonObject = feed.getInnerJSONObject();
                		JSONArray arr;
                		
						try {
							arr = jsonObject.getJSONArray("data");
						
					        String s = Integer.toString(newsFeed.size());
					        Log.d("fbPost::onCompleted", s);
					        
							for (int i = 0; i < arr.length(); i++) {
					            JSONObject object = (JSONObject) arr.get(i);
					            test(translateJSONToPost(object));
					            newsFeed.add(translateJSONToPost(object));
					            Log.d("TAG", "message = "+object.get("message"));
					         }
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	
                    Log.i("TAG", "Result: " + response.toString());
                	}
  	            }
        	}); 
        
        Request.executeBatchAndWait(request);
        
    	String s = Integer.toString(newsFeed.size());
    
    	Log.d("fbPost::after", s);
        s = Integer.toString(newsFeed.size());
        
        Log.d("fbPost", s);
        for (int i=0; i<newsFeed.size(); i++){
        	newsFeed.get(i).print();
        }
	}
	
	public void onClick(View v){
        String s = Integer.toString(newsFeed.size());
		Log.d("onClick::fbPost", s);
        for (int i=0; i<newsFeed.size(); i++){
        	newsFeed.get(i).print();
        }
	}
	
	public void test(fbPost post){
        String s = Integer.toString(newsFeed.size());
        
		newsFeed.add(post);
		
        s = Integer.toString(newsFeed.size());
        Log.d("test(): fbPost", s);
	}
	
	private fbPost translateJSONToPost(JSONObject object) throws JSONException {
		fbPost post;
		JSONObject attach;
		
		String actor_id;
		String actor_name;
		String source_id;
		String source_name;
		
		actor_id = object.getString("actor_id");
		source_id = object.getString("source_id");
		
		actor_name = getNameFromID(actor_id);
		source_name = getNameFromID(source_id);
		
		// before processing the update as a regular status update,
		// try to read it in as a Youtube link or a regular link
		try {
			attach = object.getJSONObject("attachment");

			JSONArray media = attach.getJSONArray("media");
			Log.d("2TAG", media.toString());
			
			if (media.toString().equals("[]")){
				//Log.d("2TAG", "i failed you (media)");
				post = new linkPost(attach.getString("description"), actor_name, source_name);
				return post;
			}
			post = new videoLinkPost(attach.getString("description"), attach.getString("href"), actor_name, source_name);
			return post;
			//linkPost thing = (linkPost) post;
			//Log.d("2TAG", "message: " + post.message + " URL: " + thing.linkURL );
		} catch (JSONException e) {
			// Means we were NOT able to retrieve an attachment from the post (indicating it
			// is a regular status update)!
			//Log.d("2TAG", "i failed you");
			e.printStackTrace();
		}
		
		// now that we know its not a link, then we can treat it as a regular post
		try {
			post = new statusPost(object.getString("message"), actor_name, source_name);
			return post;
		} catch (JSONException e) {
			Log.d("2TAG", "this type of post is not supported by our processing engine yet.");
			return null;
		}		
	}
	
	private String getNameFromID(String id){
		sketchyCode();
				
	    Bundle params = new Bundle();
        params.putString("q", FQL_ID_TO_NAME_QUERY + id);
        Session session = Session.getActiveSession();
		
		Request request = new Request(session,
	            "/fql",                         
	            params,                         
	            HttpMethod.GET,                 
	            new Request.Callback(){         
	                public void onCompleted(Response response) {
	                	GraphObject data = response.getGraphObject();
	                	
	                	if (data != null){
	                		JSONObject jsonObject = data.getInnerJSONObject();
	                		JSONArray arr;
	                		
							try {
								arr = jsonObject.getJSONArray("data");
						        
								JSONObject user = (JSONObject) arr.get(0);
								nameFromID = user.getString("name");
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                	}
	                	
	                    Log.i("TAG", "Result: " + response.toString());
	                }                
	        });
        
		Request.executeBatchAndWait(request);
		return nameFromID;
		
	}

	private void requestReadPermissions(Session session) {
	    if (session != null) {
	        Session.NewPermissionsRequest newPermissionsRequest = 
	            new Session.NewPermissionsRequest(this, PERMISSIONS).
	                setRequestCode(REAUTH_ACTIVITY_CODE);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	    }
	}
	
	// to allow for non-asynchronous calls to Facebook batch handling methods
	private void sketchyCode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
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
