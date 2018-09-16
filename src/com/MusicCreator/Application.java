package com.MusicCreator;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Application
{
	private static final String MINOR = "Minor";
	private static final String MAJOR = "Major";
	private static String majorSelected = MAJOR;
	private static Scale selectedScale = null;

	private static Choice majorMinorCombo;
	private static Choice notesCombo;
	private static Label notesLabel;
	private static Label chordValuesLabel;
	private static Choice availableChordsCombo;
	private static Button addChordButton;

	private static final List<Chord> availableChords = new ArrayList<>();
	private static final List<Chord> selectedChords = new ArrayList<>();

	private static final List<Chord> CHORDS = new ArrayList<>();
	private static final List<Scale> SCALES = new ArrayList<>();

	static
	{
		for (String note : Constants.NOTES)
		{
			SCALES.add(new Scale(getNotesInPattern(Constants.MAJOR_SCALE, note), true));
			CHORDS.add(new Chord(getNotesInPattern(Constants.MAJOR_CHORD, note), true));
		}

		for (String note : Constants.NOTES)
		{
			SCALES.add(new Scale(getNotesInPattern(Constants.MINOR_SCALE, note), false));
			CHORDS.add(new Chord(getNotesInPattern(Constants.MINOR_CHORD, note), false));
		}
	}

	public static void main(String[] args)
	{
		Frame frame = createFrame();

		notesLabel = new Label("Notes in Scale");
		chordValuesLabel = new Label("Test Text");

		Label majorMinorLabel = new Label("Major Or Minor Scale");
		Label keyLabel = new Label("Key");
		Label scaleLabel = new Label("Notes in Scale");
		Label chordsLabel = new Label("Chords in Key");

		createMajorMinorCombo();
		createNotesCombo();
		createAvailableChordsCombo();

		createAddChordButton();

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridwidth = 2;

		frame.add(majorMinorLabel);
		frame.add(majorMinorCombo);
		frame.add(keyLabel);
		frame.add(notesCombo);

		frame.add(scaleLabel, constraints);
		frame.add(notesLabel, constraints);

		frame.add(chordsLabel, constraints);
		frame.add(chordValuesLabel, constraints);
		frame.add(availableChordsCombo);
		frame.add(addChordButton);

		//frame.pack();
		frame.setVisible(true);
		Application.updateNotesLabel(notesLabel, notesCombo.getSelectedItem());
	}

	private static void createAddChordButton()
	{
		addChordButton = new Button("Add Selected Chord");
		addChordButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				selectedChords.add(getSelectedChord());
			}
		});
	}

	private static Chord getSelectedChord()
	{
		String selectedItem = availableChordsCombo.getSelectedItem();

		boolean isMajor = selectedItem.contains(MAJOR);

		selectedItem = selectedItem.replaceAll(MAJOR, "");
		selectedItem = selectedItem.replaceAll(MINOR, "");
		selectedItem = selectedItem.trim();

		for (Chord chord : availableChords)
		{
			if (chord.getFirstNote().equals(selectedItem) && chord.isMajor() == isMajor)
			{
				return chord;
			}
		}
		return null;
	}

	private static Frame createFrame()
	{
		Frame frame = new Frame();
		frame.setSize(800, 400);

		GridLayout gridLayout = new GridLayout(10, 2);
		frame.setLayout(gridLayout);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				System.exit(0);
			}
		});

		return frame;
	}

	private static void createNotesCombo()
	{
		notesCombo = new Choice();

		for (String s : Constants.NOTES)
			notesCombo.add(s);

		notesCombo.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				Application.updateNotesLabel(notesLabel, e.getItem().toString());
			}
		});

		notesCombo.select(Constants.NOTES.get(0));
	}

	private static void createAvailableChordsCombo()
	{
		availableChordsCombo = new Choice();

		for (Chord chord : availableChords)
		{
			availableChordsCombo.add(getChordName(chord));
		}

		availableChordsCombo.select(Constants.NOTES.get(0));
	}

	private static void updateNotesLabel(Label notesLabel, String string)
	{
		int[] pattern = isMajorSelected() ? Constants.MAJOR_SCALE : Constants.MINOR_SCALE;
		List<String> notes = getNotesInPattern(pattern, string);
		notesLabel.setText(notes.toString());

		StringBuilder sb = new StringBuilder();

		Scale scale = getSelectedScale();
		selectedScale = scale;

		availableChords.clear();
		availableChordsCombo.removeAll();

		for (Chord chord : CHORDS)
		{
			if (scale.Contains(chord.getNotes()))
			{
				availableChords.add(chord);
				sb.append(getChordName(chord) + ", ");
				availableChordsCombo.add(getChordName(chord));
			}
		}

		chordValuesLabel.setText(sb.toString());
	}

	private static String getChordName(Chord chord)
	{
		return chord.getFirstNote() + " " + getMajorMinorString(chord.isMajor());
	}

	private static String getMajorMinorString(boolean isMajor)
	{
		return isMajor ? MAJOR : MINOR;
	}

	private static boolean isMajorSelected()
	{
		return majorSelected == MAJOR;
	}

	private static Scale getSelectedScale()
	{
		for (Scale scale : SCALES)
		{
			if (scale.getFirstNote().equals(notesCombo.getSelectedItem()) && scale.isMajor() == isMajorSelected())
			{
				return scale;
			}
		}
		return null;
	}

	private static void createMajorMinorCombo()
	{
		majorMinorCombo = new Choice();
		majorMinorCombo.add(MAJOR);
		majorMinorCombo.add(MINOR);

		majorMinorCombo.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e)
			{
				majorSelected = e.getItem().toString();
				Application.updateNotesLabel(notesLabel, notesCombo.getSelectedItem());
			}
		});
	}

	public static List<String> getNotesInPattern(int[] pattern, String startingNote)
	{
		ArrayList<String> notes = new ArrayList<>();
		int start = Constants.NOTES.lastIndexOf(startingNote);

		for (int i : pattern)
		{
			int index = i + start - 1;
			notes.add(Constants.NOTES.get(index % Constants.NOTES.size()));
		}

		return notes;
	}
}
