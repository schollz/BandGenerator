import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.*;
import java.util.Random;
import java.io.*;

/* File: BandGen
 * Purpose: To generate random band names using a wordsource
 * Author: Nate_the_Great
 */
public class BandGen extends JFrame implements ActionListener, ItemListener, Runnable {
	
	Thread runner;
	boolean append_data = false;
	static Random rn = new Random();
	JPanel row1 = new JPanel();
	JTextArea display = new JTextArea(5,18);
	JScrollPane dispscroll = new JScrollPane(display, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JList bandList;
	
	DefaultListModel listModel;
	
	JPanel row2 = new JPanel();
	JButton generate = new JButton("Generate");
	JButton save = new JButton("Save Data");
	JButton quit = new JButton("Quit");
	JButton btnDef = new JButton("Definition");
	JButton btnFind = new JButton("Find Word");

	JLabel label = new JLabel(" Band Names:");
	JPanel row0 = new JPanel();
	JCheckBox chkAddOn = new JCheckBox("Check to Append", false);
	
	JMenuItem mnuSave, mnuQuit, mnuGenerate, mnuDefinition, mnuWord;
	
	public static void main(String args[]) throws IOException {
		BandGen frame = new BandGen();
	}
	
	public BandGen() {
		super("Band Name Generator");
		setSize(300,300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container pane = getContentPane();
		BorderLayout layout = new BorderLayout();
		GridLayout grid = new GridLayout(1,4,10,10);
		GridLayout grid1 = new GridLayout(1,1,10,10);
		GridLayout grid2 = new GridLayout(1,2,10,10);
		GridLayout grid3 = new GridLayout(2,1,10,10);
		pane.setLayout(layout);
		
		row0.setLayout(grid2);
		row0.add(label);
		row0.add(chkAddOn);
		pane.add(row0, BorderLayout.NORTH);
		
		/* Set up list for adding */
		listModel = new DefaultListModel();
		bandList = new JList(listModel);
		bandList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bandList.setLayoutOrientation(JList.VERTICAL);
		bandList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(bandList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		row1.setLayout(grid3);
		row1.add(listScroller);
		row1.add(dispscroll);
		pane.add(row1, BorderLayout.CENTER);
		
		row2.setLayout(new GridLayout(1,2,10,10));
		row2.add(generate);
		row2.add(btnDef);
		//row2.add(btnFind);
		//row2.add(quit);
		pane.add(row2, BorderLayout.SOUTH);
		
		// Make a MenuBar
		JMenuBar mb = new JMenuBar();
		setJMenuBar(mb);
		JMenu file = new JMenu("File");
		JMenu bandnames = new JMenu("Band Names");
		mnuSave = new JMenuItem("Save Data");
		mnuQuit = new JMenuItem("Quit");
		mnuGenerate = new JMenuItem("Generate");
		mnuDefinition = new JMenuItem("Definition");
		mnuWord = new JMenuItem("Find Word");
		mb.add(file);
		mb.add(bandnames);
		file.add(mnuSave);
		file.add(mnuQuit);
		bandnames.add(mnuGenerate);
		bandnames.add(mnuDefinition);
		bandnames.add(mnuWord);
				
		generate.addActionListener(this);
		save.addActionListener(this);
		quit.addActionListener(this);
		chkAddOn.addItemListener(this);
		btnDef.addActionListener(this);
		btnFind.addActionListener(this);
		mnuSave.addActionListener(this);
		mnuQuit.addActionListener(this);
		mnuGenerate.addActionListener(this);
		mnuDefinition.addActionListener(this);
		mnuWord.addActionListener(this);
		
		
		display.setLineWrap(true);
	        display.setWrapStyleWord(true);
		display.setMargin(new Insets(5,5,5,5));
		display.setFont(new Font("Dialog", Font.PLAIN, 11));
		bandList.setFont(new Font("Dialog", Font.PLAIN, 11));
		
		// set color
		Color theColor = Color.WHITE;
		generate.setBackground(theColor);
		save.setBackground(theColor);
		quit.setBackground(theColor);
		chkAddOn.setBackground(theColor);
		file.setBackground(theColor);
		bandnames.setBackground(theColor);
		mb.setBackground(theColor);
		btnDef.setBackground(theColor);
		btnFind.setBackground(theColor);
		mnuSave.setBackground(theColor);
		mnuQuit.setBackground(theColor);
		mnuGenerate.setBackground(theColor);
		mnuDefinition.setBackground(theColor);
		mnuWord.setBackground(theColor);
		row0.setBackground(theColor);
		row1.setBackground(theColor);
		row2.setBackground(theColor);
		
		
		setContentPane(pane);
		setVisible(true);	
		
	}
	
	public void itemStateChanged(ItemEvent ie) {
		Object source = ie.getSource();
		if (source == chkAddOn)
			append_data = !append_data;
	}
	
	public void actionPerformed(ActionEvent event) {
		String theEvent = event.getActionCommand();
		Object source = event.getSource();
		if (theEvent.equals("Generate") || source == mnuGenerate) {
			start();
		} else if (theEvent.equals("Quit") || source == mnuQuit) {
			System.exit(0);
		} else if (theEvent.equals("Definition") || source == mnuDefinition) {
			try {
				getDef();
			} catch (IOException ioe) { 
			}
		} else if (theEvent.equals("Save Data") || source == mnuSave) {
			try {
				int stray = save_data();
			}catch (IOException ie) {
			}
		} else if (theEvent.equals("Find Word") || source == mnuWord) {
			try {
				String theInput = JOptionPane.showInputDialog("Please enter a word to search for:");
				if (theInput != null) {
					StringBuffer sb = new StringBuffer(theInput);
					sb.setCharAt(0, (theInput.toUpperCase()).charAt(0));
					theInput = new String(sb);
					display.setText("Definition of " + theInput.substring(0, theInput.length()) + ":\n" + findDef(theInput));
				}
			} catch (IOException ioe) {
				
			}
		}
	}
	
	public int randInt(int lo, int hi) {
 		int n = hi - lo + 1;
                int i = rn.nextInt() % n;
                if (i < 0)
                	i = -i;
                return lo + i;
	}
	
	public int save_data() throws IOException {
		String source = "";
		for (int i=0; i<listModel.getSize(); i++) {
			bandList.setSelectedIndex(i);
			source += (bandList.getSelectedValue()).toString() + "\n";
		}
		String theInput = JOptionPane.showInputDialog("Please enter a file name:");
		if (theInput == null) {
			JOptionPane.showMessageDialog(null, "File not saved.", "Band Generator", JOptionPane.INFORMATION_MESSAGE);
		} else {
			char buffer[] = new char[source.length()];
			source.getChars(0, source.length(), buffer, 0);
			FileWriter f1 = new FileWriter(theInput);
			f1.write(buffer);
			f1.close();
			JOptionPane.showMessageDialog(null, "File saved.", "Band Generator", JOptionPane.INFORMATION_MESSAGE);
		}
		return 0;
	}
	
	public String randWord(String vocab) throws IOException {
		FileReader fr;
		BufferedReader br;
		String data = null;
		String word = null;
		String type = null;
		char ch;
		int pos;
		
		fr = new FileReader("dictionary.txt");
		br = new BufferedReader(fr);
		int rand_line = randInt(2, 156352);
		
		/* Get the line of data from the dictionary */
		for (int i=1; i<(rand_line-1); i++) {
			data = br.readLine();
		}
		do {
			data = br.readLine();
			/* Extract its vocabulary type */
			for (pos=0; pos<data.length(); pos++) {
				ch = data.charAt(pos);
				if (ch == ' ')
					break;
			}
			word = data.substring(0,pos);
			if (word.length() > 2)
				type = data.substring(word.length()+2, pos+3);
			if (type.charAt(0) == 'n')
				type = "noun";
			else if (type.charAt(0) == 'a')
				type = "adjetive";
			else if (type.charAt(0) == 'v')
				type = "verb";
			else
				type = "null";
			
		} while (type != vocab);
		for (pos += 1; pos<data.length(); pos++) {
				ch = data.charAt(pos);
				if (ch == ' ')
					break;
		}
		fr.close();
		return word;
	}
	
	public String findDef(String getdef) throws IOException {
		FileReader fr;
		BufferedReader br;
		String data = null;
		String word = null;
		String type = null;
		String definition = "No such word in dictionary.";
		char ch;
		int pos;
		
		// to ensure better finds
		getdef = getdef + " (";
		fr = new FileReader("dictionary.txt");
		br = new BufferedReader(fr);
		int rand_line = randInt(2, 156352);
		
		/* Get the line of data from the dictionary */
		for (int i=1; i<156352; i++) {
			data = br.readLine();
			if (data.indexOf(getdef) != -1)
				break;
		}
		// just in case...
		if (data.indexOf(getdef) == -1)
			return definition;
		/* Extract its vocabulary type */
		for (pos=0; pos<data.length(); pos++) {
			ch = data.charAt(pos);
			if (ch == ' ')
				break;
		}
		word = data.substring(0,pos);
		for (pos += 1; pos<data.length(); pos++) {
			ch = data.charAt(pos);
			if (ch == ')')
				break;
		}
		definition = data.substring(pos+2, data.length()-1);
		fr.close();
		return definition;		
	}
	
	public void run() {
		long start = 0;
		long end = 0;
		int band_num = 0;
		try {
			String inputValue = JOptionPane.showInputDialog("How many band names to generate?");
			if (inputValue != null) {
				if (!append_data)
					listModel.clear();
				try {
					band_num = Integer.parseInt(inputValue);
				} catch (Exception e) {
					band_num =0;
					display.setText("Must enter positive integer!");
				}
				start = System.currentTimeMillis();
				for (int i=1; i<=band_num; i++) {
					int choice = randInt(1,3);
					String bandname = "";
					switch (choice) {
						case 1:
							bandname += "The " + randWord("adjetive") + " " + randWord("noun") + "s";
							break;
						case 2:
							bandname += randWord("verb") + "ing " + randWord("noun") + "s";
							break;
						case 3:
							bandname += "A " + randWord("noun") + " " + randWord("noun");
							break;
						default:
							bandname = null;
					}
					listModel.addElement(bandname);
				}
				end = System.currentTimeMillis();
				JOptionPane.showMessageDialog(null, "Generating complete.\nElapsed time (seconds): " + (float)(end-start)/1000, "Band Generator", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException ioe) {
			System.out.println("Serious Error: " + ioe);
		}
		stop();
	}
	
	public void start() {
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
		}
	}
	
	public void stop() {
		if (runner != null) {
			runner = null;
		}
	}

	public void getDef() throws IOException {
		int pos;
		int chd = 0;
		char ch;
		if (bandList.getSelectedIndex() != -1) {
			String item = (bandList.getSelectedValue()).toString();
			String word1;
			String word2;
			String definition = null;
			for (pos=0; pos<item.length(); pos++) {
				ch = item.charAt(pos);
				if (ch == ' ')
					chd++;
				if (chd==2)
					break;
			}
			if (item.substring(0,3).equals("The")) {
				word1 = item.substring(4, pos);
				word2 = item.substring(pos+1, item.length()-1);
				definition = "The (" + findDef(word1) + ") (" + findDef(word2) + ").";
			} else if (item.substring(0,2).equals("A ")) {
				word1 = item.substring(3, pos);
				word2 = item.substring(pos+1, item.length());
				definition = "A (" + findDef(word1) + ") (" + findDef(word2) + ").";
			} else {
				for (pos=0; pos<item.length(); pos++) {
					ch = item.charAt(pos);
					if (ch == ' ')
						break;
				}
				word1 = item.substring(0, pos-3);
				word2 = item.substring(pos+1, item.length()-1);
				definition = "(" + findDef(word1) + ") (" + findDef(word2) + ").";
			}
			display.setText("Definition of [" + item + "]:\n" + definition);
		} else
			display.setText("Must select a band name!");
	}
}

