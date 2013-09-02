package com.example.alarmclock;

public class Contact {
	private String number;
	private String name;
	private boolean selected;
	
	Contact (String num, String nam)
	{
		this.number = regex(num);
		this.name = nam;
		this.selected = false;
	}
	
	void setName(String name)
	{
		this.name=name;
	}
	
	void setNumber(String number)
	{
		this.number=regex(number);
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
	
	String getNumber()
	{
		return this.number;
	}
	
	boolean isSelected()
	{
		return this.selected;
	}
	
	void toggleSelected()
	{
		if(isSelected())
		{
			for(int i = 0 ; i<GlobalStaticVariables.selectedContacts.size();i++)
			{
				if(GlobalStaticVariables.selectedContacts.get(i).getName() == this.name)
				{
					GlobalStaticVariables.selectedContacts.remove(i);
				}
			}
			this.unselect();
		}else
		{
			GlobalStaticVariables.selectedContacts.add(this);
			this.select();
		}
	}
	
	boolean equals(Contact con)
	{
		if(con.getName().compareTo(this.name)==0&&con.getNumber().compareTo(this.number)==0) return true;
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
