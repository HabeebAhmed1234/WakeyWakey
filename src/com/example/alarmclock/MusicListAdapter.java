package com.example.alarmclock;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MusicListAdapter extends BaseAdapter {
	// context
    private Context context;

    // views
    private LayoutInflater inflater;

    // data
    private ArrayList<Music> Musics;
    
    public MusicListAdapter(Context context, ArrayList<Music> musics) 
    {
    	this.context = context;
    	this.Musics = musics;
    	inflater = LayoutInflater.from(context);
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Musics.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Musics.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View v = null;
		if (convertView != null) {
			v = convertView;
		} else {
		    v = inflater.inflate(R.layout.layout_music, parent, false);
		    //v = new View(context);
		}

		Music music = (Music) getItem(position);
		
		TextView tvLAYOUTMUSICName = (TextView) v.findViewById(R.id.tvLAYOUTMUSICName);
		
		tvLAYOUTMUSICName.setText(music.getName());
		
		if(music.isSelected())
		{
			v.setBackgroundResource(R.color.Orange);
		}else
		{
			v.setBackgroundResource(R.color.White);
		}
		
		return v;
	}
}
