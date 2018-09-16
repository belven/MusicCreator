package com.MusicCreator;

import java.util.ArrayList;
import java.util.List;

public class NotesContainer
{
	List<String> notes = new ArrayList<>();
	boolean major = true;

	public NotesContainer(List<String> inNotes, boolean inMajor)
	{
		notes = inNotes;
		major = inMajor;
	}

	@Override
	public String toString()
	{
		return String.valueOf(major) + ", " + notes.toString();
	}

	public String getFirstNote()
	{
		return notes.get(0);
	}

	public boolean Contains(List<String> searchNotes)
	{
		for (String note : searchNotes)
		{
			if (!notes.contains(note))
				return false;
		}

		return true;
	}

	public boolean Contains(String note)
	{
		return notes.contains(note);
	}

	public List<String> getNotes()
	{
		return notes;
	}

	public void setNotes(List<String> notes)
	{
		this.notes = notes;
	}

	public boolean isMajor()
	{
		return major;
	}

	public void setMajor(boolean isMajor)
	{
		this.major = isMajor;
	}

}
