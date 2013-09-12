package com.example.alarmclock;

import java.util.ArrayList;
import java.util.List;

import android.os.StrictMode;
import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;
import android.util.Xml;

public class AndroidSaxFeedParser extends BaseFeedParser {

	static final String RSS = "rss";
	public AndroidSaxFeedParser(String feedUrl) {
		super(feedUrl);
	}

	public List<Message> parse() {
		// sketchy code
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		
		final Message currentMessage = new Message();
		RootElement root = new RootElement(RSS);
		final List<Message> messages = new ArrayList<Message>();
		Element channel = root.getChild(CHANNEL);
		Element item = channel.getChild(ITEM);
		item.setEndElementListener(new EndElementListener(){
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setTitle(body);
			}
		});
		item.getChild(LINK).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setLink(body);
			}
		});
		item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDescription(body);
			}
		});
		item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener(){
			public void end(String body) {
				currentMessage.setDate(body);
			}
		});
		try {
			Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
		} catch (Exception e) {
			Message testMessage = new Message();
			Log.d("rss", "here");
			testMessage.setTitle("Unable to load World News Radio!");
			testMessage.setDate("Wed, 11 Sep 2013 22:51:00 -0400.");
			testMessage.setLink("http://www.npr.org/blogs/thetwo-way/2013/09/11/221551883/challenging-obama-putin-appeals-directly-to-americans-on-syria?ft=1&f=1004");
			testMessage.setDescription("Please check your internet connection.");
			messages.add(testMessage.copy());
			
			return messages;
		}
		
		for (int i=0; i<messages.size(); i++){
			Log.d("rss", messages.get(i).getTitle() + "\n" + messages.get(i).getLink()+ "\n"  + messages.get(i).getDate()+ "\n"  + messages.get(i).getDescription());
		}
		
		return messages;
	}
}
