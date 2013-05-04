package com.example.alarmclock;

import android.util.Log;

public abstract class fbPost {
	protected String message;
	protected String sender;
	protected String receiver;
	
	public void print(){
		Log.d("fbPost::print()", "Message = " + message);
		Log.d("fbPost::print()", "Sender = " + sender);
		Log.d("fbPost::print()", "Receiver = " + receiver);
	}
}

// includes youtube links
class videoLinkPost extends fbPost {
	public videoLinkPost(String message, String linkURL, String receiver, String sender){
		this.message = message;
		this.linkURL = linkURL;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public void print(){
		Log.d("fbPost", "Message = " + message);
		Log.d("fbPost", "URL = " + linkURL);
		Log.d("fbPost)", "Sender = " + sender);
		Log.d("fbPost", "Receiver = " + receiver);
	}
	
	public String linkURL;
}

// regular links
class linkPost extends fbPost {
	public linkPost(String message, String receiver, String sender){
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public String linkURL;
	
	public void print(){
		Log.d("fbPost", "Message = " + message);
		Log.d("fbPost", "URL = " + linkURL);
		Log.d("fbPost)", "Sender = " + sender);
		Log.d("fbPost", "Receiver = " + receiver);
	}
}

// X is now friends with A, B, C, D
class friendsWithPost extends fbPost {
	
}

// regular status updates from friends
class statusPost extends fbPost {
	public statusPost(String message, String receiver, String sender){
		this.message = message;
		this.sender = sender;
		this.receiver = receiver;
	}
}

// everything else will be ignored for now
class miscPost extends fbPost {
	
}