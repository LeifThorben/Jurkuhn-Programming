/*
 * Author: 		Leif Jurkuhn
 * FileName: 	Finallab.java
 * Date: 		01.02.2020
 */

package finallab;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Finallab extends JFrame implements ActionListener {

	// attribute
	private JTextField xmin, xmax, ymin, ymax, multithreadsetter;
	private JButton draw, save, load;
	private double xminnum, xmaxnum, yminnum, ymaxnum, multithreadset;
	private int multithreadsetint;
	private Mandelbrot mandelbrot;
	JFileChooser chooser = new JFileChooser();

	// exception Handler
	private class containsCharacterException extends Exception {

		public containsCharacterException(String message) {
			super(message);
		}
	}

	// Constructor
	public Finallab() {
		// JFrame settings
		super("Finallab");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(1000, 700));
		setLocation(new Point(100, 100));
		setTitle("Finallab");
		Container c = getContentPane();
		c.setBackground(Color.white);
		c.setLayout(new BorderLayout());
		JPanel buttons = new JPanel();
		buttons.setBackground(Color.lightGray);
		buttons.setLayout(new GridLayout(8, 6, 10, 10));

		// textfield settings
		xmin = new JTextField(5);
		xmin.addActionListener(this);
		xmin.setText("-1.5");
		xmax = new JTextField(5);
		xmax.addActionListener(this);
		xmax.setText("-0.5");
		ymin = new JTextField(5);
		ymin.addActionListener(this);
		ymin.setText("-0.25");
		ymax = new JTextField(5);
		ymax.addActionListener(this);
		ymax.setText("0.25");
		multithreadsetter = new JTextField(1);
		multithreadsetter.addActionListener(this);
		multithreadsetter.setText("4");

		// Button Settings
		draw = new JButton("Draw");
		draw.addActionListener(this);
		save = new JButton("Save");
		save.addActionListener(this);
		load = new JButton("Load");
		load.addActionListener(this);

		// Add all interactable GUI to the buttons jpanel
		buttons.add(new JLabel("X-min"));
		buttons.add(xmin);
		buttons.add(new JLabel("X-max"));
		buttons.add(xmax);
		buttons.add(new JLabel("Y-min"));
		buttons.add(ymin);
		buttons.add(new JLabel("Y-max"));
		buttons.add(ymax);
		buttons.add(new JLabel("MultiThread"));
		buttons.add(multithreadsetter);
		buttons.add(draw);
		buttons.add(save);
		buttons.add(load);

		// create new instance of mandelbrot class
		mandelbrot = new Mandelbrot();

		// add GUI and Mandelbrot GUI to c containter
		c.add(buttons, BorderLayout.LINE_END);
		c.add(mandelbrot, BorderLayout.CENTER);

		setVisible(true);

	}

	// Method to check whether the text in the textfields are numbers or not
	private void isInputValid(final String input) throws containsCharacterException {
		String invalidCharacters = "";
		for (int i = 0; i < input.length(); i++) {
			if (!Character.isDigit(input.charAt(i))) {
				invalidCharacters += input.charAt(i);
			}
		}
		// throws exception that there is a Character that is invalid
		if (invalidCharacters.length() > 0) {
			throw (new containsCharacterException(invalidCharacters));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// actions performed after clicking the Draw button
		// test if inserted Text is actually a valid input
		if (e.getSource() == draw) {
			try {
				isInputValid(xmin.getText());
				isInputValid(xmax.getText());
				isInputValid(ymin.getText());
				isInputValid(ymax.getText());
				isInputValid(multithreadsetter.getText());
			} catch (containsCharacterException exc) { // Throw exception
				JOptionPane.showMessageDialog(null, "The following Characters are invalid: " + exc.getMessage());
				return;
			}
			// convert text to double value
			xminnum = Double.parseDouble(xmin.getText());
			xmaxnum = Double.parseDouble(xmax.getText());
			yminnum = Double.parseDouble(ymin.getText());
			ymaxnum = Double.parseDouble(ymax.getText());
			multithreadset = Double.parseDouble(multithreadsetter.getText());
			// convert double multithread value to INT value
			multithreadsetint = (int) multithreadset;
			// set amount of threads to use
			mandelbrot.setT(multithreadsetint);
			// set limits of the displayed mandelbrot image
			mandelbrot.setLimits(xminnum, xmaxnum, yminnum, ymaxnum);
			mandelbrot.repaint();
		}

		// actions performed after clicking the save button
		if (e.getSource() == save) {
			// create new Filechooser with filefilter
			chooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
			// open filechooser gui
			if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				// try to save mandelbrot image to selected path
				try {
					ImageIO.write((BufferedImage) mandelbrot.getImage(), "png", new File(file.getAbsolutePath()));
				} catch (IOException ex) { // error messages
					System.out.println("Failed to save image!");
				}
			} else {
				System.out.println("No file choosen!");
			}

		}

		// actions performed after clicking load buttons
		if (e.getSource() == load) {
			// create new filechooser file filter
			chooser.setFileFilter(new FileNameExtensionFilter("*.png", "png"));
			// open filechooser load GUI
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				try {// load image from selected path and set as Mandelbrot image
					ImageIO.read(new File(file.getAbsolutePath()));
					BufferedImage img = ImageIO.read(file);
					mandelbrot.setImage(img);
				} catch (IOException ex) {
					System.out.println("Failed to load image!");
				}
			} else {
				System.out.println("No file choosen!");
			}
		}

	}

	// main to run as jfile
	public static void main(String args[]) {
		new Finallab();
	}
}
