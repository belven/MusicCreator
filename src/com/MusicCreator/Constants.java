package com.MusicCreator;

import java.util.Arrays;
import java.util.List;

public class Constants
{
	public static int WHOLE_STEP = 2;
	public static int HALF_STEP = 1;

	public static List<String> NOTES = Arrays
			.asList(new String[] { "C", "C/#", "D", "D/#", "E", "F", "F/#", "G", "G/#", "A", "A/#", "B" });

	public static int[] MAJOR_CHORD = new int[] { 1, 5, 8 };

	public static int[] MINOR_CHORD = new int[] { 1, 4, 8 };

	public static int[] MAJOR_SCALE = new int[] { 1, 3, 5, 6, 8, 10, 12 };

	public static int[] MINOR_SCALE = new int[] { 1, 3, 4, 6, 8, 9, 11 };
}
