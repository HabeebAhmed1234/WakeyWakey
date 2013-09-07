package com.example.alarmclock;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MessageList {
	
	public List<Message> messages = new ArrayList<Message>();
	
    public MessageList()
    {
        loadFeed(ParserType.ANDROID_SAX);
        for (int i=0; i<messages.size(); i++){
        	Log.d("title", messages.get(i).getTitle());
        	Log.d("rss", messages.get(i).getDescription());
        }
        
    }
    
	private void loadFeed(ParserType type){
    	try{
    		Log.i("AndroidNews", "ParserType="+type.name());
	    	FeedParser parser = FeedParserFactory.getParser();
	    	messages = parser.parse();
	    	List<String> titles = new ArrayList<String>(messages.size());
	    	for (Message msg : messages){
	    		titles.add(msg.getTitle());
	    	}
    	} catch (Throwable t){
    		Log.e("AndroidNews",t.getMessage(),t);
    	}
    }
    }