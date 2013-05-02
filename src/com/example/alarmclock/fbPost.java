package com.example.alarmclock;

import android.util.Log;

public abstract class fbPost {
	protected String message;
	protected String sender;
	protected String receiver;
	
	public void print(){
		Log.d("fbPost", "Message = " + message);
	}
}

// includes youtube links
class videoLinkPost extends fbPost {
	public videoLinkPost(String message, /*String sender,*/ String linkURL){
		this.message = message;
		//this.sender = sender;
	}
	
	public void print(){
		Log.d("fbPost", "Message = " + message);
		Log.d("fbPost", "URL = " + linkURL);
	}
	
	public String linkURL;
}

// regular links
class linkPost extends fbPost {
	public linkPost(String message/*, String poster*/){
		this.message = message;
		//this.sender = sender;
	}
	
	public String linkURL;
	
	public void print(){
		Log.d("fbPost", "Message = " + message);
		Log.d("fbPost", "URL = " + linkURL);
	}
}

// X is now friends with A, B, C, D
class friendsWithPost extends fbPost {
	
}

// regular status updates from friends
class statusPost extends fbPost {
	public statusPost(String message){
		this.message = message;
	}
}

// everything else will be ignored for now
class miscPost extends fbPost {
	
}