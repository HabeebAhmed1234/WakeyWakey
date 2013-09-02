package com.example.alarmclock;

import java.util.ArrayList;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MusicManagerActivity extends Activity {

	public static final String TAG = "MusicManagerActivity";
    // Data and List Adapter
    MusicListAdapter musicListAdapter;
    ArrayList<Music> music = new ArrayList<Music>();
    ArrayList<Music> selectedMusic = new ArrayList <Music>();
    
    @Override
    public void onDestroy ()
    {
    	super.onDestroy();
    	ArrayList<Music> selectedMusic = new ArrayList<Music>();
    	
    	for(int i=0;i<music.size();i++)
    	{
    		if(music.get(i).isSelected())
    		{
    			selectedMusic.add(music.get(i));
    		}
    	}
    	GlobalStaticVariables.selectedMusic = selectedMusic;
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.v(TAG, "Activity State: onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_manager);        
        
        ListView lvMusicssContent = (ListView) findViewById(R.id.musicList);
		
        music=getMusic();
        
        musicListAdapter = new MusicListAdapter(this, music);
        lvMusicssContent.setAdapter(musicListAdapter);
		
        lvMusicssContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent,  final View view,int position, long id) {
		    	    String name=(String) ((TextView) view.findViewById(R.id.tvLAYOUTMUSICName)).getText();
		            Music selectedMusic = (Music)parent.getAdapter().getItem(position);
		            selectedMusic.toggleSelected();
		            musicListAdapter.notifyDataSetChanged();
		            view.refreshDrawableState();
		      }

		    });
    }
    
    private ArrayList <Music> getMusic()
    {
    	
    	ArrayList <Music> musicList = new ArrayList<Music>();

    	String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

    	String[] projection = {
    	        MediaStore.Audio.Media._ID,
    	        MediaStore.Audio.Media.ARTIST,
    	        MediaStore.Audio.Media.TITLE,
    	        MediaStore.Audio.Media.DATA,
    	        MediaStore.Audio.Media.DISPLAY_NAME,
    	        MediaStore.Audio.Media.DURATION
    	};

    	Cursor cursor = this.managedQuery(
    	        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
    	        projection,
    	        selection,
    	        null,
    	        null);
    	
    	while(cursor.moveToNext()){
    		musicList.add(new Music (cursor.getString(3),cursor.getString(2)));
    	}
  
    	return filter(musicList);
    }
    
    //only checks for names 
    boolean isInArray (ArrayList <Music> mus, Music isin)
    {
    	for(int i=0;i<mus.size();i++)
    	{
    		if(mus.get(i).getName().compareTo(isin.getName())==0)
    		{
    			return true;
    		}
    	}
    	return false;
    }
   
    private ArrayList <Music> filter (ArrayList <Music> unfiltered)
    {
    	ArrayList <Music> filtered = unfiltered;
    	for(int i=0;i<filtered.size();i++)
    	{
    		for(int x=0;x<selectedMusic.size();x++)
    		{
    			if(filtered.get(i).equals(selectedMusic.get(x)))
    			{
    				filtered.get(i).select();
    				//Log.d("debuggings",filtered.get(i).getName() + " is Selected");
    			}
    			
    		}
    	}
    	return filtered;
    }
}






