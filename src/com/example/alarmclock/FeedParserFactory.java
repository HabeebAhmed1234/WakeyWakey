package com.example.alarmclock;
public abstract class FeedParserFactory {
	static String feedUrl = "http://www.npr.org/rss/rss.php?id=1004";
	
	public static FeedParser getParser(){
		return new AndroidSaxFeedParser(feedUrl);
	}

}
