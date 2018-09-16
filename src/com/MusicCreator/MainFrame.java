package com.MusicCreator;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends Frame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6981306342593949229L;

	private final String MINOR = "Minor";
	private final String MAJOR = "Major";
	private String majorSelected = MAJOR;
	private Choice majorMinorCombo;
	private Choice notesCombo;
	private Label notesLabel;
	private Label chordValuesLabel;
	private Choice availableChordsCombo;
	private Button addChordButton;

	private final List<Chord> availableChords = new ArrayList<Chord>();
	private final List<Chord> selectedChords = new ArrayList<Chord>();

	public final static List<Chord> CHORDS = new ArrayList<Chord>();
	public final static List<Scale> SCALES = new ArrayList<Scale>();

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

	public MainFrame() throws HeadlessException
	{
		setupFrame();

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

		add(majorMinorLabel);
		add(majorMinorCombo);
		add(keyLabel);
		add(notesCombo);

		add(scaleLabel, constraints);
		add(notesLabel, constraints);

		add(chordsLabel, constraints);
		add(chordValuesLabel, constraints);
		add(availableChordsCombo);
		add(addChordButton);

		// pack();
		setVisible(true);
		updateNotesLabel(notesLabel, notesCombo.getSelectedItem());
	}

	public void main(String[] args)
	{

	}

	private void createAddChordButton()
	{
		addChordButton = new Button("Add Selected Chord");
		addChordButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectedChords.add(getSelectedChord());
			}
		});
	}

	private Chord getSelectedChord()
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

	private void setupFrame()
	{
		setSize(800, 400);

		GridLayout gridLayout = new GridLayout(10, 2);
		setLayout(gridLayout);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				System.exit(0);
			}
		});

	}

	private void createNotesCombo()
	{
		notesCombo = new Choice();

		for (String s : Constants.NOTES)
			notesCombo.add(s);

		notesCombo.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				updateNotesLabel(notesLabel, e.getItem().toString());
			}
		});

		notesCombo.select(Constants.NOTES.get(0));
	}

	private void createAvailableChordsCombo()
	{
		availableChordsCombo = new Choice();

		for (Chord chord : availableChords)
		{
			availableChordsCombo.add(getChordName(chord));
		}

		availableChordsCombo.select(Constants.NOTES.get(0));
	}

	private void updateNotesLabel(Label notesLabel, String string)
	{
		int[] pattern = isMajorSelected() ? Constants.MAJOR_SCALE : Constants.MINOR_SCALE;
		List<String> notes = getNotesInPattern(pattern, string);
		notesLabel.setText(notes.toString());

		StringBuilder sb = new StringBuilder();

		Scale scale = getSelectedScale();
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

	private String getChordName(Chord chord)
	{
		return chord.getFirstNote() + " " + getMajorMinorString(chord.isMajor());
	}

	private String getMajorMinorString(boolean isMajor)
	{
		return isMajor ? MAJOR : MINOR;
	}

	private boolean isMajorSelected()
	{
		return majorSelected == MAJOR;
	}

	private Scale getSelectedScale()
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

	private void createMajorMinorCombo()
	{
		majorMinorCombo = new Choice();
		majorMinorCombo.add(MAJOR);
		majorMinorCombo.add(MINOR);

		majorMinorCombo.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				majorSelected = e.getItem().toString();
				updateNotesLabel(notesLabel, notesCombo.getSelectedItem());
			}
		});
	}

	public static List<String> getNotesInPattern(int[] pattern, String startingNote)
	{
		ArrayList<String> notes = new ArrayList<String>();
		int start = Constants.NOTES.lastIndexOf(startingNote);

		for (int i : pattern)
		{
			int index = i + start - 1;
			notes.add(Constants.NOTES.get(index % Constants.NOTES.size()));
		}

		return notes;
	}
}
