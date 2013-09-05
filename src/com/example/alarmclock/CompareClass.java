package com.example.alarmclock;

import java.util.Comparator;

public class CompareClass implements Comparator<Contact> {
	@Override
	public int compare(Contact lhs, Contact rhs) {
		// TODO Auto-generated method stub
		return lhs.getName().compareToIgnoreCase(rhs.getName());
	}
}
