import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;

/*
 * Vigenere Encrypter
 * 
 * Author: Thomas Auberson
 * Version: 0.141
 * 
 * This class controls a JFrame window with a single JPanel canvas display.  Extracted from Template Library v0.12b
 */

public class Display extends JPanel {

	private static final long serialVersionUID = -5910732331541907943L;
	// FIELDS
	private String TITLE = "Vigenere Encrypter";
	private String VERSION = "0.141"; // *RREQUIRES DEFINITION
	private String AUTHOR = "Thomas Auberson";
	private String DESCRIPTION = "<br><br>Encrypt or decrypt a text document using a Vigenere Cipher.<br>Preserves case and ignores punctuation and spaces."; // *RREQUIRES DEFINITION
	private int SIZE_X = 300, SIZE_Y = 200;
	private boolean RESIZABLE = false;

	private JFrame frame;
	private MenuBar menu;
	private Cipher cipher = new Cipher(); // Cipher class handles actual encryption/decryption
	private File input, output; // Input/Source and Output/Target files ==> Files to be en/decrpted from and to
	private JTextField keyField; // TextField for user to specify cipher key
	private JTextField[] urlFields = new JTextField[2]; // Textfields displaying input and output file URLs

	// CONSTRUCTOR
	public Display() {
		// Initialize the Display
		frame = createFrame();
		frame.add(this);

		// Initialize Menu Bar
		menu = new MenuBar(this); 
		frame.setJMenuBar(menu);

		// GUI Layout
		this.setLayout(new GridLayout(5, 1));
		JPanel tempPanel; // Reusable JPanel variable
		JButton tempButton; // Reusable JButton variable

		// Key Field and Label
		tempPanel = new JPanel();
		tempPanel.setLayout(new GridLayout(1, 2));
		tempPanel.add(new JLabel("Key:")); // Label for key field
		tempPanel.add(keyField = new JTextField());
		this.add(tempPanel); // Add row to GUI

		// Encrypt / Decrypt buttons at bottom
		tempPanel = new JPanel(); 
		tempPanel.setLayout(new GridLayout(1, 2));
		tempPanel.add(tempButton = new JButton("Encrypt"));
		tempButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				encrypt();
			}
		});
		tempPanel.add(tempButton = new JButton("Decrypt"));
		tempButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				decrypt();
			}
		});
		this.add(tempPanel); // Add row to GUI

		// Input File URL
		tempPanel.add(urlFields[0] = new JTextField("Source:"));
		urlFields[0].setEditable(false);
		this.add(urlFields[0]); // Add row to GUI

		// Output File URL
		tempPanel.add(urlFields[1] = new JTextField("Target:"));
		urlFields[1].setEditable(false);
		this.add(urlFields[1]); // Add row to GUI

		// Input / Output Buttons
		tempPanel = new JPanel();
		tempPanel.setLayout(new GridLayout(1, 2));
		tempPanel.add(tempButton = new JButton("Source File"));
		tempButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chooseInputFile();
			}
		});
		tempPanel.add(tempButton = new JButton("Target File"));
		tempButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chooseOutputFile();
			}
		});
		this.add(tempPanel); // Add row to GUI

		// Start Display
		frame.setVisible(true);
	}

	private JFrame createFrame() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(SIZE_X, SIZE_Y);
		frame.setTitle(TITLE);
		frame.setResizable(RESIZABLE);
		frame.setLocationRelativeTo(null); // Sets window in centre
		frame.setLayout(new GridLayout(1, 1, 0, 0));
		return frame;
	}

	// MENU ACTION LISTENER
	public void menuButtonClicked(String button) {
		switch (button) {
		case "About":
			JOptionPane.showMessageDialog(this, "<html>" + TITLE + "<br>Version: " + VERSION + "<br>Author: " + AUTHOR + DESCRIPTION + "</html>", "About", JOptionPane.PLAIN_MESSAGE);
			break;
		}
	}
	
	// CIPHER METHODS
	public void encrypt() {
		if(input == null){ // Force User to choose an Input directory
			JOptionPane.showMessageDialog(this, "No source file specified!","WARNING",JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(output == null){ // If no output directory specified choose a default
			output = new File(input.getParent(),"encrypted.txt");
		}
		cipher.setKey(keyField.getText()); // Take the key from the key text field and set it to the cipher
		try {
			BufferedReader scan = new BufferedReader(new FileReader(input)); // Create an input reader and output writer
			BufferedWriter prin = new BufferedWriter(new FileWriter(output));

			cipher.encrypt(scan, prin); // Give the reader and writer to cipher to encrypt text

			scan.close();
			prin.close();		
			JOptionPane.showMessageDialog(this, "Encryption Successful", "Completed", JOptionPane.PLAIN_MESSAGE);
			input = null;
			output = null;	
			urlFields[0].setText("Source: ");  // Display URL of file on a GUI textfield
			urlFields[1].setText("Target: ");  // Display URL of file on a GUI textfield			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Failed to load file!", "Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void decrypt() {
		if(input == null){ // Force User to choose an Input directory
			JOptionPane.showMessageDialog(this, "No source file specified!","WARNING",JOptionPane.WARNING_MESSAGE);
			return;
		}
		if(output == null){ // If no output directory specified choose a default
			output = new File(input.getParent(),"decrypted.txt");
		}
		cipher.setKey(keyField.getText());  // Take the key from the key text field and set it to the cipher
		try {
			BufferedReader scan = new BufferedReader(new FileReader(input)); // Create an input reader and output writer
			BufferedWriter prin = new BufferedWriter(new FileWriter(output));

			cipher.decrypt(scan, prin); // Give the reader and writer to cipher to decrypt text

			scan.close();
			prin.close();
			JOptionPane.showMessageDialog(this, "Decryption Successful", "Completed", JOptionPane.PLAIN_MESSAGE);
			output = null;
			input = null;	
			urlFields[0].setText("Source: ");  // Display URL of file on a GUI textfield
			urlFields[1].setText("Target: ");  // Display URL of file on a GUI textfield
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Failed to load file!", "Warning", JOptionPane.WARNING_MESSAGE);

		}
	}

	// CHOOSE DIRECTORIES
	public void chooseInputFile() {
		JFileChooser loader = new JFileChooser(); // Create a JFileChooser Window and get user to specify a file
		loader.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int n = loader.showOpenDialog(this);
		if (n == JFileChooser.APPROVE_OPTION) {
			try {
				input = loader.getSelectedFile(); // Set specified file to input
				urlFields[0].setText("Source: "+input.getCanonicalPath()); // Display URL of file on a GUI textfield
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "File path not found:", "WARNING", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void chooseOutputFile() {
		JFileChooser loader = new JFileChooser(); // Create a JFileChooser Window and get user to specify a file
		loader.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int n = loader.showSaveDialog(this);
		if (n == JFileChooser.APPROVE_OPTION) {
			try {
				output = loader.getSelectedFile(); // Set specified file to input
				urlFields[1].setText("Target: "+output.getCanonicalPath());  // Display URL of file on a GUI textfield
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "File path not found:", "WARNING", JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	// MAIN
	public static void main(String[] args) {
		new Display();
	}
}