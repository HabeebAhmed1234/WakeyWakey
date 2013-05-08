package com.example.alarmclock;

public class Music {
	private String path;
	private String name;
	private boolean selected;
	
	Music (String path, String nam)
	{
		this.path = path;
		this.name = nam;
		this.selected = false;
	}
	
	void setName(String name)
	{
		this.name=name;
	}
	
	void setPath(String number)
	{
		this.path=number;
	}
	
	void select()
	{
		this.selected=true;
	}
	
	void unselect()
	{
		this.selected=false;
	}
	
	String getName()
	{
		return this.name;
	}
	
	String getPath()
	{
		return this.path;
	}
	
	boolean isSelected()
	{
		return this.selected;
	}
	
	void toggleSelected()
	{
		if(isSelected())
		{
			this.unselect();
		}else
		{
			this.select();
		}
	}
	
	boolean equals(Music mus)
	{
		if(mus.getName().compareTo(this.name)==0&&mus.getPath().compareTo(this.path)==0) return true;
		return false;
	}
	
	private String regex (String in)
	{
		in=in.replace("+", "");
		in=in.replace("-", "");
		in=in.replace("*", "");
		in=in.replace(",", "");
		return in;
	}
}
