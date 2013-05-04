package com.example.alarmclock;

import java.util.ArrayList;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactsListAdapter extends BaseAdapter {

	// context
    private Context context;

    // views
    private LayoutInflater inflater;

    // data
    private ArrayList<Contact> Contacts;
    
    public ContactsListAdapter(Context context, ArrayList<Contact> Contacts) 
    {
    	this.context = context;
    	this.Contacts = Contacts;
    	inflater = LayoutInflater.from(context);
    }
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Contacts.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Contacts.get(arg0);
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
		    v = inflater.inflate(R.layout.layout_contacts, parent, false);
		    //v = new View(context);
		}

		Contact contact = (Contact) getItem(position);
		
		TextView tvLAYOUTEVENTSName = (TextView) v.findViewById(R.id.tvLAYOUTEVENTSName);
		
		tvLAYOUTEVENTSName.setText(contact.getName()+" | "+contact.getNumber());
		
		if(contact.isSelected())
		{
			v.setBackgroundResource(R.color.Orange);
		}else
		{
			v.setBackgroundResource(R.color.White);
		}
		
		return v;
	}

}
